package com.yjt.wallet.components.http;


import android.text.TextUtils;

import com.yjt.wallet.components.constant.Regex;
import com.yjt.wallet.components.http.listener.implement.CustomX509TrustManager;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.List;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class HttpsCertificate {

    private OkHttpClient.Builder mBuilder;

    public HttpsCertificate(OkHttpClient.Builder builder) {
        this.mBuilder = builder;
    }

    public void setCertificates(List<InputStream> certificates) {
        setCertificates(certificates.toArray(new InputStream[]{}), null, null);
    }

    private void setCertificates(InputStream[] certificates, InputStream bksFile, String password) {
        try {
            TrustManager[] trustManagers = prepareTrustManager(certificates);
            KeyManager[] keyManagers = prepareKeyManager(bksFile, password);
            SSLContext sslContext = SSLContext.getInstance(Regex.TLS.getRegext());
            sslContext.init(keyManagers, new TrustManager[]{new CustomX509TrustManager(chooseTrustManager(trustManagers))}, new SecureRandom());
            mBuilder.sslSocketFactory(sslContext.getSocketFactory());
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    private TrustManager[] prepareTrustManager(InputStream... certificates) {
        if (certificates != null && certificates.length > 0) {
            try {
                CertificateFactory certificateFactory = CertificateFactory.getInstance(Regex.X509.getRegext());
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null);
                int index = 0;
                for (InputStream certificate : certificates) {
                    keyStore.setCertificateEntry(Integer.toString(index++), certificateFactory.generateCertificate(certificate));
                    if (certificate != null) {
                        certificate.close();
                    }
                }
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(keyStore);
                return trustManagerFactory.getTrustManagers();
            } catch (CertificateException | NoSuchAlgorithmException | IOException | KeyStoreException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private KeyManager[] prepareKeyManager(InputStream certificate, String password) {
        if (certificate != null || !TextUtils.isEmpty(password)) {
            try {
                KeyStore keyStore = KeyStore.getInstance(Regex.BKS.getRegext());
                keyStore.load(certificate, password.toCharArray());
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(keyStore, password.toCharArray());
                return keyManagerFactory.getKeyManagers();
            } catch (KeyStoreException | IOException | NoSuchAlgorithmException | UnrecoverableKeyException | CertificateException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        return null;
    }

}
