package com.kisen.kdownload.net;

import android.os.Handler;
import android.os.Message;

import com.kisen.kdownload.callback.FileCallback;

import java.lang.ref.WeakReference;

/**
 * Created by huangwy on 2017/12/28.
 * email: kisenhuang@163.com.
 */

public class CallHandler extends Handler {

    private FileCallback callback;
    private Call call;

    CallHandler(Call call, FileCallback callback) {
        this.callback = new WeakReference<>(callback).get();
        this.call = new WeakReference<>(call).get();
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case Call.WHAT_PROGRESS:
                if (callback != null)
                    callback.onProgress(call, msg.arg1, msg.arg2);
                break;
            case Call.WHAT_PAUSE:
                if (callback != null)
                    callback.onPause(call, msg.arg1, msg.arg2);
                break;
            case Call.WHAT_COMPLETE:
                if (callback != null)
                    callback.onComplete(call);
                break;
            case Call.WHAT_ERROR:
                if (callback != null) {
                    Throwable obj = (Throwable) msg.obj;
                    callback.onError(call, obj);
                }
                break;
        }
    }
}
