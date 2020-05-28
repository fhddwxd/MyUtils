package com.wxd.myutils.Utils;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 绕过证书的工具类
 *
 * @author Fansd(fansd @ hadlinks.com)
 */
public class miTM implements TrustManager,X509TrustManager {
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    public boolean isServerTrusted(X509Certificate[] certs) {
        return true;
    }

    public boolean isClientTrusted(X509Certificate[] certs) {
        return true;
    }

    public void checkServerTrusted(X509Certificate[] certs, String authType)
            throws CertificateException {
    }

    public void checkClientTrusted(X509Certificate[] certs, String authType)
            throws CertificateException {
    }

    public static SocketFactory getFactory() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[1];
        TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        return sc.getSocketFactory();
    }
}
