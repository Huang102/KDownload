package com.kisen.kdownload.entity;

import java.io.Serializable;

/**
 * Created by huangwy on 2017/12/27.
 * email: kisenhuang@163.com.
 */

public class FileInfo implements Serializable{

    private int id;
    private String url;
    private String fileName;
    private String rootPath;
    private int length;
    private int finished;

    public FileInfo() {
    }

    public FileInfo(String url, String rootPath) {
        this.url = url;
        this.rootPath = rootPath;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }
}
