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
import com.suryani.manage.booking.domain.Doctor;
import com.suryani.manage.booking.service.DepartmentService;
import com.suryani.manage.booking.service.DoctorService;
import com.suryani.manage.util.DateEditor;

@Controller
@RequestMapping("doctor")
public class DoctorController extends SiteController {
    @Inject
    private DoctorService doctorService;
    @Inject
    private DepartmentService departmentService;

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(Date.class, new DateEditor());
    }

    @RequestMapping(value = "/home")
    public String home(Map<String, Object> viewData) {
        return "common.doctor.list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@Valid Doctor doctor, Map<String, Object> viewData) {
        String id = doctor.getId();
        if (StringUtils.hasText(id)) {
            doctor.setCreatedTime(doctorService.getById(id).getCreatedTime());
            doctorService.update(doctor);
        } else {
            doctor.setId(UUID.randomUUID().toString());
            doctor.setCreatedTime(new Date());
            doctorService.save(doctor);
        }
        return "redirect:home";
    }

    @RequestMapping(value = "/add/view", method = RequestMethod.GET)
    public String addView(@RequestParam("id") String id, Map<String, Object> viewData) {
        viewData.put("departments", departmentService.findAllNoPaging());
        if (StringUtils.hasText(id)) {
            Doctor doctor = this.doctorService.getById(id);
            if (doctor != null) {
                viewData.put("bean", doctor);
            }
        }
        return "common.doctor.update";
    }

}
