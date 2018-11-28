package com.kisen.kdownload.manager;

import com.kisen.kdownload.http.HttpEngine;
import com.kisen.kdownload.http.IHttpStrategy;

/**
 * description: Http工具管理类
 * author: Kisenhuang
 * email: Kisenhuang@163.com
 * time: 2018/11/27 下午5:48
 */
public class HttpManager {

    private HttpEngine httpEngine;

    private static HttpManager manager;

    private HttpManager() {
    }

    public static HttpManager getManager() {
        if (manager == null) {
            synchronized (HttpManager.class) {
                if (manager == null) {
                    manager = new HttpManager();
                }
            }
        }
        return manager;
    }

    public void setHttpStrategy(IHttpStrategy strategy) {
        httpEngine = new HttpEngine(strategy);
    }

    public HttpEngine getHttpEngine() {
        return httpEngine;
    }


}
