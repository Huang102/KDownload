package com.kisen.kdownload.net;

import android.content.Context;
import android.os.Message;

import com.kisen.kdownload.callback.FileCallback;
import com.kisen.kdownload.entity.FileInfo;
import com.kisen.kdownload.task.SyncTask;
import com.kisen.kdownload.task.Task;
import com.kisen.kdownload.util.Util;

/**
 * Created by huangwy on 2017/12/27.
 * email: kisenhuang@163.com.
 */

public class CallImpl extends Call {

    private Context context;
    private Task task;
    private CallHandler callHandler;

    /**
     * 下载状态：0,默认未开始;1,正在下载;2,下载完成;3,下载失败
     */
    private int downloadState = Call.STATE_DEFAULT;

    public static Call newCall(Context context, String url, String folderPath, FileCallback callback) {
        return new CallImpl(context, url, folderPath, callback);
    }

    private CallImpl(Context context, String url, String folderPath, FileCallback callback) {
        fileInfo = new FileInfo(url, folderPath);
        fileInfo.setFileName(Util.encode(url, "temp"));
        this.context = context;
        this.callback = callback;
        callHandler = new CallHandler(this, callback);
        createTask();
    }

    public void bind(Context context, FileCallback callback) {
        this.context = context;
        this.callback = callback;
        createTask();
    }

    public Context getContext() {
        return context;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void changeState(int state) {
        downloadState = state;
    }

    public int getDownloadState() {
        return downloadState;
    }

    @Override
    public void start() {
        createTask();
        switch (downloadState) {
            case STATE_DEFAULT:
            case STATE_PAUSE:
            case STATE_ERROR:
                task.download();
                changeState(Call.STATE_LOADING);
                break;
            case STATE_LOADING:
                break;
            case STATE_COMPLETE:
                break;
        }
    }

    private void createTask() {
        if (task == null)
            task = new SyncTask(this);
    }

    @Override
    public void stop() {
        changeState(STATE_PAUSE);
    }

    public void callToMain(Message msg){
        callHandler.sendMessage(msg);
    }
}
