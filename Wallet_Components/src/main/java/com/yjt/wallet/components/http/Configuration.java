package com.yjt.wallet.components.http;

import android.text.TextUtils;

import com.google.common.collect.Lists;
import com.yjt.wallet.components.constant.Constant;
import com.yjt.wallet.components.constant.Regex;
import com.yjt.wallet.components.http.model.Parameter;

import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.CertificatePinner;
import okhttp3.CookieJar;
import okhttp3.Dispatcher;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Response;
import okio.Buffer;

public class Configuration {

    private List<Parameter> mParameters;
    protected Headers mHeaders;
    private List<InputStream> mCertificates;
    private HostnameVerifier mHostnameVerifier;
    private long mTimeout = Constant.HttpTask.REQUEST_TIME_OUT_PERIOD;
    private boolean isDebug;
    private CookieJar mCookieJar;
    private Cache mCache;
    private Authenticator mAuthenticator;
    private CertificatePinner mCertificatePinner;
    private boolean isFollowSSLRedirect;
    private boolean isFollowRedirect;
    private boolean isRetry;
    private Proxy mProxy;
    private List<Interceptor> mNetworkInterceptors;
    private List<Interceptor> mInterceptors;
    private SSLSocketFactory mSSLSocketFactory;
    private Dispatcher mDispatcher;

    private Configuration(Builder builder) {
        this.mParameters = builder.mParameters;
        this.mHeaders = builder.mHeaders;
        this.mCertificates = builder.mCertificates;
        this.mHostnameVerifier = builder.mHostnameVerifier;
        this.mTimeout = builder.mTimeout;
        this.isDebug = builder.isDebug;
        this.mCookieJar = builder.mCookieJar;
        this.mCache = builder.mCache;
        this.mAuthenticator = builder.mAuthenticator;
        this.mCertificatePinner = builder.mCertificatePinner;
        this.isFollowSSLRedirect = builder.isFollowSSLRedirect;
        this.isFollowRedirect = builder.isFollowRedirect;
        this.isRetry = builder.isRetry;
        this.mProxy = builder.mProxy;
        this.mNetworkInterceptors = builder.mNetworkInterceptors;
        this.mInterceptors = builder.mInterceptors;
        this.mSSLSocketFactory = builder.mSSLSocketFactory;
        this.mDispatcher = builder.mDispatcher;
    }

    public static class Builder {

        private List<Parameter> mParameters;
        protected Headers mHeaders;
        private List<InputStream> mCertificates;
        private HostnameVerifier mHostnameVerifier;
        private long mTimeout;
        private boolean isDebug;
        private CookieJar mCookieJar = CookieJar.NO_COOKIES;
        private Cache mCache;
        private Authenticator mAuthenticator;
        private CertificatePinner mCertificatePinner;
        private boolean isFollowSSLRedirect;
        private boolean isFollowRedirect;
        private boolean isRetry;
        private Proxy mProxy;
        private List<Interceptor> mNetworkInterceptors;
        private List<Interceptor> mInterceptors;
        private SSLSocketFactory mSSLSocketFactory;
        private Dispatcher mDispatcher;

        public Builder() {
            mCertificates = Lists.newArrayList();
            isFollowSSLRedirect = true;
            isFollowRedirect = true;
            isRetry = true;
            mNetworkInterceptors = Lists.newArrayList();
        }

        public Builder setParameters(List<Parameter> parameters) {
            this.mParameters = parameters;
            return this;
        }

        public Builder setHeaders(Headers headers) {
            this.mHeaders = headers;
            return this;
        }

        public Builder setCertificates(List<InputStream> certificates) {
            for (InputStream inputStream : certificates) {
                if (inputStream != null) {
                    mCertificates.add(inputStream);
                }
            }
            return this;
        }

        public Builder setCertificates(String... certificates) {
            for (String certificate : certificates) {
                if (!TextUtils.isEmpty(certificate)) {
                    mCertificates.add(new Buffer()
                                              .writeUtf8(certificate)
                                              .inputStream());
                }
            }
            return this;
        }

        public Builder setHostnameVerifier(HostnameVerifier hostnameVerifier) {
            this.mHostnameVerifier = hostnameVerifier;
            return this;
        }

        public Builder setTimeout(long timeout) {
            this.mTimeout = timeout;
            return this;
        }

        public Builder setDebug(boolean debug) {
            isDebug = debug;
            return this;
        }

        public Builder setCookieJar(CookieJar cookieJar) {
            this.mCookieJar = cookieJar;
            return this;
        }

        public void setCache(Cache cache) {
            this.mCache = cache;
        }

        public Builder setCache(Cache cache, final int cacheTime) {
            setCache(cache, String.format(Regex.MAX_STALE.getRegext(), cacheTime));
            return this;
        }

        public Builder setCache(Cache cache, final String value) {
            Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    return chain.proceed(chain.request()).newBuilder()
                            .removeHeader(Regex.PRAGMA.getRegext())
                            .header(Regex.CACHE_CONTROL.getRegext(), value)
                            .build();
                }
            };
            mNetworkInterceptors.add(REWRITE_CACHE_CONTROL_INTERCEPTOR);
            this.mCache = cache;
            return this;
        }

        public Configuration build() {
            return new Configuration(this);
        }

        public Builder setAuthenticator(Authenticator authenticator) {
            this.mAuthenticator = authenticator;
            return this;
        }

        public Builder setCertificatePinner(CertificatePinner certificatePinner) {
            this.mCertificatePinner = certificatePinner;
            return this;
        }

        public Builder setFollowSSLRedirect(boolean followSslRedirect) {
            isFollowSSLRedirect = followSslRedirect;
            return this;
        }

        public Builder setFollowRedirect(boolean followRedirect) {
            isFollowRedirect = followRedirect;
            return this;
        }

        public Builder setRetry(boolean retry) {
            isRetry = retry;
            return this;
        }

        public Builder setProxy(Proxy proxy) {
            this.mProxy = proxy;
            return this;
        }

        public Builder setmNetworkInterceptors(List<Interceptor> networkInterceptors) {
            this.mNetworkInterceptors = networkInterceptors;
            return this;
        }

        public Builder setInterceptors(List<Interceptor> interceptors) {
            this.mInterceptors = interceptors;
            return this;
        }

        public Builder setSSLSocketFactory(SSLSocketFactory sSLSocketFactory) {
            this.mSSLSocketFactory = sSLSocketFactory;
            return this;
        }

        public Builder setDispatcher(Dispatcher mDispatcher) {
            this.mDispatcher = mDispatcher;
            return this;
        }
    }

    public List<Parameter> getParameters() {
        return mParameters;
    }

    public Headers getHeaders() {
        return mHeaders;
    }

    public List<InputStream> getCertificates() {
        return mCertificates;
    }

    public HostnameVerifier getHostnameVerifier() {
        return mHostnameVerifier;
    }

    public long getTimeout() {
        return mTimeout;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public CookieJar getCookieJar() {
        return mCookieJar;
    }

    public Cache getCache() {
        return mCache;
    }

    public Authenticator getAuthenticator() {
        return mAuthenticator;
    }

    public CertificatePinner getCertificatePinner() {
        return mCertificatePinner;
    }

    public boolean isFollowSSLRedirect() {
        return isFollowSSLRedirect;
    }

    public boolean isFollowRedirect() {
        return isFollowRedirect;
    }

    public boolean isRetry() {
        return isRetry;
    }

    public Proxy getProxy() {
        return mProxy;
    }

    public List<Interceptor> getNetworkInterceptors() {
        return mNetworkInterceptors;
    }

    public List<Interceptor> getInterceptors() {
        return mInterceptors;
    }

    public SSLSocketFactory getSSLSocketFactory() {
        return mSSLSocketFactory;
    }

    public Dispatcher getDispatcher() {
        return mDispatcher;
    }
}
