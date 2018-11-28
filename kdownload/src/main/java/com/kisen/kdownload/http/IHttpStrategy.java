package com.kisen.kdownload.http;

import com.kisen.kdownload.net.CallImpl;

/**
 * description:Http策略接口
 * author: Kisenhuang
 * email: Kisenhuang@163.com
 * time: 2018/11/27 下午5:34
 */
public interface IHttpStrategy {

    void download(CallImpl call);

    void getFileInfo(CallImpl call);

}
