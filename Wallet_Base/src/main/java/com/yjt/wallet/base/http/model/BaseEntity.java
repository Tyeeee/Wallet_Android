package com.yjt.wallet.base.http.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.JSONObject;
import com.yjt.wallet.base.constant.BaseResponseParameterKey;
import com.yjt.wallet.base.http.model.cache.listener.implement.CacheableImplement;

public class BaseEntity extends CacheableImplement implements Parcelable {

    private String returnCode;
    private String returnMessage;
    private String errorCode;
    private String errorMessage;
    private String resultData;

    public BaseEntity() { }

    public String getReturnCode() {
        return returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public BaseEntity parse(JSONObject object) {
        if (object != null) {
            returnCode = object.getString(BaseResponseParameterKey.RETURN_CODE);
            returnMessage = object.getString(BaseResponseParameterKey.RETURN_MSG);
            errorCode = object.getString(BaseResponseParameterKey.ERROR_CODE);
            errorMessage = object.getString(BaseResponseParameterKey.ERROR_MSG);
            resultData = object.getString(BaseResponseParameterKey.RESULT_DATA);
        }
        return this;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "returnCode='" + returnCode + '\'' +
                ", returnMessage='" + returnMessage + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", resultData='" + resultData + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.returnCode);
        dest.writeString(this.returnMessage);
        dest.writeString(this.errorCode);
        dest.writeString(this.errorMessage);
        dest.writeString(this.resultData);
    }

    protected BaseEntity(Parcel in) {
        this.returnCode = in.readString();
        this.returnMessage = in.readString();
        this.errorCode = in.readString();
        this.errorMessage = in.readString();
        this.resultData = in.readString();
    }

}
