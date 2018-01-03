package com.kisen.kdownload.download.net;

import com.kisen.kdownload.download.callback.FileCallback;
import com.kisen.kdownload.download.entity.FileInfo;

/**
 * Created by huangwy on 2017/12/27.
 * email: kisenhuang@163.com.
 */

public abstract class Call {

    /**
     * 线程状态
     */
    public static final int STATE_DEFAULT = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_PAUSE = 2;
    public static final int STATE_COMPLETE = 3;
    public static final int STATE_ERROR = 4;

    /**
     * Handler 通讯类型
     */
    public static final int WHAT_PROGRESS = 101;
    public static final int WHAT_PAUSE = 102;
    public static final int WHAT_COMPLETE = 103;
    public static final int WHAT_ERROR = 104;

    protected FileCallback callback;
    protected FileInfo fileInfo;

    public String getPath() {
        return fileInfo.getRootPath() + "/" + fileInfo.getFileName();
    }

    public FileCallback getCallback() {
        return callback;
    }

    public abstract void start();

    public abstract void stop();

}
