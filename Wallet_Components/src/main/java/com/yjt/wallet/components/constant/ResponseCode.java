package com.yjt.wallet.components.constant;

/**
 * 后台错误码类型
 *
 * @author yjt
 */
public enum ResponseCode {

    RESPONSE_CODE_DATA_PARSE_ERROR("9998"),
    RESPONSE_MESSAGE_DATA_PARSE_ERROR("数据解析异常"),
    RESPONSE_CODE_UNKNOWN("9999"),
    RESPONSE_MESSAGE_UNKNOWN("服务器繁忙"),
    RESPONSE_MESSAGE_TIME_OUT("服务器连接超时");

    private String mContent;

    ResponseCode(String content) {
        this.mContent = content;
    }

    public String getContent() {
        return mContent;
    }

}
