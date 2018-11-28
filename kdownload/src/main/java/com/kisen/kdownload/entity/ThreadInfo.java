package com.kisen.kdownload.entity;

/**
 * Created by huangwy on 2017/12/26.
 * email: kisenhuang@163.com.
 */

public class ThreadInfo {

    private String fileName;
    private int id;
    private String url;
    private long totalSize;
    private long downloadTime;
    private long finished;

    public ThreadInfo() {
        id = hashCode();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(long downloadTime) {
        this.downloadTime = downloadTime;
    }

    public long getFinished() {
        return finished;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }
}
