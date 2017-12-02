package com.yjt.wallet.base.constant;

public enum Temp {

    REQUEST_CODE("request_code"),
    CANCELABLE_ON_TOUCH_OUTSIDE("cancelable_on_touch_outside"),
    USE_LIGHT_THEME("use_light_theme"),
    USE_DARK_THEME("use_dark_theme"),
    DIALOG_TITLE("dialog_title"),
    DIALOG_PROGRESS_TITLE_IMAGE("dialog_progress_title_image"),
    DIALOG_PROGRESS_IMAGE("dialog_progress_image"),
    DIALOG_PROGRESS_IMAGE_IS_SHOW("dialog_progress_image_is_show"),
    DIALOG_PROMPT_IMAGE("dialog_prompt_image"),
    DIALOG_PROMPT("dialog_prompt"),
    DIALOG_DOWNLOAD_URL("dialog_download_url"),
    DIALOG_DOWNLOAD_FILE("dialog_download_file"),
    DIALOG_BUTTON_POSITIVE("dialog_button_positive"),
    DIALOG_BUTTON_NEGATIVE("dialog_button_negative"),
    DIALOG_BUTTON_NEUTRAL("dialog_button_neutral"),
    DIALOG_LIST("dialog_list"),

    CANCEL_BUTTON_TITLE("cancel_button_title"),
    MENU_ITEM_TITLES("menu_item_titles"),
    MENU_ITEM_IDS("menu_item_ids"),
    IS_CANCELABLE_ONTOUCH_OUTSIDE("is_cancelable_ontouch_outside"),
    HAS_DISMISSED("has_dismissed"),

    DIALOG_CHOICE_ITEMS("dialog_choice_items"),
    DIALOG_CHOICE_ITEM("dialog_choice_item"),
    DIALOG_CHOICE_MODE("dialog_choice_mode"),
    DIALOG_ITEMS("dialog_items"),
    DIALOG_ITEM("dialog_item"),
    TIME_ZONE("time_zone"),
    DATE("date"),
    MINIMUM_NUMBER("minimum_number"),
    MAXIMUM_NUMBER("maximum_number"),

    PERMISSIONS("permissions"),
    ORDER_INFO("order_info");

    private String mContent;

    Temp(String content) {
        this.mContent = content;
    }

    public String getContent() {
        return mContent;
    }

}
