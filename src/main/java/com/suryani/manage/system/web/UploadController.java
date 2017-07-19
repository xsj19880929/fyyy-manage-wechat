package com.suryani.manage.system.web;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.quidsi.core.json.JSONBinder;
import com.quidsi.core.platform.web.site.SiteController;
import com.quidsi.core.util.StringUtils;

@Controller
@RequestMapping("upload")
public class UploadController extends SiteController {

    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    public void uploadcallback(@RequestParam(required = false) String methodName, HttpServletRequest request, HttpServletResponse response) {
        Enumeration rnames = request.getParameterNames();
        Map<String, String> map = new HashMap<String, String>();
        for (Enumeration e = rnames; e.hasMoreElements();) {
            String thisName = e.nextElement().toString();
            String thisValue = request.getParameter(thisName);
            map.put(thisName, thisValue);
        }
        String jsonString = JSONBinder.binder(Map.class).toJSON(map);
        String resMsg = null;
        if (StringUtils.equals("jscbCKEdit", methodName)) {
            resMsg = "<script>window.parent.jscbCKEdit(" + jsonString + ");</script>";

        } else {
            resMsg = "<script>window.parent.jscb(" + jsonString + ");</script>";
        }
        try {
            response.getWriter().write(resMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
