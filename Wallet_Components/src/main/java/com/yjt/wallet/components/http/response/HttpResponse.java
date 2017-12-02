package com.yjt.wallet.components.http.response;

import com.yjt.wallet.components.utils.ReflectUtil;

import java.lang.reflect.Type;

import okhttp3.Headers;
import okhttp3.Response;

public abstract class HttpResponse<T> {

    public Type    type;
    public Headers headers;

    public HttpResponse() {
        type = ReflectUtil.getInstance().getGenericSuperclassType(getClass());
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public abstract void onStart();

    public abstract void onProgress(int progress, long speed, boolean isDone);

    public abstract void onEnd();

    public abstract void onResponse(Response httpResponse, String response, Headers headers);

    public abstract void onResponse(String response, Headers headers);

    public abstract void onSuccess(Headers headers, T t);

    public abstract void onSuccess(T t);

    public abstract void onFailed(int code, String message);
}
