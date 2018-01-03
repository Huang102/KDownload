package com.kisen.kdownload.download.db;

import com.kisen.kdownload.download.entity.FileInfo;

import java.util.List;

/**
 * Created by huangwy on 2017/12/28.
 * email: kisenhuang@163.com.
 */

public interface FileDao {

    //插入文件
    void insertFile(FileInfo info);

    //删除文件
    void deleteFile(String url, int fileId);

    //更新文件
    void updateFile(String url, int fileId, int finished);

    //查询文件
    List<FileInfo> queryFile(String url);

    //查询所有文件
    List<FileInfo> queryAllFile();

    //判断文件是否存在
    boolean isExists(String url);
}
