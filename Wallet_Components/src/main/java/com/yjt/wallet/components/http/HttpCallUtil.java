package com.yjt.wallet.components.http;

import android.text.TextUtils;

import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;

public class HttpCallUtil {

    private static HttpCallUtil httpCallUtil;
    private ConcurrentHashMap<String, Call> calls;

    private HttpCallUtil() {
        calls = new ConcurrentHashMap<>();
    }

    public static synchronized HttpCallUtil getInstance() {
        if (httpCallUtil == null) {
            httpCallUtil = new HttpCallUtil();
        }
        return httpCallUtil;
    }

    public static void releaseInstance() {
        if (httpCallUtil != null) {
            httpCallUtil = null;
        }
    }

    public void addCall(String url, Call call) {
        if (call != null && !TextUtils.isEmpty(url)) {
            calls.put(url, call);
        }
    }

    public Call getCall(String url) {
        if (!TextUtils.isEmpty(url)) {
            return calls.get(url);
        }

        return null;
    }

    public void removeCall(String url) {
        if (!TextUtils.isEmpty(url)) {
            calls.remove(url);
        }
    }
}
