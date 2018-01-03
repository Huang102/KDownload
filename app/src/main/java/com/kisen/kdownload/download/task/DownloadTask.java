package com.kisen.kdownload.download.task;

import android.os.Message;
import android.util.Log;

import com.kisen.kdownload.download.entity.FileInfo;
import com.kisen.kdownload.download.entity.ThreadInfo;
import com.kisen.kdownload.download.net.Call;
import com.kisen.kdownload.download.net.CallImpl;
import com.kisen.kdownload.download.db.ThreadDao;
import com.kisen.kdownload.download.db.ThreadDaoImpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * 下载线程
 * Created by huangwy on 2017/12/26.
 * email: kisenhuang@163.com.
 */

public class DownloadTask implements Task {

    private FileInfo fileInfo = null;
    private ThreadInfo threadInfo = null;
    private ThreadDao mDao;
    private CallImpl call;
    private int mFinished;

    DownloadTask(CallImpl call) {
        this.call = call;
        fileInfo = call.getFileInfo();
        mDao = new ThreadDaoImpl(call.getContext());
    }

    @Override
    public void download() {
        getThreadInfo();
        new Thread() {
            @Override
            public void run() {
                if (!mDao.isExists(threadInfo.getUrl(), threadInfo.getId())) {
                    mDao.insertThread(threadInfo);
                }
                HttpURLConnection mConnection = null;
                InputStream is = null;
                RandomAccessFile raf = null;
                try {
                    URL url = new URL(threadInfo.getUrl());
                    mConnection = (HttpURLConnection) url.openConnection();
                    //builder
                    mConnection.setConnectTimeout(10 * 1000);
                    mConnection.setReadTimeout(5 * 1000);
                    mConnection.setRequestMethod("GET");

                    long start = threadInfo.getStart() + threadInfo.getFinished();
                    mConnection.setRequestProperty("Range", "bytes=" + start + "-" + threadInfo.getEnd());
                    File file = new File(fileInfo.getRootPath(), threadInfo.getFileName());
                    raf = new RandomAccessFile(file, "rwd");
                    raf.seek(start);
                    mFinished += threadInfo.getFinished();

                    long startTime = System.currentTimeMillis();
                    float progress = 0;
                    if (mConnection.getResponseCode() == HttpURLConnection.HTTP_PARTIAL) {//连接成功
                        Log.i("KHttp", "下载开始");
                        is = mConnection.getInputStream();
                        byte[] bytes = new byte[1024];
                        int len = -1;

                        long time = System.currentTimeMillis();
                        while ((len = is.read(bytes)) != -1) {
                            raf.write(bytes, 0, len);
                            mFinished += len;

                            //发送通知
                            //更新下载进度
                            long tempTime = System.currentTimeMillis();
                            if (tempTime - time > 100) {
                                time = tempTime;
                                progress = mFinished * 100f / fileInfo.getLength();
                                Log.i("DownloadTask-progress:", "" + progress);
                                Message obtain = Message.obtain(null,
                                        Call.WHAT_PROGRESS, mFinished, fileInfo.getLength());
                                call.callToMain(obtain);
                            }

                            //暂停下载
                            if (call.getDownloadState() == Call.STATE_PAUSE) {
                                mDao.updateThread(threadInfo.getUrl(), threadInfo.getId(), mFinished);
                                Message obtain = Message.obtain(null,
                                        Call.WHAT_PAUSE, mFinished, fileInfo.getLength());
                                call.callToMain(obtain);
                                return;
                            }
                        }
                        is.close();
                        raf.close();
                    }
                    Log.i("KHttp", "下载完成");
                    //下载完成，删除数据库信息。
                    mDao.deleteThread(threadInfo.getUrl(), threadInfo.getId());
                    Log.e("DownloadTask", "File size = " + fileInfo.getLength() + " time = " + (System.currentTimeMillis() - startTime));

                    if (progress != 100) {
                        Message obtain = Message.obtain(null,
                                Call.WHAT_PROGRESS, 100, 100);
                        call.callToMain(obtain);
                    }

                    Message obtain = Message.obtain(null, Call.WHAT_COMPLETE);
                    call.callToMain(obtain);
                    call.changeState(Call.STATE_COMPLETE);

                } catch (IOException e) {
                    e.printStackTrace();
                    call.changeState(Call.STATE_ERROR);
                    Message obtain = Message.obtain(null, Call.WHAT_ERROR, e);
                    call.callToMain(obtain);
                } finally {
                    if (mConnection != null)
                        mConnection.disconnect();
                    try {
                        if (is != null)
                            is.close();
                        if (raf != null)
                            raf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                super.run();
            }
        }.start();
    }

    private void getThreadInfo() {
        List<ThreadInfo> threadInfos = mDao.queryThread(fileInfo.getUrl());
        if (threadInfos.size() == 0) {
            threadInfo = new ThreadInfo();
            threadInfo.setUrl(fileInfo.getUrl());
            threadInfo.setEnd(fileInfo.getLength());
            threadInfo.setFileName(fileInfo.getFileName());
        } else {
            threadInfo = threadInfos.get(0);
        }
    }

}
