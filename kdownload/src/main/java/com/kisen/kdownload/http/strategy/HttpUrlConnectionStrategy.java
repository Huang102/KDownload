package com.kisen.kdownload.http.strategy;

import android.os.Message;

import com.kisen.kdownload.db.ThreadDaoImpl;
import com.kisen.kdownload.entity.FileInfo;
import com.kisen.kdownload.entity.ThreadInfo;
import com.kisen.kdownload.http.ssl.KSSLManager;
import com.kisen.kdownload.net.Call;
import com.kisen.kdownload.net.CallImpl;
import com.kisen.kdownload.util.KLog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * description: HttpUrlConnection策略类
 * author: Kisenhuang
 * email: Kisenhuang@163.com
 * time: 2018/11/27 下午5:35
 */
public class HttpUrlConnectionStrategy extends BaseStrategy {

    @Override
    public void download(final CallImpl call) {
        new Thread() {
            @Override
            public void run() {
                int mFinished = 0;
                ThreadDaoImpl mDao = new ThreadDaoImpl(call.getContext());
                FileInfo fileInfo = call.getFileInfo();
                ThreadInfo threadInfo = getThreadInfo(fileInfo, mDao);
                if (!mDao.isExists(threadInfo.getUrl(), threadInfo.getId())) {
                    mDao.insertThread(threadInfo);
                }
                HttpsURLConnection mConnection = null;
                InputStream is = null;
                RandomAccessFile raf = null;
                try {
                    URL url = new URL(threadInfo.getUrl());
                    mConnection = (HttpsURLConnection) url.openConnection();
                    //builder
                    setHttps(mConnection);
                    mConnection.setConnectTimeout(10 * 1000);
                    mConnection.setReadTimeout(5 * 1000);
                    mConnection.setRequestMethod("GET");

                    long start = threadInfo.getFinished();
                    mConnection.setRequestProperty("Range", "bytes=" + start + "-" + threadInfo.getTotalSize());
                    File file = new File(fileInfo.getRootPath(), threadInfo.getFileName());
                    raf = new RandomAccessFile(file, "rwd");
                    raf.seek(start);
                    mFinished += threadInfo.getFinished();

                    long startTime = System.currentTimeMillis();
                    float progress = 0;
                    if (mConnection.getResponseCode() == HttpURLConnection.HTTP_PARTIAL) {//连接成功
                        KLog.i("download  下载开始");
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
                            if (tempTime - time > 500) {
                                time = tempTime;
                                progress = mFinished * 100f / fileInfo.getLength();
                                KLog.i("downloading...  progress : " + progress);
                                Message obtain = Message.obtain(null,
                                        Call.WHAT_PROGRESS, mFinished, fileInfo.getLength());
                                call.callToMain(obtain);
                            }

                            //暂停下载
                            if (call.getDownloadState() == Call.STATE_PAUSE) {
                                long downloadTime = System.currentTimeMillis() - startTime;
                                mDao.updateThread(threadInfo.getUrl(), threadInfo.getId(), mFinished, downloadTime);
                                Message obtain = Message.obtain(null,
                                        Call.WHAT_PAUSE, mFinished, fileInfo.getLength());
                                call.callToMain(obtain);
                                return;
                            }
                        }
                        is.close();
                        raf.close();
                    }
                    KLog.i("download 下载完成");
                    //下载完成，删除数据库信息。
                    mDao.deleteThread(threadInfo.getUrl(), threadInfo.getId());
                    long downloadTime = System.currentTimeMillis() - startTime + threadInfo.getDownloadTime();
                    KLog.i("download File size = " + fileInfo.getLength() + " time = " + downloadTime);

                    if (progress >= 100) {
                        Message obtain = Message.obtain(null,
                                Call.WHAT_PROGRESS, 100, 100);
                        call.callToMain(obtain);
                    }

                    Message obtain = Message.obtain(null, Call.WHAT_COMPLETE);
                    call.callToMain(obtain);
                    call.changeState(Call.STATE_COMPLETE);

                } catch (IOException e) {
                    e.printStackTrace();
                    KLog.e("download error", e);
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

    @Override
    public void getFileInfo(final CallImpl call) {
        HttpsURLConnection conn = null;
        RandomAccessFile raf = null;
        try {
            URL url = new URL(call.getFileInfo().getUrl());
            conn = (HttpsURLConnection) url.openConnection();
            setHttps(conn);
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept-Encoding", "gzip");
            int code = conn.getResponseCode();
            int length = -1;
            if (code == HttpURLConnection.HTTP_OK) {
                length = conn.getContentLength();
            }
            //如果文件长度为小于0，表示获取文件失败，直接返回
            if (length <= 0) {
                KLog.e("info 文件获取失败");
                call.changeState(Call.STATE_ERROR);
                Message obtain = Message.obtain(null, Call.WHAT_ERROR, new Throwable("文件信息获取失败"));
                call.callToMain(obtain);
                return;
            }

            //设置文件拓展名
            setupExpand(call, conn);

            File dir = new File(call.getFileInfo().getRootPath());
            if (!dir.exists()) {
                dir.mkdir();
            }
            // TODO: 2017/12/27 如果中途用户删除本地未完成文件，需要做处理。
            File file = new File(dir, call.getFileInfo().getFileName());
            checkDB(call, file);
            raf = new RandomAccessFile(file, "rwd");
            raf.setLength(length);
            call.getFileInfo().setLength(length);

            KLog.i("info, length: " + length + ", start download");
            // 获取文件信息成功，准备下载。
            call.changeState(Call.STATE_READY);

        } catch (Exception e) {
            e.printStackTrace();
            KLog.e("info error", e);
            call.changeState(Call.STATE_ERROR);
            Message obtain = Message.obtain(null, Call.WHAT_ERROR, e);
            call.callToMain(obtain);
        } finally {
            if (conn != null)
                conn.disconnect();
            try {
                if (raf != null)
                    raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setHttps(HttpsURLConnection conn) {
        conn.setSSLSocketFactory(KSSLManager.createSSLSocketFactory());
    }


}
