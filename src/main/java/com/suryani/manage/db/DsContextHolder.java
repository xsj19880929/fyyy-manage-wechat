package com.suryani.manage.db;

public class DsContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static void setKey(String key) {
        contextHolder.set(key);
    }

    public static String getKey() {
        return (String) contextHolder.get();
    }

    public static void clearKey() {
        contextHolder.remove();
    }
}
