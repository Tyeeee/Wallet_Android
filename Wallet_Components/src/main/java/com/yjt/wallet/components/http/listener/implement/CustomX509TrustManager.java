package com.yjt.wallet.components.http.listener.implement;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class CustomX509TrustManager implements X509TrustManager {

    private X509TrustManager defaultTrustManager;
    private X509TrustManager localTrustManager;

    public CustomX509TrustManager(X509TrustManager localTrustManager) throws NoSuchAlgorithmException, KeyStoreException {
        TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        factory.init((KeyStore) null);
        defaultTrustManager = chooseTrustManager(factory.getTrustManagers());
        this.localTrustManager = localTrustManager;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String authType) throws CertificateException {
        try {
            defaultTrustManager.checkServerTrusted(x509Certificates, authType);
        } catch (CertificateException ce) {
            localTrustManager.checkServerTrusted(x509Certificates, authType);
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    private X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
        for (TrustManager manager : trustManagers) {
            if (manager instanceof X509TrustManager) {
                return (X509TrustManager) manager;
            }
        }
        return null;
    }
}
