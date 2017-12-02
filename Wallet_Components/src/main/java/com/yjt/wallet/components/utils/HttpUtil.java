package com.yjt.wallet.components.utils;

import android.os.Handler;

import com.yjt.wallet.components.constant.Regex;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {

    private static HttpUtil httpUtil;

    private HttpUtil() {
        // cannot be instantiated
    }

    public static synchronized HttpUtil getInstance() {
        if (httpUtil == null) {
            httpUtil = new HttpUtil();
        }
        return httpUtil;
    }

    public void doGet(HashMap<String, String> parameter, String url, Handler handler, int... message) {
        HttpURLConnection connection = null;
        StringBuilder builder = new StringBuilder(url);
        if (parameter != null && parameter.size() != 0) {
            builder.append(Regex.QUESTION_MARK.getRegext());
            boolean isFirst = true;
            for (Map.Entry<String, String> entry : parameter.entrySet()) {
                if (!isFirst) {
                    builder.append(Regex.AND.getRegext());
                }
                builder.append(entry.getKey());
                builder.append(Regex.EQUALS.getRegext());
                builder.append(entry.getValue());
                isFirst = false;
            }
        }
        try {
            connection = (HttpURLConnection) new URL(builder.toString()).openConnection();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setConnectTimeout(30 * 1000);
            connection.setReadTimeout(18 * 1000);
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                handler.sendMessage(MessageUtil.getMessage(message[0], convertStreamToString(connection.getInputStream())));
            } else {
                handler.sendMessage(MessageUtil.getMessage(message[1], "服务器连接出错,请稍后重试"));
            }
        } catch (Exception e) {
            handler.sendMessage(MessageUtil.getMessage(message[2], "服务器连接异常,请稍后重试"));
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public void doPost(HashMap<String, String> parameter, String url, Handler handler, int... message) {
        HttpURLConnection connection = null;
        StringBuilder builder = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : parameter.entrySet()) {
                builder.append(entry.getKey()).append(Regex.EQUALS.getRegext()).append(URLEncoder.encode(entry.getValue(), Regex.UTF_8.getRegext())).append(Regex.AND.getRegext());
            }
            builder.deleteCharAt(builder.length() - 1);
            LogUtil.getInstance().print("post url:" + url);
            LogUtil.getInstance().print("post request:" + builder.toString());
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Length", String.valueOf(builder.toString().getBytes().length));
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setConnectTimeout(30 * 1000);
            connection.setReadTimeout(18 * 1000);
            connection.connect();

            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(builder.toString());
            outputStream.flush();
            outputStream.close();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                handler.sendMessage(MessageUtil.getMessage(message[0], convertStreamToString(connection.getInputStream())));
            } else {
                handler.sendMessage(MessageUtil.getMessage(message[1], "服务器连接出错,请稍后重试"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            handler.sendMessage(MessageUtil.getMessage(message[2], "http excep"));
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String convertStreamToString(InputStream is) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder builder = new StringBuilder();
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return builder.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}

