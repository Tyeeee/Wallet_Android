package com.yjt.wallet.components.http;

import android.text.TextUtils;

import com.yjt.wallet.components.http.model.Parameter;
import com.yjt.wallet.components.utils.LogUtil;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;

import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.OkHttpClient;

public class CustomHttpClient {

    private OkHttpClient okHttpClient;
    private Configuration configuration;

    private static CustomHttpClient mCustomHttpClientUtil;

    private CustomHttpClient() {
        // cannot be instantiated
    }

    public static synchronized CustomHttpClient getInstance() {
        if (mCustomHttpClientUtil == null) {
            mCustomHttpClientUtil = new CustomHttpClient();
        }
        return mCustomHttpClientUtil;
    }

    public static void releaseInstance() {
        if (mCustomHttpClientUtil != null) {
            mCustomHttpClientUtil = null;
        }
    }

    public synchronized void initialize(Configuration configuration) {
        this.configuration = configuration;
        long timeout = configuration.getTimeout();
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                .writeTimeout(timeout, TimeUnit.MILLISECONDS)
                .readTimeout(timeout, TimeUnit.MILLISECONDS);
        if (configuration.getHostnameVerifier() != null) {
            builder.hostnameVerifier(configuration.getHostnameVerifier());
        }

        List<InputStream> certificates = configuration.getCertificates();
        if (certificates != null && certificates.size() > 0) {
            HttpsCertificate httpsCerManager = new HttpsCertificate(builder);
            httpsCerManager.setCertificates(certificates);
        }

        CookieJar cookieJar = configuration.getCookieJar();
        if (cookieJar != null) {
            builder.cookieJar(cookieJar);
        }

        if (configuration.getCache() != null) {
            builder.cache(configuration.getCache());
        }

        if (configuration.getAuthenticator() != null) {
            builder.authenticator(configuration.getAuthenticator());
        }

        if (configuration.getCertificatePinner() != null) {
            builder.certificatePinner(configuration.getCertificatePinner());
        }

        builder.followRedirects(configuration.isFollowRedirect());
        builder.followSslRedirects(configuration.isFollowSSLRedirect());

        if (configuration.getSSLSocketFactory() != null) {
            builder.sslSocketFactory(configuration.getSSLSocketFactory());
        }

        if (configuration.getDispatcher() != null) {
            builder.dispatcher(configuration.getDispatcher());
        }
        builder.retryOnConnectionFailure(configuration.isRetry());
        if (configuration.getNetworkInterceptors() != null) {
            builder.networkInterceptors().addAll(configuration.getNetworkInterceptors());
        }

        if (configuration.getInterceptors() != null) {
            builder.interceptors().addAll(configuration.getInterceptors());
        }

        if (configuration.getProxy() != null) {
            builder.proxy(configuration.getProxy());
        }

        LogUtil.getInstance().print("CustomHttpClient initialize...");
        okHttpClient = builder.build();
    }

    public void updateHeaders(String key, String value) {
        Headers headers = configuration.getHeaders();
        if (headers == null) {
            headers = new Headers.Builder().build();
        }
        configuration.mHeaders = headers.newBuilder().set(key, value).build();
    }

    public void updateParameters(String key, String value) {
        boolean add = false;
        List<Parameter> parameters = configuration.getParameters();
        if (parameters != null) {
            for (Parameter parameter : parameters) {
                if (parameter != null && TextUtils.equals(parameter.getKey(), key)) {
                    parameter.setValue(value);
                    add = true;
                    break;
                }
            }
            if (!add) {
                parameters.add(new Parameter(key, value));
            }
        }
    }

    public OkHttpClient.Builder getHttpClientBuilder() {
        return okHttpClient.newBuilder();
    }

    public Headers getHeaders() {
        return configuration.getHeaders();
    }

    public List<Parameter> getParameters() {
        return configuration.getParameters();
    }

    public List<InputStream> getCertificates() {
        return configuration.getCertificates();
    }

    public HostnameVerifier getHostnameVerifier() {
        return configuration.getHostnameVerifier();
    }

    public long getTimeout() {
        return configuration.getTimeout();
    }
}
