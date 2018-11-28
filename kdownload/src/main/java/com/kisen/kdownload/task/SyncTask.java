package com.kisen.kdownload.task;

import android.os.Message;

import com.kisen.kdownload.exception.KDownloadException;
import com.kisen.kdownload.http.HttpEngine;
import com.kisen.kdownload.manager.HttpManager;
import com.kisen.kdownload.net.Call;
import com.kisen.kdownload.net.CallImpl;

/**
 * description:
 * <p>
 * 优化：
 * 加入线程池。
 * </p>
 * author: Kisenhuang
 * email: Kisenhuang@163.com
 * time: 2018/11/28 下午2:51
 */
public class SyncTask implements Task {

    private CallImpl call;

    public SyncTask(CallImpl call) {
        this.call = call;
    }

    @Override
    public void download() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpEngine httpEngine = HttpManager.getManager().getHttpEngine();
                if (httpEngine == null) {
                    call.changeState(Call.STATE_ERROR);
                    call.callToMain(Message.obtain(null, Call.WHAT_ERROR, new KDownloadException("HttpEngine is null.")));
                } else {
                    httpEngine.download(call);
                }
            }
        }).start();
    }
}
