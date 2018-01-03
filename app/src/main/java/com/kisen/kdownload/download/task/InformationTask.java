package com.kisen.kdownload.download.task;

import android.os.Message;
import android.util.Log;

import com.kisen.kdownload.download.entity.ThreadInfo;
import com.kisen.kdownload.download.net.Call;
import com.kisen.kdownload.download.net.CallImpl;
import com.kisen.kdownload.download.net.ContentTypeMap;
import com.kisen.kdownload.download.db.ThreadDao;
import com.kisen.kdownload.download.db.ThreadDaoImpl;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * 获取文件下载信息，并初始化下载
 * Created by huangwy on 2017/12/27.
 * email: kisenhuang@163.com.
 */

public class InformationTask implements Task {

    private CallImpl call;
    private DownloadTask downloadTask;
    private ThreadDao mDao;

    public InformationTask(CallImpl call) {
        this.call = call;
        mDao = new ThreadDaoImpl(call.getContext());
    }

    private void checkDB(File file) {
        List<ThreadInfo> threadInfos = mDao.queryThread(call.getFileInfo().getUrl());
        if (threadInfos.size() != 0) {//有未完成下载任务。
            if (!file.exists()) {
                ThreadInfo info = threadInfos.get(0);
                mDao.deleteThread(info.getUrl(), info.getId());
            }
        }
    }

    @Override
    public void download() {
        new Thread() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                RandomAccessFile raf = null;
                try {
                    URL url = new URL(call.getFileInfo().getUrl());
                    conn = (HttpURLConnection) url.openConnection();
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
                        Log.e("InformationTask", "文件获取失败");
                        call.changeState(Call.STATE_ERROR);
                        Message obtain = Message.obtain(null, Call.WHAT_ERROR, new Throwable("文件信息获取失败"));
                        call.callToMain(obtain);
                        return;
                    }

                    //设置文件拓展名
                    setupExpand(conn);

                    File dir = new File(call.getFileInfo().getRootPath());
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    // TODO: 2017/12/27 如果中途用户删除本地未完成文件，需要做处理。
                    File file = new File(dir, call.getFileInfo().getFileName());
                    checkDB(file);
                    raf = new RandomAccessFile(file, "rwd");
                    raf.setLength(length);
                    call.getFileInfo().setLength(length);

                    // 获取文件信息成功，准备下载。
                    if (call.getDownloadState() != Call.STATE_PAUSE) {
                        downloadTask = new DownloadTask(call);
                        downloadTask.download();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
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
                super.run();
            }
        }.start();
    }

    private void setupExpand(HttpURLConnection conn) {
        String expand = ContentTypeMap.getExpandByContentType(conn.getContentType());
        String fileName = call.getFileInfo().getFileName();
        call.getFileInfo().setFileName(fileName + expand);
    }

}
