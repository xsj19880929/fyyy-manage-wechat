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
import com.suryani.manage.booking.domain.JobSchedule;
import com.suryani.manage.booking.service.JobScheduleService;
import com.suryani.manage.util.DateEditor;

@Controller
@RequestMapping("jobschedule")
public class JobScheduleController extends SiteController {
    @Inject
    private JobScheduleService jobScheduleService;

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(Date.class, new DateEditor());
    }

    @RequestMapping(value = "/home")
    public String home(Map<String, Object> viewData) {
        return "common.jobschedule.list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@Valid JobSchedule jobSchedule, Map<String, Object> viewData) {
        String id = jobSchedule.getId();
        if (StringUtils.hasText(id)) {
            jobSchedule.setCreatedTime(jobScheduleService.getById(id).getCreatedTime());
            jobSchedule.setUpdatedTime(new Date());
            jobScheduleService.update(jobSchedule);
        } else {
            jobSchedule.setId(UUID.randomUUID().toString());
            jobSchedule.setCreatedTime(new Date());
            jobScheduleService.save(jobSchedule);
        }
        return "redirect:home";
    }

    @RequestMapping(value = "/add/view", method = RequestMethod.GET)
    public String addView(@RequestParam("id") String id, Map<String, Object> viewData) {
        if (StringUtils.hasText(id)) {
            JobSchedule jobSchedule = this.jobScheduleService.getById(id);
            if (jobSchedule != null) {
                viewData.put("bean", jobSchedule);
            }
        }
        return "common.jobschedule.update";
    }

}
