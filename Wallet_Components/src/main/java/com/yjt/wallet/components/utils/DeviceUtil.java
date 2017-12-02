package com.yjt.wallet.components.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;

import com.alibaba.fastjson.JSONObject;
import com.yjt.wallet.components.constant.Constant;
import com.yjt.wallet.components.utils.LogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Enumeration;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class DeviceUtil {

    private static DeviceUtil mInstance;

    public static synchronized DeviceUtil getInstance() {
        if (mInstance == null) {
            mInstance = new DeviceUtil();
        }
        return mInstance;
    }

    public static void releaseInstance() {
        if (mInstance != null) {
            mInstance = null;
        }
    }


    private DeviceUtil() {
        // cannot be instantiated
    }

    public String getDeviceModel() {
        return Build.MODEL;
    }

    private String getSDKVersion() {
        return String.valueOf(Build.VERSION.SDK_INT);
    }

    public String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    public String getSystemName() {
        return Build.VERSION.CODENAME;
    }

    private static String getDeviceBrand() {
        return Build.BRAND;
    }

    private String getDeviceSoftwareVersion(Context ctx) {
        return ctx != null ? ((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceSoftwareVersion() : null;
    }


    public String getDeviceId(Context ctx) {
        return ctx != null ? ((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId() : null;
    }

    private String getLine1Number(Context ctx) {
        return ctx != null ? ((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number() : null;
    }

    private String getNetworkCountryIso(Context ctx) {
        return ctx != null ? ((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkCountryIso() : null;
    }

    private String getNetworkOperator(Context ctx) {
        return ctx != null ? ((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperator() : null;
    }

    private String getNetworkOperatorName(Context ctx) {
        return ctx != null ? ((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName() : null;
    }

    /**
     * NETWORK_TYPE_UNKNOWN  网络类型未知  0
     * NETWORK_TYPE_GPRS     GPRS网络  1
     * NETWORK_TYPE_EDGE     EDGE网络  2
     * NETWORK_TYPE_UMTS     UMTS网络  3
     * NETWORK_TYPE_HSDPA    HSDPA网络  8
     * NETWORK_TYPE_HSUPA    HSUPA网络  9
     * NETWORK_TYPE_HSPA     HSPA网络  10
     * NETWORK_TYPE_CDMA     CDMA网络,IS95A 或 IS95B.  4
     * NETWORK_TYPE_EVDO_0   EVDO网络, revision 0.  5
     * NETWORK_TYPE_EVDO_A   EVDO网络, revision A.  6
     * NETWORK_TYPE_1xRTT    1xRTT网络  7
     *
     * @param ctx
     *
     * @return
     */
    private String getNetworkType(Context ctx) {
        return ctx != null ? String.valueOf(((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkType()) : null;
    }

    private String getPhoneType(Context ctx) {
        return ctx != null ? String.valueOf(((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getPhoneType()) : null;
    }

    private String getSimCountryIso(Context ctx) {
        return ctx != null ? ((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getSimCountryIso() : null;
    }

    private String getSimOperator(Context ctx) {
        return ctx != null ? ((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getSimOperator() : null;
    }

    private String getSimOperatorName(Context ctx) {
        return ctx != null ? ((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getSimOperatorName() : null;
    }

    private String getSimSerialNumber(Context ctx) {
        return ctx != null ? ((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getSimSerialNumber() : null;
    }

    /**
     * SIM_STATE_UNKNOWN          未知状态 0
     * SIM_STATE_ABSENT           没插卡 1
     * SIM_STATE_PIN_REQUIRED     锁定状态，需要用户的PIN码解锁 2
     * SIM_STATE_PUK_REQUIRED     锁定状态，需要用户的PUK码解锁 3
     * SIM_STATE_NETWORK_LOCKED   锁定状态，需要网络的PIN码解锁 4
     * SIM_STATE_READY            就绪状态 5
     *
     * @param ctx
     *
     * @return
     */
    private String getSimState(Context ctx) {
        return ctx != null ? String.valueOf(((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getSimState()) : null;
    }

    private String getSubscriberId(Context ctx) {
        return ctx != null ? ((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId() : null;
    }

    private String getMacAddress(Context ctx) {
        return ctx != null ? ((WifiManager) ctx.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getMacAddress() : null;
    }

    public String getDeviceIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress address = enumIpAddr.nextElement();
                    if (!address.isLoopbackAddress() && (address instanceof Inet4Address)) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public byte[] getDeviceIp(String name) {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().equals(name)) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                            return inetAddress.getAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public int getCpuCores() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            return 1;
        }
        int cores;
        try {
            cores = new File("/sys/devices/system/cpu/").listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    String path = pathname.getName();
                    //regex is slow, so checking char by char.
                    if (path.startsWith("cpu")) {
                        for (int i = 3; i < path.length(); i++) {
                            if (path.charAt(i) < '0' || path.charAt(i) > '9') {
                                return false;
                            }
                        }
                        return true;
                    }
                    return false;
                }
            }).length;
        } catch (SecurityException | NullPointerException e) {
            cores = 0;
        }
        return cores;
    }

    private String getDeviceMemory(Context ctx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
            ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
            manager.getMemoryInfo(info);
            return String.valueOf(info.totalMem);
        } else {
            long memory = 0;
            try {
                FileReader fileReader = new FileReader("/proc/meminfo");
                BufferedReader bufferReader = new BufferedReader(fileReader, 8192);
                memory = Integer.valueOf(bufferReader.readLine().split("\\s+")[1]) * 1024;
                bufferReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Formatter.formatFileSize(ctx, memory);
        }
    }

    public static boolean isPhone(Context context) {
        TelephonyManager telephony = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephony.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    public String getDeviceType(Context context) {
        if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
            return "Android_Table";
        } else if (Build.BRAND.equals("SUNMI")) {
            return Build.BRAND;
        } else {
            return "Android_Mobile";
        }
    }

    public String getDeviceInfo(Context ctx, boolean isEncrypt, int appVer) {
        try {
            JSONObject object = new JSONObject();
//            object.put(Constant.Device.DEVICE_VERSION, getSystemVersion());
//            object.put(Constant.Device.DEVICE_VERSION_NAME, getSystemName());
//            object.put(Constant.Device.DEVICE_TYPE, getPhoneType(ctx));
//            object.put(Constant.Device.DEVICE_ID, getDeviceId(ctx));
//            object.put(Constant.Device.DEVICE_NAME, getDeviceModel());
//            object.put(Constant.Device.DEVICE_CORE, getCpuCores());
//            object.put(Constant.Device.SUBSCRIBER_ID, getSubscriberId(ctx));
//            object.put(Constant.Device.DEVICE_IP, getDeviceIp());
//            LogUtil.getInstance().print(Constant.Device.DEVICE_VERSION + Regex.EQUALS.getRegext() + getSystemVersion() + Regex.COMMA.getRegext());
//            LogUtil.getInstance().print(Constant.Device.DEVICE_VERSION_NAME + Regex.EQUALS.getRegext() + getSystemName() + Regex.COMMA.getRegext());
//            LogUtil.getInstance().print( Constant.Device.DEVICE_TYPE + Regex.EQUALS.getRegext() + getPhoneType(ctx) + Regex.COMMA.getRegext());
//            LogUtil.getInstance().print(Constant.Device.DEVICE_ID + Regex.EQUALS.getRegext() + getDeviceId(ctx) + Regex.COMMA.getRegext());
//            LogUtil.getInstance().print(Constant.Device.DEVICE_NAME + Regex.EQUALS.getRegext() + getDeviceModel() + Regex.COMMA.getRegext());
//            LogUtil.getInstance().print(Constant.Device.DEVICE_CORE + Regex.EQUALS.getRegext() + getCpuCores() + Regex.COMMA.getRegext());
//            LogUtil.getInstance().print(Constant.Device.SUBSCRIBER_ID + Regex.EQUALS.getRegext() + getSubscriberId(ctx) + Regex.COMMA.getRegext());
//            LogUtil.getInstance().print(Constant.Device.DEVICE_IP + Regex.EQUALS.getRegext() + getDeviceIp() + Regex.COMMA.getRegext());
//            LogUtil.getInstance().print(object.toString());

            object.put(Constant.Device.APP_VER, appVer);
            object.put(Constant.Device.DEVICE_TYPE, getDeviceType(ctx));
            object.put(Constant.Device.OS_TYPE, "Android");
            object.put(Constant.Device.OS_VERSION, getSystemVersion());
            object.put(Constant.Device.DEVICE_ID, getDeviceId(ctx));
            LogUtil.getInstance().print("object:" + object.toString());

            if (isEncrypt) {
                return SecurityUtil.getInstance().encrypt3Des(object.toString(), Constant.Data.KEY, Constant.Data.FROMAT);
                
            } else {
                return object.toString();
            }
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | BadPaddingException | InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
            return null;
        }
    }
}