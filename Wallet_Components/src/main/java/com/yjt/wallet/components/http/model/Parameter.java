package com.yjt.wallet.components.http.model;

import android.text.TextUtils;

import com.yjt.wallet.components.constant.Regex;

public final class Parameter {

    private String key;
    private String value;
    private FileWrapper fileWrapper;

    public Parameter(String key, String value) {
        setKey(key);
        setValue(value);
    }

    public Parameter(String key, FileWrapper fileWrapper) {
        setKey(key);
        this.fileWrapper = fileWrapper;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public FileWrapper getFileWrapper() {
        return fileWrapper;
    }

    public void setKey(String key) {
        if (TextUtils.isEmpty(key)) {
            this.key = Regex.NONE.getRegext();
        } else {
            this.key = key;
        }
    }

    public void setValue(String value) {
        if (TextUtils.isEmpty(value)) {
            this.value = Regex.NONE.getRegext();
        } else {
            this.value = value;
        }
    }

    public void setFile(FileWrapper fileWrapper) {
        this.fileWrapper = fileWrapper;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof Parameter)) {
            Parameter parameter = (Parameter) obj;
            if (TextUtils.equals(parameter.getKey(), getKey()) && TextUtils.equals(parameter.getValue(), getValue())) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }
}
