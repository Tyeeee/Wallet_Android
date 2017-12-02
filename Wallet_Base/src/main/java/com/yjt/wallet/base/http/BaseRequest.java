package com.yjt.wallet.base.http;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.yjt.wallet.base.application.BaseApplication;
import com.yjt.wallet.base.constant.BaseRequestParameterKey;
import com.yjt.wallet.components.http.request.RequestParameter;
import com.yjt.wallet.components.utils.LogUtil;
import com.yjt.wallet.components.utils.SecurityUtil;

public class BaseRequest {

    protected BaseRequest() {
        // cannot be instantiated
    }

    protected JSONObject generateRequestParameters(String method, String bizContent, String mchUid, String clientId, String deviceType, String clientInfo, String clientVersion, String timeStamp) {
        LogUtil.getInstance().print(bizContent);
        JSONObject jsonObject = new JSONObject();
        if (!TextUtils.isEmpty(bizContent)) {
            jsonObject.put(BaseRequestParameterKey.BIZ_CONTENT, String.valueOf(bizContent));
        }
        if (!TextUtils.isEmpty(clientId)) {
            jsonObject.put(BaseRequestParameterKey.CLIENT_ID, String.valueOf(clientId));
        }
        if (!TextUtils.isEmpty(clientInfo)) {
            jsonObject.put(BaseRequestParameterKey.CLIENT_INFO, String.valueOf(clientInfo));
        }
        if (!TextUtils.isEmpty(clientVersion)) {
            jsonObject.put(BaseRequestParameterKey.CLIENT_VER, String.valueOf(clientVersion));
        }
        if (!TextUtils.isEmpty(deviceType)) {
            jsonObject.put(BaseRequestParameterKey.CLIENT_DEVICE_TYPE, String.valueOf(deviceType));
        }
        if (!TextUtils.isEmpty(mchUid)) {
            jsonObject.put(BaseRequestParameterKey.MCH_UID, String.valueOf(mchUid));
        }
        if (!TextUtils.isEmpty(method)) {
            jsonObject.put(BaseRequestParameterKey.METHOD, String.valueOf(method));
        }
        if (!TextUtils.isEmpty(timeStamp)) {
            jsonObject.put(BaseRequestParameterKey.TIME_STAMP, String.valueOf(timeStamp));
        }
        return jsonObject;
    }

    protected RequestParameter generateRequestParameters(String method, String bizContent, boolean isEncrypt, String mchUid, String clientId, String deviceType, String clientInfo, String clientVersion, String timeStamp, boolean isJson) {
        return formatParameters(generateRequestParameters(method, bizContent, mchUid, clientId, deviceType, clientInfo, clientVersion, timeStamp), isEncrypt, isJson);
    }

    private RequestParameter formatParameters(JSONObject jsonObject, boolean isEncrypt, boolean isJson) {
        RequestParameter parameter = new RequestParameter();
        parameter.setJsonType(isJson);
        LogUtil.getInstance().print("签名加密前：" + jsonObject.toString());
        String requestData = SecurityUtil.encryptAES(jsonObject.toString(), BaseApplication.getInstance().getEncryptKey(), isEncrypt);
        LogUtil.getInstance().print("签名加密后：" + requestData);
        if (!TextUtils.isEmpty(requestData)) {
            jsonObject = JSONObject.parseObject(requestData);
            for (String key : jsonObject.keySet()) {
                String value = jsonObject.getString(key);
                if (!TextUtils.isEmpty(value)) {
                    parameter.addFormDataParameter(key, value);
                    LogUtil.getInstance().print("key:" + key + ",value:" + value);
                }
            }
            return parameter;
        } else {
            return null;
        }
    }

    protected String generateRequestString(String method, String bizContent, boolean isEncrypt, String mchUid, String clientId, String deviceType, String clientInfo, String clientVersion, String timeStamp) {
        return formatParameters(generateRequestParameters(method, bizContent, mchUid, clientId, deviceType, clientInfo, clientVersion, timeStamp), isEncrypt);
    }

    private String formatParameters(JSONObject jsonObject, boolean isEncrypt) {
        LogUtil.getInstance().print("签名加密前：" + jsonObject.toString());
        String requestData = SecurityUtil.encryptAES(jsonObject.toString(), BaseApplication.getInstance().getEncryptKey(), isEncrypt);
        LogUtil.getInstance().print("签名加密后：" + requestData);
        if (!TextUtils.isEmpty(requestData)) {
            return requestData;
        } else {
            return null;
        }
    }
}
