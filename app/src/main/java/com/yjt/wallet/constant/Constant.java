package com.yjt.wallet.constant;

import android.Manifest;

public final class Constant {

    private Constant() {
    }

    public static final String[] PERMISSIONS = {Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.RECORD_AUDIO
            , Manifest.permission.ACCESS_FINE_LOCATION
            , Manifest.permission.ACCESS_COARSE_LOCATION
            , Manifest.permission.CAMERA
            , Manifest.permission.READ_SMS
            , Manifest.permission.CALL_PHONE};

    public final static class RequestCode {
        public static final int DIALOG_PROMPT_SET_PERMISSION = 0x4999;
        public static final int PREMISSION_SETTING = 0x5000;
        public static final int NET_WORK_SETTING = 0x5001;
        public static final int DIALOG_PROMPT_SET_NET_WORK = 0x5002;
        public static final int DIALOG_PROGRESS_WALLET = 0x5003;
    }

    public final static class Profile {
        public static final String REFRESH_LOGIN = "refresh_login";
        public static final String LOGIN_PROFILE = "login_profile";
        public static final String PRINT_TICKET = "print_ticket";
        public static final String KEY_PROFILE = "key_profile";
        public static final String GESTURE_PASSWORD = "gesture_password";
        public static final String GUIDE_VIEW = "guide_view";
        public static final String SAVE_CACHE_DATE = "save_cache";
    }

    public final static class SmsVerificationCode {
        public static final String REGISTER_SMS = "1";
        public static final long MILLIS_IN_FUTURE = 60000;
        public static final long COUNT_DOWN_INTERVAL = 1000;
    }

    public static class Tab {
        public static final int HOME_PAGE = 0;
        public final static int SERVICE = 1;
        public final static int MINE = 2;
    }

    public class Cache {
        public static final String ROOT = "/mergepay";
        public static final String CACHE_ROOT = ROOT + "/cache";
        public static final String PAGE_DATA_CACHE_PATH = CACHE_ROOT + "/page";
        public static final String PAGE_IMAGE_CACHE_PATH = PAGE_DATA_CACHE_PATH + "/image";
        public static final String SERVICE_DATA_CACHE_PATH = CACHE_ROOT + "/service";
        public static final String SERVICE_IMAGE_CACHE_PATH = SERVICE_DATA_CACHE_PATH + "/icon";
        public static final String TAB_DATA_CACHE_PATH = CACHE_ROOT + "/tab";
        public static final String TAB_IMAGE_CACHE_PATH = TAB_DATA_CACHE_PATH + "/icon";
    }

    public static class CustomProgressBarStatus {
        public static final int STATE_DEFAULT = 101;
        public static final int STATE_DOWNLOADING = 102;
        public static final int STATE_DOWNLOADED = 103;
    }

    public static class RecycleView {
        public static final int DATA_EMPTY = 0x7011;
        public static final int DATA_LOADING = 0x7012;
        public static final int DATA_NONE = 0x7013;
        public static final int DATA_ERROR = 0x7014;
        public static final int FOOTER_VIEW_ID = 0x7015;
        public static final int DATA_SIZE = 10;
    }
}
