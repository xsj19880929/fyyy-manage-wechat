package com.suryani.manage.util;


import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.suryani.manage.system.domain.SystemUser;

public class SystemUserUtils {

    private static final String USER_KEY = "systemUser";

    public static SystemUser getCurrentUser() {
        SystemUser currentUser = null;
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            currentUser = (SystemUser) attributes.getAttribute(USER_KEY, RequestAttributes.SCOPE_SESSION);
        }
        return currentUser;
    }
    
    public static void invalid(){
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            attributes.setAttribute(USER_KEY, null, RequestAttributes.SCOPE_SESSION);
        }
    }

}
