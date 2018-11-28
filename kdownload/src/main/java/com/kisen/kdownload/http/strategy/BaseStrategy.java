package com.kisen.kdownload.http.strategy;

import com.kisen.kdownload.db.ThreadDao;
import com.kisen.kdownload.db.ThreadDaoImpl;
import com.kisen.kdownload.entity.FileInfo;
import com.kisen.kdownload.entity.ThreadInfo;
import com.kisen.kdownload.http.IHttpStrategy;
import com.kisen.kdownload.net.CallImpl;
import com.kisen.kdownload.net.ContentTypeMap;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.List;

/**
 * description:
 * author: Kisenhuang
 * email: Kisenhuang@163.com
 * time: 2018/11/27 下午5:58
 */
abstract class BaseStrategy implements IHttpStrategy {

    ThreadInfo getThreadInfo(FileInfo fileInfo, ThreadDao dao) {
        ThreadInfo threadInfo;
        List<ThreadInfo> threadInfos = dao.queryThread(fileInfo.getUrl());
        if (threadInfos.size() == 0) {
            threadInfo = new ThreadInfo();
            threadInfo.setUrl(fileInfo.getUrl());
            threadInfo.setTotalSize(fileInfo.getLength());
            threadInfo.setFileName(fileInfo.getFileName());
        } else {
            threadInfo = threadInfos.get(0);
        }
        return threadInfo;
    }

    void checkDB(CallImpl call, File file) {
        ThreadDao dao = new ThreadDaoImpl(call.getContext());
        List<ThreadInfo> threadInfos = dao.queryThread(call.getFileInfo().getUrl());
        if (threadInfos.size() != 0) {//有未完成下载任务。
            if (!file.exists()) {
                ThreadInfo info = threadInfos.get(0);
                dao.deleteThread(info.getUrl(), info.getId());
            }
        }
    }

    void setupExpand(CallImpl call, HttpURLConnection conn) {
        String expand = ContentTypeMap.getExpandByContentType(conn.getContentType());
        String fileName = call.getFileInfo().getFileName();
        call.getFileInfo().setFileName(fileName + expand);
    }
}
