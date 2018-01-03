package com.kisen.kdownload.download.db;

import com.kisen.kdownload.download.entity.ThreadInfo;

import java.util.List;

/**
 * Created by huangwy on 2017/12/26.
 * email: kisenhuang@163.com.
 */

public interface ThreadDao {

    //插入线程
    void insertThread(ThreadInfo info);

    //删除线程
    void deleteThread(String url, int threadId);

    //更新线程
    void updateThread(String url, int threadId, int finished);

    //查询线程
    List<ThreadInfo> queryThread(String url);

    //判断线程是否存在
    boolean isExists(String url, int threadId);

}
