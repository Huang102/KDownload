package com.kisen.kdownload.callback;

/**
 * description:
 * author: Kisenhuang
 * email: Kisenhuang@163.com
 * time: 2018/11/27 下午5:40
 */
public interface FileProgressCallback {

    /**
     * 文件进度
     *
     * @param size      已下载长度
     * @param totalSize 总长度
     */
    void progress(long size, long totalSize);

    /**
     * 没有文件进度
     */
    void noProgress();
}
