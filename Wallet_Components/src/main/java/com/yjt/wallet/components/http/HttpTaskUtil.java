package com.yjt.wallet.components.http;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpTaskUtil {

    private static HttpTaskUtil httpTaskUtil;
    private Map<String, List<HttpTask>> httpTasks;

    private HttpTaskUtil() {
        httpTasks = new ConcurrentHashMap<>();
    }

    public static synchronized HttpTaskUtil getInstance() {
        if (httpTaskUtil == null) {
            httpTaskUtil = new HttpTaskUtil();
        }
        return httpTaskUtil;
    }

    public static void releaseInstance() {
        if (httpTaskUtil != null) {
            httpTaskUtil = null;
        }
    }

    public void removeTask(String key) {
        if (httpTasks.containsKey(key)) {
            httpTasks.remove(key);
        }
    }

    public void addTask(String key, HttpTask task) {
        if (httpTasks.containsKey(key)) {
            List<HttpTask> tasks = httpTasks.get(key);
            tasks.add(task);
            httpTasks.put(key, tasks);
        } else {
            List<HttpTask> tasks = Lists.newArrayList();
            tasks.add(task);
            httpTasks.put(key, tasks);
        }
    }

    public boolean contains(String key) {
        return httpTasks.containsKey(key);
    }
}
