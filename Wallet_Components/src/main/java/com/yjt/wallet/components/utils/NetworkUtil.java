package com.yjt.wallet.components.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.yjt.wallet.components.constant.NetType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class NetworkUtil {

    private static NetworkUtil mInstance;

    private Proxy mProxy = null;

    private NetworkUtil() {
        setDefaultHostnameVerifier();
    }


    public static synchronized NetworkUtil getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkUtil();
        }
        return mInstance;
    }

    public static void releaseInstance() {
        if (mInstance != null) {
            mInstance = null;
        }
    }


    /**
     * 是否连接网络
     *
     * @return
     */
    public boolean isInternetConnecting(Context ctx) {
        ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo[] infos = manager.getAllNetworkInfo();
            if (infos != null)
                for (NetworkInfo info : infos) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
        }
        return false;
    }

//    public boolean isInternetConnecting(Context ctx) {
//        ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo info = manager.getActiveNetworkInfo();
//        return info != null && info.isConnectedOrConnecting();
//    }

    /**
     * 获取网络类型
     *
     * @return
     */
    public String getNetworkType(Context ctx) {
        String type = null;
        NetworkInfo info = ((ConnectivityManager) (ctx.getSystemService(Context.CONNECTIVITY_SERVICE))).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                type = NetType.WIFI.getContent();
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                // TD-SCDMA   networkType is 17
                int networkType = info.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        type = NetType.GG.getContent();
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        type = NetType.GGG.getContent();
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        type = NetType.GGGG.getContent();
                        break;
                    default:
                        if (info.getSubtypeName().equalsIgnoreCase(NetType.TD_SCDMA.getContent())
                                || info.getSubtypeName().equalsIgnoreCase(NetType.WCDMA.getContent())
                                || info.getSubtypeName().equalsIgnoreCase(NetType.CDMA2000.getContent())) {
                            type = "3G";
                        } else {
                            type = info.getSubtypeName();
                        }
                        break;
                }
            }
        }
        return type;
    }

    @SuppressWarnings("deprecation")
    private void detectProxy(Context ctx) {
        ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()
                && info.getType() == ConnectivityManager.TYPE_MOBILE) {
            String proxyHost = android.net.Proxy.getDefaultHost();
            int port = android.net.Proxy.getDefaultPort();
            if (proxyHost != null) {
                final InetSocketAddress sa = new InetSocketAddress(proxyHost,
                                                                   port);
                mProxy = new Proxy(Proxy.Type.HTTP, sa);
            }
        }
    }

    private void setDefaultHostnameVerifier() {
        HostnameVerifier hv = new HostnameVerifier() {

            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }

    public boolean download(String url, File file) {
        boolean result = false;
        HttpURLConnection connection;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            if (mProxy != null) {
                connection = (HttpURLConnection) new URL(url).openConnection(mProxy);
            } else {
                connection = (HttpURLConnection) new URL(url).openConnection();
            }
            connection.setConnectTimeout(60 * 1000);
            connection.setReadTimeout(60 * 1000);
            connection.setDoInput(true);
            connection.connect();
            LogUtil.getInstance().print("----size:" + connection.getContentLength());

            byte[] buffer = new byte[1024];
            int length;
            int startPosition = 0;
            int currPosition;

            if (!file.exists()) {
                file.createNewFile();
            }
            currPosition = startPosition;
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK
                    || connection.getResponseCode() == HttpURLConnection.HTTP_PARTIAL) {
                inputStream = connection.getInputStream();
                outputStream = new FileOutputStream(file, true);
                do {
                    length = inputStream.read(buffer);
                    if (length > 0) {
                        // 写入数据时要使用write(byte[],offer,count);,不要使用write(byte[]);
                        outputStream.write(buffer, 0, length);
                        currPosition = currPosition + length;
                    }
                } while (length != -1);
                result = true;
            } else {
                result = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
