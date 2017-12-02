package com.yjt.wallet.components.http.response;

import okhttp3.Headers;
import okhttp3.Response;

public class ResponseParameter {

    private boolean isNoResponse;
    private boolean isTimeout;
    private int responseCode;
    private String responseMessage;
    private String responseResult;
    private boolean isSuccess;
    private Headers headers;
    private Response response;

    public boolean isNoResponse() {
        return isNoResponse;
    }

    public void setNoResponse(boolean noResponse) {
        isNoResponse = noResponse;
    }

    public boolean isTimeout() {
        return isTimeout;
    }

    public void setTimeout(boolean timeout) {
        isTimeout = timeout;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseResult() {
        return responseResult;
    }

    public void setResponseResult(String responseResult) {
        this.responseResult = responseResult;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
