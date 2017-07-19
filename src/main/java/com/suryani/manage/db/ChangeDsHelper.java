package com.suryani.manage.db;

public class ChangeDsHelper {
    
    private static final ThreadLocal<String> tmpHolder = new ThreadLocal<String>();
    
    public static void useMange(){
        String key=DsContextHolder.getKey();
        tmpHolder.set(key);
        DsContextHolder.setKey("manage");
    }
    
    public static void releaseUseManage(){
        DsContextHolder.setKey(tmpHolder.get());
        tmpHolder.remove();
    }

}
