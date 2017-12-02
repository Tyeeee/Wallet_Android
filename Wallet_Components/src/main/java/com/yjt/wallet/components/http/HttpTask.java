package com.yjt.wallet.components.http;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yjt.wallet.components.constant.Constant;
import com.yjt.wallet.components.constant.HttpRequestType;
import com.yjt.wallet.components.constant.Regex;
import com.yjt.wallet.components.constant.ResponseCode;
import com.yjt.wallet.components.http.listener.OnUpdateProgressListener;
import com.yjt.wallet.components.http.model.Parameter;
import com.yjt.wallet.components.http.request.ProgressRequestBody;
import com.yjt.wallet.components.http.request.RequestParameter;
import com.yjt.wallet.components.http.response.HttpResponse;
import com.yjt.wallet.components.http.response.ResponseParameter;
import com.yjt.wallet.components.utils.JsonFormatUtil;
import com.yjt.wallet.components.utils.LogUtil;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpTask implements Callback, OnUpdateProgressListener {

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private HttpRequestType httpRequestType;
    private String url;
    private RequestParameter parameter;
    private OkHttpClient okHttpClient;
    private HttpResponse httpResponse;
    private Headers headers;
    private String httpTaskKey;

    public HttpTask(HttpRequestType type, String url, RequestParameter parameter, OkHttpClient.Builder builder, HttpResponse response) {
        this.httpRequestType = type;
        this.url = url;
        if (parameter == null) {
            this.parameter = new RequestParameter();
        } else {
            this.parameter = parameter;
        }
        this.okHttpClient = builder.build();
        this.httpResponse = response;
        this.httpTaskKey = this.parameter.getHttpTaskKey();
        if (TextUtils.isEmpty(httpTaskKey)) {
            httpTaskKey = Constant.HttpTask.DEFAULT_TASK_KEY;
        }
        HttpTaskUtil.getInstance().addTask(httpTaskKey, this);
    }

    public void execute() {
        LogUtil.getInstance().print("execute invoked!!");
        if (parameter.builder != null) {
            headers = parameter.builder.build();
        }
        if (httpResponse != null) {
            httpResponse.onStart();
        }
        enqueue();
    }

    private void enqueue() {
        LogUtil.getInstance().print("enqueue invoked!!");
        String original = url;
        Request.Builder builder = new Request.Builder();
        switch (httpRequestType) {
            case GET:
                url = getCompleteUrl(url, parameter.getParameters(), parameter.isUrlEncode());
                builder.get();
                break;
            case POST:
                RequestBody post = parameter.getRequestBody();
                if (post != null) {
                    builder.post(new ProgressRequestBody(post, this));
                }
                break;
            case PUT:
                RequestBody put = parameter.getRequestBody();
                if (put != null) {
                    builder.put(new ProgressRequestBody(put, this));
                }
                break;
            case DELETE:
                url = getCompleteUrl(url, parameter.getParameters(), parameter.isUrlEncode());
                builder.delete();
                break;
            case HEAD:
                url = getCompleteUrl(url, parameter.getParameters(), parameter.isUrlEncode());
                builder.head();
                break;
            case PATCH:
                RequestBody bodyPatch = parameter.getRequestBody();
                if (bodyPatch != null) {
                    builder.put(new ProgressRequestBody(bodyPatch, this));
                }
                break;
            default:
                break;
        }
        if (parameter.cacheControl != null) {
            builder.cacheControl(parameter.cacheControl);
        }
        builder.url(url).tag(original).headers(headers);
        Request request = builder.build();
        LogUtil.getInstance().print("original:" + original);
        LogUtil.getInstance().print("url:" + url);
        LogUtil.getInstance().print("parameter:" + parameter.toString());
        LogUtil.getInstance().print("header:" + headers.toString());
        Call call = okHttpClient.newCall(request);
        HttpCallUtil.getInstance().addCall(url, call);
        call.enqueue(this);
    }

    @Override
    public void updateProgress(final int progress, final long speed, final boolean isDone) {
        LogUtil.getInstance().print("updateProgress invoked!!");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (httpResponse != null) {
                    httpResponse.onProgress(progress, speed, isDone);
                }
            }
        });
    }

    @Override
    public void onFailure(Call call, IOException exception) {
        LogUtil.getInstance().print("onFailure invoked!!");
        ResponseParameter parameter = new ResponseParameter();
        if (exception instanceof SocketTimeoutException) {
            parameter.setTimeout(true);
        } else if (exception instanceof InterruptedIOException && TextUtils.equals(exception.getMessage(), Constant.HttpTask.TIME_OUT)) {
            parameter.setTimeout(true);
        }
        handleResponse(parameter, null);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        LogUtil.getInstance().print("onResponse invoked!!");
        handleResponse(new ResponseParameter(), response);
    }

    private void handleResponse(final ResponseParameter parameter, Response response) {
        LogUtil.getInstance().print("handleResponse invoked!!");
        if (response != null) {
            try {
                parameter.setNoResponse(false);
                parameter.setResponseCode(response.code());
                parameter.setResponseMessage(response.message());
                parameter.setSuccess(response.isSuccessful());
                parameter.setResponseResult(response.body().string());
                parameter.setHeaders(response.headers());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            parameter.setNoResponse(true);
            parameter.setResponseCode(Integer.parseInt(ResponseCode.RESPONSE_CODE_UNKNOWN.getContent()));
            if (parameter.isTimeout()) {
                parameter.setResponseMessage(ResponseCode.RESPONSE_MESSAGE_TIME_OUT.getContent());
            } else {
                parameter.setResponseMessage(ResponseCode.RESPONSE_MESSAGE_UNKNOWN.getContent());
            }
        }
        parameter.setResponse(response);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onPostExecute(parameter);
            }
        });
    }

    protected void onPostExecute(ResponseParameter parameter) {
        LogUtil.getInstance().print("onPostExecute invoked!!");
        HttpCallUtil.getInstance().removeCall(url);
        if (!HttpTaskUtil.getInstance().contains(httpTaskKey)) {
            return;
        }

        if (httpResponse != null) {
            httpResponse.setHeaders(parameter.getHeaders());
            httpResponse.onResponse(parameter.getResponse(), parameter.getResponseResult(), parameter.getHeaders());
            httpResponse.onResponse(parameter.getResponseResult(), parameter.getHeaders());
        }
        int code = parameter.getResponseCode();
        String messge = parameter.getResponseMessage();
        LogUtil.getInstance().print("code:" + code + ",messge:" + messge + ",parameter:" + parameter.isNoResponse());
        httpResponse.onEnd();
        if (!parameter.isNoResponse()) {
            if (parameter.isSuccess()) {
                LogUtil.getInstance().print("url=" + url + ",result=" + JsonFormatUtil.formatJson(parameter.getResponseResult()) + ",headers=" + parameter.getHeaders().toString());
                parseResponseBody(parameter, httpResponse);
            } else {
                LogUtil.getInstance().print("url=" + url + ",response failure code=" + code + ",messge=" + messge);
                if (httpResponse != null) {
                    httpResponse.onFailed(code, messge);
                }
            }
        } else {
            LogUtil.getInstance().print("url=" + url + "\n response failure code=" + code + " msg=" + messge);
            if (httpResponse != null) {
                httpResponse.onFailed(code, messge);
            }
        }
//        if (httpResponse != null) {
//            httpResponse.onEnd();
//        }
    }

    private void parseResponseBody(ResponseParameter parameter, HttpResponse response) {
//        LogUtil.getInstance().print("parseResponseBody invoked!!");
        if (response == null) {
            return;
        }
        String result = parameter.getResponseResult();
        LogUtil.getInstance().print("result:" + result + ",mediaType:" + response.type);
        try {
            if (response.type == String.class) {
                response.onSuccess(parameter.getHeaders(), result);
                response.onSuccess(result);
                return;
            } else if (response.type == JSONObject.class) {
                JSONObject object = JSON.parseObject(result);
                if (object != null) {
                    response.onSuccess(parameter.getHeaders(), object);
                    response.onSuccess(object);
                    return;
                }
            } else if (httpResponse.type == JSONArray.class) {
                JSONArray array = JSON.parseArray(result);
                if (array != null) {
                    response.onSuccess(parameter.getHeaders(), array);
                    response.onSuccess(array);
                    return;
                }
            } else {
                Object object = JSON.parseObject(result, httpResponse.type);
                if (object != null) {
                    response.onSuccess(parameter.getHeaders(), object);
                    response.onSuccess(object);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.onFailed(Integer.parseInt(ResponseCode.RESPONSE_CODE_DATA_PARSE_ERROR.getContent()), ResponseCode.RESPONSE_MESSAGE_DATA_PARSE_ERROR.getContent());
    }

    private String getCompleteUrl(String url, List<Parameter> parameters, boolean isUrlEncode) {
        StringBuilder builder = new StringBuilder();
        builder.append(url);
        if (builder.indexOf(Regex.QUESTION_MARK.getRegext(), 0) < 0 && parameters.size() > 0) {
            builder.append(Regex.QUESTION_MARK.getRegext());
        }
        int flag = 0;
        for (Parameter parameter : parameters) {
            String key = parameter.getKey();
            String value = parameter.getValue();
            if (isUrlEncode) {
                try {
                    key = URLEncoder.encode(key, Regex.UTF_8.getRegext());
                    value = URLEncoder.encode(value, Regex.UTF_8.getRegext());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            builder.append(key).append(Regex.EQUALS.getRegext()).append(value);
            if (++flag != parameters.size()) {
                builder.append(Regex.AND.getRegext());
            }
        }
        return builder.toString();
    }
}
