package com.yjt.wallet.components.http.response;

import com.alibaba.fastjson.JSONArray;

import okhttp3.Headers;
import okhttp3.Response;

public class JSONArrayResponse extends HttpResponse<JSONArray> {

    public JSONArrayResponse() {
        super();
        type = JSONArray.class;
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
    public void onSuccess(Headers headers, JSONArray objects) {

    }

    @Override
    public void onSuccess(JSONArray objects) {

    }

    @Override
    public void onFailed(int code, String message) {

    }
}
