package com.yjt.wallet.constant;

public enum Temp {

    ENCRYPT_KEY("encrypt_key"),
    VERSION_INFO("version_info"),
    LOGIN_INFO("login_info"),
    BASE_INFO("base_info"),
    STORE_INFO("store_info"),
    POS_DEVICE_INFO("pos_device_info"),
    IS_BACKGROUND("is_background"),
    SHARE_URL("share_url"),
    IS_START_ACT_FOR_RESULT("is_start_activity_for_result"),
    CLIENT_ID("client_id"),
    PRINT_STATUS("print_status"),
    SHOW_POS_DEVICE_ACTIVITY("show_pos_device_activity"),
    BOX_STORE("box_store");

    private String content;

    Temp(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

}
