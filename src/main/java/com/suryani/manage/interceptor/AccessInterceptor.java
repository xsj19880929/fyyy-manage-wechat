package com.suryani.manage.interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.suryani.manage.system.domain.SystemUser;
import com.suryani.manage.util.SystemUserUtils;
import com.suryani.manage.util.Utils;

public class AccessInterceptor extends HandlerInterceptorAdapter {

    @Inject
    private MenuGetter menuGetter;

    private List<String> freeAccessUrl = new ArrayList<String>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUrl = Utils.getRequestUrl(request);
        if (isFreeAccessUrl(requestUrl)) {
            return true;

        }
        SystemUser systemUser = SystemUserUtils.getCurrentUser();
        boolean blockOper = true;
        if (systemUser != null) {
            if (systemUser.getIsAdmin().equals(SystemUser.ADMIN)) {
                blockOper = false;
            } else {

                List<Map<String, Object>> menus = menuGetter.getMenus(systemUser.getRoleId());

                if (menus != null) {
                    int lidx = requestUrl.lastIndexOf("/");
                    String comparedPath = requestUrl.substring(0, lidx == 0 ? requestUrl.length() : lidx);
                    while (comparedPath.lastIndexOf("/") != 0) {
                        lidx = comparedPath.lastIndexOf("/");
                        comparedPath = requestUrl.substring(0, lidx == 0 ? requestUrl.length() : lidx);
                    }
                    for (Map<String, Object> menu : menus) {
                        String menuPath = "/" + menu.get("url");
                        if (menuPath.indexOf(comparedPath) != -1) {
                            blockOper = false;
                            break;
                        }
                    }
                }
            }
            if (blockOper && (requestUrl.equals("/passport/home") || requestUrl.indexOf("/account/") != -1)) {
                blockOper = false;
            }
        }
        
        if (blockOper) {
            if (Utils.isAjaxRequest(request)) {
                response.setStatus(HttpStatus.SC_UNAUTHORIZED);
                response.getWriter().write("Sorry,you have not permission to visit this page!");
                response.flushBuffer();

            } else {
                response.sendRedirect(Utils.getContextPath(request) + "passport/login" + "?preRequestUrl=" + Utils.getRequestUrlWithParam(request));
            }
        }

        return !blockOper;
    }

    private boolean isFreeAccessUrl(String requestUrl) {
        int idx = 1;
        for (String free : freeAccessUrl) {
            if (requestUrl.equals("/")) {
                break;
            }
            idx = requestUrl.indexOf(free);
            if (idx != -1) {
                break;
            }
        }
        return idx != -1;
    }

    public void addFreeAccessUrl(String freeAccessUrl) {
        this.freeAccessUrl.add(freeAccessUrl);
    }
}
