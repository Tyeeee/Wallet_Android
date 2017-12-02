package com.yjt.wallet.components.constant;

public final class Constant {

    private Constant() {}

    public final static class Validation {
        public static final String BANK_CARD_FORMAT = "bank_card";
        public static final String PHONE_NUMBER_FORMAT = "phone_number";
        public static final String ID_CARD_FORMAT = "id_card";
    }

    public final static class View {
        public static final int DEFAULT_INTERPOLATOR_TYPE = -1;
        public static final int DRAWABLE_TOP = 0x1001;
        public static final int DRAWABLE_LEFT = 0x1002;
        public static final int DRAWABLE_RIGHT = 0x1003;
        public static final int DRAWABLE_BOTTOM = 0x1004;
        public static final int GLIDE_CENTER_CROP = 0x1005;
        public static final int GLIDE_FIT_CENTER = 0x1006;
        public static final int GLIDE_BITMAP = 0x1007;
        public static final int GLIDE_GIF = 0x1008;
        public static final long CLICK_PERIOD = -500;
        public static final int DEFAULT_COLOR = 0x9999;
        public static final int DEFAULT_SIZE = 0x9999;
        public static final int DEFAULT_RESOURCE = 0x9999;

    }

    public final static class Device {
        public final static String DEVICE_NAME = "phoneName";
        public final static String DEVICE_VERSION = "systemVersion";
        public final static String DEVICE_VERSION_NAME = "systemName";
//        public final static String DEVICE_TYPE = "phoneModel";
//        public final static String DEVICE_ID = "phoneID";
//        public final static String SUBSCRIBER_ID = "carrierName";
//        public final static String DEVICE_IP = "ipAddress";
//        public final static String DEVICE_CORE = "countCores";

        public final static String DEVICE_TYPE = "device_type";
        public final static String DEVICE_ID = "uid";
        public final static String APP_VER = "client_ver";
        public final static String OS_VERSION = "os_ver";
        public final static String OS_TYPE = "os_type";
    }

    public static class HttpTask {
        public static final int REQUEST_TIME_OUT_PERIOD = 40000;
        public static final String DEFAULT_TASK_KEY = "default_key";
        public static final String TIME_OUT = "timeout";
    }

    public static class Extra {
        public static final int DEFAULT_VALUE = 0x9999;
    }

    public final static class Data {
        public final static String KEY = "39EB339F80B715384793F7EF";
        public final static String FROMAT = "ToHex16";
        public static final String ALGORITHM_CBC = "DES/ECB/NoPadding";// "DES/CBC/NoPadding";
        public static final String ALGORITHM_ECB = "DES/ECB/NoPadding";
        public static final String ALGORITHM0 = "DES";
        public static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        public static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        public static final byte[] TABLE = {
                (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E',
                (byte) 'F', (byte) 'G', (byte) 'H', (byte) 'I', (byte) 'J',
                (byte) 'K', (byte) 'L', (byte) 'M', (byte) 'N', (byte) 'O',
                (byte) 'P', (byte) 'Q', (byte) 'R', (byte) 'S', (byte) 'T',
                (byte) 'U', (byte) 'V', (byte) 'W', (byte) 'X', (byte) 'Y',
                (byte) 'Z', (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd',
                (byte) 'e', (byte) 'f', (byte) 'g', (byte) 'h', (byte) 'i',
                (byte) 'j', (byte) 'k', (byte) 'l', (byte) 'm', (byte) 'n',
                (byte) 'o', (byte) 'p', (byte) 'q', (byte) 'r', (byte) 's',
                (byte) 't', (byte) 'u', (byte) 'v', (byte) 'w', (byte) 'x',
                (byte) 'y', (byte) 'z', (byte) '0', (byte) '1', (byte) '2',
                (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7',
                (byte) '8', (byte) '9', (byte) '+', (byte) '/'
        };
    }

    // LOCK
    public static class Lock {
        public static final int STATE_UNSELECTED = 0;
        public static final int STATE_SELECTED = 1;
        public static final int STATE_HIGHLIGHTED = 2;
        public static final int STATE_CORRECT = 3;
        public static final int STATE_INCORRECT = 4;
        public static final int STATE_CUSTOM = 5;

        public static final int[] DEFAULT_STATE_COLORS = {0xff949ab2, 0xff129fdd, 0xff129fdd, 0xff1111ff, 0xffdd1111, 0xff999999};

        public static final int CIRCLE_OUTER = 0;
        public static final int CIRCLE_MIDDLE = 1;
        public static final int CIRCLE_INNER = 2;
        public static final int CIRCLE_COUNT = 3;
        // public static final int CIRCLE_COUNT = 2;
        public static final int[] CIRCLE_ORDER = {CIRCLE_OUTER, CIRCLE_MIDDLE, CIRCLE_INNER};
        public static final float[] CIRCLE_RATIOS = {1.0f, 0.95f, 0.33f};
        public static final int[] DEFAULT_CIRCLE_COLORS = {0xff949ab2, 0xff232736, 0xff129fdd};

        public static final int DEFAULT_LENGTH_PX = 50;
        public static final int DEFAULT_LENGTH_NODES = 3;
        public static final float CELL_NODE_RATIO = 0.575f;
        public static final float NODE_EDGE_RATIO = 0.95f;
        public static final int EDGE_COLOR = 0xff129fdd;
        // public static final int BACKGROUND_COLOR = 0xff000000;
        public static final int DEATH_COLOR = 0xffff0000;
        public static final int PRACTICE_RESULT_DISPLAY_MILLIS = 100;
        public static final long BUILD_TIMEOUT_MILLIS = 1000;
        public static final int TACTILE_FEEDBACK_DURATION = 35;

        // Thumbnail
        public static final float WIDTH_ZOOM_SCALE = 0.3f;
        public static final float HEIGHT_ZOOM_SCALE = 0.3f;
        public static final float WIDTH_TRANSLATE_SCALE = 0.4f;
        public static final float HEIGHT_TRANSLATE_SCALE = 0.845f;

        public static final int PRACTICE_RESULT_ONCE = 0x10001;
        public static final int PRACTICE_RESULT_TWICE = 0x10002;

        public static final int GESTURE_REQUEST_CODE = 1;
        public static final int RESET_GESTURE_RESULT_CODE = 2;
        public static final int CLOSE_GESTURE_RESULT_CODE = 3;
        public static final int SET_GESTURE_RESULT_CODE = 4;
    }

    public static class Scanner {
        public static final int REQUEST_CODE_PAYMENT = 0x0030;
        public static final float BEEP_VOLUME = 0.10f;
        public static final long VIBRATE_DURATION = 200L;
        public static final int SCUCCESS = 1;
        public static final int FAIL = -1;
    }

    public class TTS {
        public static final String TTS_ROLE = "xiaoyan";
        public static final String TTS_SPEED = "40";
        public static final String TTS_VOLUME = "100";
        public static final String APPID = "=5981adfc";
    }

    public class DownloadProgressBar {
        public static final int DEFAULT_PROGRESS_DURATION = 1000;
        public static final int DEFAULT_RESULT_DURATION = 1000;
        public static final float DEFAULT_OVERSHOOT_VALUE = 2.5f;
    }

    public class Permission {
        public static final int FILEPROVIDER_DEFAULT_FLAG = -1;
    }

    public class KeyBoard {
        public static final int EMPTY_CODE = -10;
    }

    public static class Channel {
        public static final String NAME = "InstallChannel";
        public static final String SHANG_MI = "pos_shangmi";
        public static final String MO_FANG = "pos_mofang";
        public static final String WANG = "pos_wang";
        public static final String APP = "hy_app";
        public static final String SERVER = "CHANNEL_SERVER";
        public static final String SERVER_95 = "95";
        public static final String SERVER_104 = "104";
        public static final String SERVER_105 = "105";
    }
}
