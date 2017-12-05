package com.yjt.wallet.base.http.response;

import com.alibaba.fastjson.JSONObject;
import com.yjt.wallet.base.http.model.BaseEntity;
import com.yjt.wallet.components.http.response.HttpResponse;

public abstract class JSONObjectResponse extends HttpResponse<JSONObject> {

    private BaseEntity baseEntity;

    public JSONObjectResponse() {
        super();
        type = JSONObject.class;
        baseEntity = new BaseEntity();
    }

    @Override
    public void onSuccess(JSONObject object) {
        baseEntity.parse(object);
        if (baseEntity.isHasError()) {
            onResponseSuccess(object);
        } else {
//            onResponseFailed(baseEntity.getReturnCode(), baseEntity.getReturnMessage());
        }
//        switch (baseEntity.getReturnCode()) {
//            case ResponseCode.SUCCESS:
//                onResponseSuccess(object);
//                break;
//            case ResponseCode.FAILED:
//                if (!TextUtils.isEmpty(baseEntity.getErrorCode())) {
//                    onResponseFailed(baseEntity.getErrorCode(), TextUtils.isEmpty(baseEntity.getErrorMessage()) ? baseEntity.getReturnMessage() : baseEntity.getErrorMessage(), object);
//                } else if (!TextUtils.isEmpty(baseEntity.getReturnCode())) {
//                    onResponseFailed(baseEntity.getReturnCode(), baseEntity.getReturnMessage());
//                } else {
//                    onResponseFailed(ResponseCode.ERROR_CODE_UNKNOWN, "未知错误");
//                }
//                break;
//            default:
//                break;
//        }
    }

    public abstract void onParseData(JSONObject object);

    public abstract void onResponseSuccess(JSONObject object);

    public abstract void onResponseFailed(String code, String message);

    public abstract void onResponseFailed(String code, String message, JSONObject object);
}
