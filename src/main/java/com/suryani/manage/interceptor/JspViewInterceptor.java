package com.suryani.manage.interceptor;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.quidsi.core.json.JSONBinder;
import com.suryani.manage.system.domain.SystemUser;
import com.suryani.manage.util.SystemUserUtils;
import com.suryani.manage.util.Utils;

@Service
public class JspViewInterceptor extends HandlerInterceptorAdapter {

    private String uploadBasePath;
    private String resourceDomain;
    private String uploadPath;
    private String uploadFilePath;
    @Inject
    private MenuGetter menuGetter;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String path = request.getContextPath();
        int port = request.getServerPort();
        String basePath = request.getScheme() + "://" + request.getServerName() + (port == 80 ? "" : ":" + port) + path + "/";
        request.setAttribute("basePath", basePath);
        request.setAttribute("uploadBasePath", uploadBasePath);
        request.setAttribute("resourceDomain", resourceDomain);
        request.setAttribute("uploadPath", uploadPath);
        request.setAttribute("uploadFilePath", uploadFilePath);
        if (!Utils.isAjaxRequest(request)) {
            SystemUser systemUser = SystemUserUtils.getCurrentUser();
            if (systemUser != null) {
                request.setAttribute("systemMenus", JSONBinder.binder(List.class).toJSON(menuGetter.getMenus(systemUser.getRoleId())));
            }
            String menuId = request.getParameter("menuId");
            if (menuId == null) {
                Object sMenuId = request.getSession().getAttribute("MENU_ID_SOTRE");
                if (sMenuId != null) {
                    menuId = (String) sMenuId;
                }
            } else {
                request.getSession().setAttribute("MENU_ID_SOTRE", menuId);
            }
            request.setAttribute("menuId", menuId);
        }
    }

    @Autowired
    @Value("${upload.basePath}")
    public void setUploadBasePath(String uploadBasePath) {
        this.uploadBasePath = uploadBasePath;
    }

    @Autowired
    @Value("${resourceDomain}")
    public void setResourceDomain(String resourceDomain) {
        this.resourceDomain = resourceDomain;
    }

    @Autowired
    @Value("${uploadPath}")
    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    @Autowired
    @Value("${uploadFilePath}")
    public void setUploadFilePath(String uploadFilePath) {
        this.uploadFilePath = uploadFilePath;
    }

}