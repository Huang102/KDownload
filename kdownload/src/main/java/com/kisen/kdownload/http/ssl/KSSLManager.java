package com.kisen.kdownload.http.ssl;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * description:
 * author: Kisenhuang
 * email: Kisenhuang@163.com
 * time: 2018/11/28 下午3:20
 */
public class KSSLManager {

    public static X509TrustManager createTrustManager() {
        X509TrustManager trustAllCerts = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };

        return trustAllCerts;
    }


    public static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{createTrustManager()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
            ssfFactory = new Tls122SocketFactory(ssfFactory);

        } catch (Exception e) {
        }

        return ssfFactory;
    }
}
