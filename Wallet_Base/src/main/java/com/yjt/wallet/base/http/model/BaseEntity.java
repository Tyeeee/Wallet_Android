package com.yjt.wallet.base.http.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.JSONObject;
import com.yjt.wallet.base.constant.BaseResponseParameterKey;
import com.yjt.wallet.base.http.model.cache.listener.implement.CacheableImplement;

import java.util.List;

public class BaseEntity extends CacheableImplement implements Parcelable {

    private boolean hasError;
    private String message;
    private List<String> payload;

    public BaseEntity() { }

    public boolean isHasError() {
        return hasError;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getPayload() {
        return payload;
    }

    public BaseEntity parse(JSONObject object) {
        if (object != null) {
            hasError = object.getBoolean(BaseResponseParameterKey.HAS_ERROR);
            message = object.getString(BaseResponseParameterKey.MESSAGE);
            payload = object.getJSONArray(BaseResponseParameterKey.PAY_LOAD).toJavaList(String.class);
        }
        return this;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "hasError='" + hasError + '\'' +
                ", message='" + message + '\'' +
                ", payload='" + payload.toString() +
                '}';
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.hasError ? (byte) 1 : (byte) 0);
        dest.writeString(this.message);
        dest.writeStringList(this.payload);
    }

    protected BaseEntity(Parcel in) {
        this.hasError = in.readByte() != 0;
        this.message = in.readString();
        this.payload = in.createStringArrayList();
    }
}
