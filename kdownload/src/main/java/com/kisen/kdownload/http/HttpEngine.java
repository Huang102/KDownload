package com.kisen.kdownload.http;

import com.kisen.kdownload.net.Call;
import com.kisen.kdownload.net.CallImpl;

/**
 * description: http策略助手类
 * author: Kisenhuang
 * email: Kisenhuang@163.com
 * time: 2018/11/27 下午5:33
 */
public class HttpEngine {

    private IHttpStrategy mHttpStrategy;

    public HttpEngine(IHttpStrategy strategy){
        mHttpStrategy = strategy;
    }

    public void download(CallImpl call){
        mHttpStrategy.getFileInfo(call);
        if (call.getDownloadState() == Call.STATE_READY) {
            mHttpStrategy.download(call);
        }
    }

}
