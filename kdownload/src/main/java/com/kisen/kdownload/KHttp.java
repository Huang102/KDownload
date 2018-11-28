package com.kisen.kdownload;

import com.kisen.kdownload.util.KLog;

/**
 * Created by huangwy on 2017/12/26.
 * email: kisenhuang@163.com.
 * 设置文件本地状态存储。
 * 下载完成文件读取。
 */

public class KHttp {

    public static void configLog(boolean open, String tag){
        KLog.configLog(open, tag);
    }

    public static void configLog(boolean open){
        KLog.configLog(open);
    }

    public static void setupSSL(){

    }

}
