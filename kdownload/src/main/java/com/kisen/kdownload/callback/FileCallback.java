package com.kisen.kdownload.callback;


import com.kisen.kdownload.net.Call;

/**
 * Created by huangwy on 2017/12/27.
 * email: kisenhuang@163.com.
 */

public interface FileCallback {

    void onProgress(Call call, int size, int totalSize);

    void onPause(Call call, int size, int totalSize);

//    void onRetry(Call call)

    void onComplete(Call call);

    void onError(Call call, Throwable t);

}
