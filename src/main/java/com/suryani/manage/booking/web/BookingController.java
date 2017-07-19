package com.suryani.manage.booking.web;

import com.quidsi.core.json.JSONBinder;
import com.quidsi.core.platform.web.site.SiteController;
import com.quidsi.core.util.StringUtils;
import com.suryani.manage.booking.domain.Booking;
import com.suryani.manage.booking.domain.Doctor;
import com.suryani.manage.booking.domain.DoctorDateTime;
import com.suryani.manage.booking.domain.Hospital;
import com.suryani.manage.booking.service.BookingService;
import com.suryani.manage.booking.service.DepartmentService;
import com.suryani.manage.booking.service.DoctorDateTimeService;
import com.suryani.manage.booking.service.DoctorService;
import com.suryani.manage.booking.service.HospitalService;
import com.suryani.manage.schedule.service.HospitalRegisterService;
import com.suryani.manage.schedule.service.RegisterBean;
import com.suryani.manage.util.CalendarUtils;
import com.suryani.manage.util.DateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("booking")
public class BookingController extends SiteController {
    @Inject
    private BookingService bookingService;
    @Inject
    private DepartmentService departmentService;
    @Inject
    private DoctorService doctorService;
    @Inject
    private HospitalService hospitalService;
    @Inject
    private HospitalRegisterService hospitalRegisterService;
    @Inject
    private DoctorDateTimeService doctorDateTimeService;

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(Date.class, new DateEditor());
    }

    @RequestMapping(value = "/home")
    public String home(Map<String, Object> viewData, HttpServletRequest request) {
        if (request.getSession().getAttribute("bookingBeginTime") != null) {
            viewData.put("bookingBeginTime", request.getSession().getAttribute("bookingBeginTime"));
        }
        if (request.getSession().getAttribute("bookingEndTime") != null) {
            viewData.put("bookingEndTime", request.getSession().getAttribute("bookingEndTime"));
        }
        return "common.booking.list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@Valid Booking booking, Map<String, Object> viewData) {
        String id = booking.getId();
        Doctor doctor = doctorService.getOneByDoctorId(booking.getDoctorId());
        if (StringUtils.hasText(booking.getBackupDoctorId())) {
            Doctor backupDoctor = doctorService.getOneByDoctorId(booking.getBackupDoctorId());
            if (backupDoctor != null) {
                booking.setBackupDoctorSn(backupDoctor.getDoctorSn());
                booking.setBackupDoctor(backupDoctor.getDoctor());
            }
        }
        Hospital hospital = hospitalService.getOneByHospitalId(booking.getOrgId());
        booking.setOrgCode(hospital.getOrgCode());
        booking.setOrgName(hospital.getName());
        if (doctor != null) {
            booking.setDoctorSn(doctor.getDoctorSn());
            booking.setDoctor(doctor.getDoctor());
        }

        if (StringUtils.hasText(id)) {
            booking.setStatus(bookingService.getById(id).getStatus());
            bookingService.update(booking);
        } else {
            booking.setStatus(Booking.STATUS_NOBOOKING);
            booking.setId(UUID.randomUUID().toString());
            bookingService.save(booking);
        }
        return "redirect:home";
    }

    @RequestMapping(value = "/add/view", method = RequestMethod.GET)
    public String addView(@RequestParam("id") String id, Map<String, Object> viewData) {
        viewData.put("hospitals", hospitalService.list(0, 100));
        viewData.put("doctors", doctorService.list(0, 100));
        viewData.put("departments", departmentService.list(0, 100));
        if (StringUtils.hasText(id)) {
            Booking booking = this.bookingService.getById(id);
            if (booking != null) {
                viewData.put("bean", booking);
            }
        }
        return "common.booking.update";
    }

    @RequestMapping(value = "/date")
    public String date(@Valid RegisterBean registerBean, Map<String, Object> viewData) {
        try {
            Map<String, Object> map = hospitalRegisterService.reservationListAll(registerBean);
            if (!"null".equals(map.get("data").toString())) {
                Map<String, Object> dataMap = JSONBinder.binder(Map.class).fromJSON(map.get("data").toString());
                viewData.put("data", dataMap);
                Map<String, Object> doctorMap = JSONBinder.binder(Map.class).fromJSON(JSONBinder.binder(Map.class).toJSON((Map) dataMap.get("doctor")));
                List<Map<String, Object>> dateList = new ArrayList<>();
                if (doctorMap.get("date") instanceof Map) {
                    dateList.add(JSONBinder.binder(Map.class).fromJSON(JSONBinder.binder(Map.class).toJSON((Map) doctorMap.get("date"))));
                } else {
                    dateList = JSONBinder.binder(List.class).fromJSON(JSONBinder.binder(List.class).toJSON((List) doctorMap.get("date")));
                }
                List sectionList = JSONBinder.binder(List.class).fromJSON(JSONBinder.binder(List.class).toJSON((List) dateList.get(0).get("section")));
                Map<String, Object> mapRequest = new HashMap<>();
//                mapRequest.put("registerBean", JSONBinder.binder(RegisterBean.class).toJSON(registerBean));
//                mapRequest.put("sectionList", JSONBinder.binder(List.class).toJSON(sectionList));
                viewData.put("mapRequest", JSONBinder.binder(RegisterBean.class).toJSON(registerBean));
                DoctorDateTime doctorDateTimeRequest = new DoctorDateTime();
                doctorDateTimeRequest.setTriageNo(registerBean.getDeptCode());
                doctorDateTimeRequest.setDoctorId(registerBean.getDocCode());
                doctorDateTimeRequest.setOrgCode(registerBean.getOrgCode());
                doctorDateTimeRequest.setTimeDesc(dateList.get(0).get("time").toString());
                doctorDateTimeRequest.setSelectDate(CalendarUtils.format(CalendarUtils.parse(registerBean.getEndDate(), "yyyy-MM-dd"), "yyyy-MM-dd HH:mm:ss"));
                DoctorDateTime doctorDateTime = doctorDateTimeService.findDoctorDateTime(doctorDateTimeRequest);
                viewData.put("doctorDateTime", doctorDateTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "common.booking.date";
    }

}
