package com.suryani.manage.interceptor;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.suryani.manage.annotation.LogOperationRequired;
import com.suryani.manage.annotation.WhenLog;
import com.suryani.manage.system.domain.SystemUser;
import com.suryani.manage.system.service.SystemLogService;
import com.suryani.manage.util.SystemUserUtils;

public class LogOpetationRequiredInterceptor extends HandlerInterceptorAdapter {
    @Inject
    private SystemLogService systemLogService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        proc(handler, WhenLog.before);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        proc(handler, WhenLog.after);
    }

    private void proc(Object handler, int flag) {
        if (!(handler instanceof HandlerMethod)) {
            return;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        LogOperationRequired log = handlerMethod.getMethodAnnotation(LogOperationRequired.class);
        if (log == null) {
            return;
        }
        if ((flag == WhenLog.before && log.whenLog() == flag) || flag == WhenLog.after) {
            SystemUser user = SystemUserUtils.getCurrentUser();
            if (user != null) {
                this.systemLogService.save(user.getId(), log.value(), -1);
            }

        }
    }

}
