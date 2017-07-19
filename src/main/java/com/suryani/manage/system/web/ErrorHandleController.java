package com.suryani.manage.system.web;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.quidsi.core.platform.web.site.SiteController;

@Controller
public class ErrorHandleController extends SiteController {

    @RequestMapping(value = "/error/resource-not-found")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFound() {
        return "sys.404";
    }

    @RequestMapping(value = "/error/internal-error")
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String error() {
        return "sys.error";
    }

}
