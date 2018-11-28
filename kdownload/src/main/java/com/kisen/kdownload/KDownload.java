package com.kisen.kdownload;

import android.content.Context;

import com.kisen.kdownload.callback.FileCallback;
import com.kisen.kdownload.callback.FileProgressCallback;
import com.kisen.kdownload.db.FileDao;
import com.kisen.kdownload.db.FileDaoImpl;
import com.kisen.kdownload.entity.FileInfo;
import com.kisen.kdownload.http.strategy.HttpUrlConnectionStrategy;
import com.kisen.kdownload.manager.CallManager;
import com.kisen.kdownload.manager.HttpManager;
import com.kisen.kdownload.net.Call;
import com.kisen.kdownload.util.Util;

import java.util.List;

/**
 * description:
 * author: Kisenhuang
 * email: Kisenhuang@163.com
 * time: 2018/11/27 下午5:37
 */
public class KDownload {

    private static KDownload mInstance;

    public static KDownload getInstance() {
        if (mInstance == null) {
            synchronized (KDownload.class) {
                if (mInstance == null)
                    mInstance = new KDownload();
            }
        }
        return mInstance;
    }

    private KDownload() {
        HttpManager.getManager().setHttpStrategy(new HttpUrlConnectionStrategy());
    }

    public Call call(Context context, String url, String folderPath, FileCallback callback) {
        return CallManager.map(url, context, folderPath, callback);
    }

    public void getFileProgress(Context context, String url, FileProgressCallback iProgress) {
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

    public List<FileInfo> getDownloadFileList(Context context) {
        FileDao fileDao = new FileDaoImpl(context);
        return fileDao.queryAllFile();
    }

}
