package com.kisen.kdownload.exception;

/**
 * description:
 * author: Kisenhuang
 * email: Kisenhuang@163.com
 * time: 2018/11/28 下午3:14
 */
public class KDownloadException extends RuntimeException {
    public KDownloadException(String message) {
        super(message);
    }

    public KDownloadException(Throwable cause) {
        super(cause);
    }
}
