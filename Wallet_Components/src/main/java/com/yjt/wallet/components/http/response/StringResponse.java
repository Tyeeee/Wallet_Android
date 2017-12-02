package com.yjt.wallet.components.http.response;

import okhttp3.Headers;
import okhttp3.Response;

public class StringResponse extends HttpResponse<String> {

    public StringResponse() {
        super();
        type = String.class;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onProgress(int progress, long speed, boolean isDone) {

    }

    @Override
    public void onEnd() {

    }

    @Override
    public void onResponse(Response httpResponse, String response, Headers headers) {

    }

    @Override
    public void onResponse(String response, Headers headers) {

    }

    @Override
    public void onSuccess(Headers headers, String s) {

    }

    @Override
    public void onSuccess(String s) {

    }

    @Override
    public void onFailed(int code, String message) {

    }
}
