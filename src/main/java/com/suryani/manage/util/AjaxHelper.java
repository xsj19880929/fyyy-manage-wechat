package com.suryani.manage.util;

import java.util.HashMap;
import java.util.Map;


public class AjaxHelper {

    public static final String SUCCESS = "ok";
    public static final String FAILD = "faild";

    // {st:,msg:}

    public static Map<String, Object> ajaxReturn(String st, Object msg) {
        Map<String, Object> rt = new HashMap<String, Object>();
        rt.put("st", st);
        rt.put("msg", msg);
        return rt;
    }

    public static Map<String, Object> success(Object msg) {
        return ajaxReturn(SUCCESS, msg);
    }

    public static Map<String, Object> faild(Object msg) {
        return ajaxReturn(FAILD, msg);
    }

}
