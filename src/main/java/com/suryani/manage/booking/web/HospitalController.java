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
import com.suryani.manage.booking.domain.Hospital;
import com.suryani.manage.booking.service.HospitalService;
import com.suryani.manage.util.DateEditor;

@Controller
@RequestMapping("hospital")
public class HospitalController extends SiteController {
    @Inject
    private HospitalService hospitalService;

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(Date.class, new DateEditor());
    }

    @RequestMapping(value = "/home")
    public String home(Map<String, Object> viewData) {
        return "common.hospital.list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@Valid Hospital hospital, Map<String, Object> viewData) {
        String id = hospital.getId();
        if (StringUtils.hasText(id)) {
            hospitalService.update(hospital);
        } else {
            hospital.setId(UUID.randomUUID().toString());
            hospitalService.save(hospital);
        }
        return "redirect:home";
    }

    @RequestMapping(value = "/add/view", method = RequestMethod.GET)
    public String addView(@RequestParam("id") String id, Map<String, Object> viewData) {
        if (StringUtils.hasText(id)) {
            Hospital hospital = this.hospitalService.getById(id);
            if (hospital != null) {
                viewData.put("bean", hospital);
            }
        }
        return "common.hospital.update";
    }

}
