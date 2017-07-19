package com.suryani.manage.booking.web;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.quidsi.core.platform.web.site.SiteController;
import com.quidsi.core.util.StringUtils;
import com.suryani.manage.booking.domain.Department;
import com.suryani.manage.booking.service.DepartmentService;
import com.suryani.manage.util.DateEditor;

@Controller
@RequestMapping("department")
public class DepartmentController extends SiteController {
    @Inject
    private DepartmentService departmentService;

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(Date.class, new DateEditor());
    }

    @RequestMapping(value = "/home")
    public String home(Map<String, Object> viewData) {
        return "common.department.list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@Valid Department department, Map<String, Object> viewData) {
        String id = department.getId();
        if (StringUtils.hasText(id)) {
            departmentService.update(department);
        } else {
            department.setId(UUID.randomUUID().toString());
            departmentService.save(department);
        }
        return "redirect:home";
    }

    @RequestMapping(value = "/add/view", method = RequestMethod.GET)
    public String addView(@RequestParam("id") String id, Map<String, Object> viewData) {
        if (StringUtils.hasText(id)) {
            Department department = this.departmentService.getById(id);
            if (department != null) {
                viewData.put("bean", department);
            }
        }
        return "common.department.update";
    }

}
