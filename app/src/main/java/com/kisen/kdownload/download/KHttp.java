package com.kisen.kdownload.download;


import android.content.Context;

import com.kisen.kdownload.download.callback.FileCallback;
import com.kisen.kdownload.download.db.FileDao;
import com.kisen.kdownload.download.db.FileDaoImpl;
import com.kisen.kdownload.download.entity.FileInfo;
import com.kisen.kdownload.download.manager.CallManager;
import com.kisen.kdownload.download.net.Call;
import com.kisen.kdownload.download.util.Util;

import java.util.List;

/**
 * Created by huangwy on 2017/12/26.
 * email: kisenhuang@163.com.
 * 设置文件本地状态存储。
 * 下载完成文件读取。
 */

public class KHttp {

    public static Call call(Context context, String url, String folderPath, FileCallback callback) {
        return mapCall(context, url, folderPath, callback);
    }

    private static Call mapCall(Context context, String url, String folderPath, FileCallback callback) {
        return CallManager.map(url, context, folderPath, callback);
    }

    public static void getFileProgress(Context context, String url, IProgress iProgress) {
        if (iProgress == null)
            return;
        Util.checkNullString(url);
        FileDao fileDao = new FileDaoImpl(context);
        List<FileInfo> fileInfos = fileDao.queryFile(url);
        if (fileInfos.size() == 0)
            iProgress.noProgress();
        else {
            FileInfo fileInfo = fileInfos.get(0);
            iProgress.progress(fileInfo.getFinished(), fileInfo.getLength());
        }
    }

    public static List<FileInfo> getDownloadFileList(Context context) {
        FileDao fileDao = new FileDaoImpl(context);
        return fileDao.queryAllFile();
    }

    public interface IProgress {

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

}
