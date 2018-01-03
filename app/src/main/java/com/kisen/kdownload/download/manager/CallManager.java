package com.kisen.kdownload.download.manager;

import android.content.Context;

import com.kisen.kdownload.download.callback.FileCallback;
import com.kisen.kdownload.download.net.Call;
import com.kisen.kdownload.download.net.CallImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Call 缓存类
 * Created by huangwy on 2017/12/28.
 * email: kisenhuang@163.com.
 */

public class CallManager {

    private static Map<String, Call> callMap;

    public static Call map(String url, Context context, String folderPath, FileCallback callback) {
        if (callMap == null)
            callMap = new HashMap<>();
        if (!callMap.containsKey(url)) {
            Call call = CallImpl.newCall(context, url, folderPath, callback);
            callMap.put(url, call);
            return call;
        } else {
            Call call = callMap.get(url);
            if (call instanceof CallImpl)
                ((CallImpl) call).bind(context, callback);
            return call;
        }
    }

}
