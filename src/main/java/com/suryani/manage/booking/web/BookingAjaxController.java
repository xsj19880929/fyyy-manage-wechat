package com.suryani.manage.booking.web;

import com.quidsi.core.platform.web.rest.RESTController;
import com.quidsi.core.util.StringUtils;
import com.suryani.manage.booking.domain.Booking;
import com.suryani.manage.booking.service.BookingService;
import com.suryani.manage.common.CHttpClient;
import com.suryani.manage.schedule.service.BookingDoctorNewService;
import com.suryani.manage.schedule.service.BookingDoctorOperatingNewService;
import com.suryani.manage.schedule.service.HospitalRegisterService;
import com.suryani.manage.schedule.service.RegisterBean;
import com.suryani.manage.schedule.service.RegisterBeanSwitch;
import com.suryani.manage.util.AjaxHelper;
import com.suryani.manage.util.CalendarUtils;
import com.suryani.manage.util.Utils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("booking")
public class BookingAjaxController extends RESTController {
    @Inject
    private BookingService bookingService;
    @Inject
    private BookingDoctorOperatingNewService bookingDoctorOperatingNewService;
    @Inject
    private BookingDoctorNewService bookingDoctorNewService;
    @Inject
    private CHttpClient cHttpClient;
    @Inject
    private HospitalRegisterService hospitalRegisterService;

    @RequestMapping(value = "/list")
    @ResponseBody
    public Map<String, Object> listWxCbqShop(@RequestParam String username, @RequestParam String beginTime, @RequestParam String endTime, @RequestParam(defaultValue = "0") Integer offset,
                                             @RequestParam(defaultValue = "10") Integer fetchSize, HttpServletRequest request) {
        HttpSession session = request.getSession();

        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.hasText(username)) {
            params.put("username", username);
        }
        if (StringUtils.hasText(beginTime)) {
            params.put("beginTime", CalendarUtils.parse(beginTime, "yyyy-MM-dd"));
            session.setAttribute("bookingBeginTime", beginTime);
        }
        if (StringUtils.hasText(endTime)) {
            params.put("endTime", CalendarUtils.parse(endTime, "yyyy-MM-dd"));
            session.setAttribute("bookingEndTime", endTime);
        }
        return Utils.pagerWarp(bookingService.list(params, offset, fetchSize), bookingService.total(params));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(@RequestParam("id") String id) {
        bookingService.delete(id);
        return "true";
    }

    @RequestMapping(value = "/booking", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> booking(@RequestParam("id") String id) {
        Booking booking = bookingService.getById(id);
        String msg = bookingDoctorOperatingNewService.simbitBookingDoctor(booking);
        if (msg.contains("成功")) {
            bookingService.updateStatus(id, Booking.STATUS_SUCCESS);
        }
        return AjaxHelper.success(msg);

    }

    @RequestMapping(value = "/batchBooking", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> batchBooking(@RequestParam("id") String id) {
        Booking booking = bookingService.getById(id);
        String msg = bookingDoctorOperatingNewService.batchBookingDoctor(booking);
        if (msg.contains("成功")) {
            bookingService.updateStatus(id, Booking.STATUS_SUCCESS);
        }
        return AjaxHelper.success(msg);

    }

    @RequestMapping(value = "/cancelBooking", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> cancelBooking(@RequestParam("id") String id) {
        Booking booking = bookingService.getById(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String msg = bookingDoctorOperatingNewService.cancelBookingDoctor(booking.getIcardid(), sdf.format(booking.getSelectDate()), booking.getDoctor());
        if (msg.contains("成功")) {
            bookingService.updateStatus(id, Booking.STATUS_CANCEL);
        }
        return AjaxHelper.success(msg);

    }

    //    @RequestMapping(value = "/queryBooking", method = RequestMethod.POST)
//    @ResponseBody
//    public Map<String, Object> queryBooking(@RequestParam("id") String id) {
//        Booking booking = bookingService.getById(id);
//        String msg = bookingDoctorOperatingNewService.queryBookingDoctor(booking.getIcardid());
//        return AjaxHelper.success(msg);
//
//    }
    @RequestMapping(value = "/queryBooking", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> queryBooking(@RequestParam("id") String id) {

        Booking booking = bookingService.getById(id);
        RegisterBean registerBean = RegisterBeanSwitch.switchBean(booking);
        HttpClientContext clientContext = null;
        String responseStr = "";
        try {
            clientContext = HttpClientContext.create();
//            hospitalRegisterService.index(registerBean, clientContext);
            hospitalRegisterService.login(registerBean, clientContext);
            HttpUriRequest request = RequestBuilder.post()
                    .setUri("http://wechat.xmsmjk.com/zycapwxsehr/MyReservedController/myReservedData.do")
                    .setHeader("Host", "wechat.xmsmjk.com")
                    .setHeader("Referer", "http://wechat.xmsmjk.com/zycapwxsehr/view/appointment/confirm.jsp")
                    .setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9B176 MicroMessenger/4.3.2")
                    .setHeader("Oid", booking.getOpenId())
                    .addParameter("openid", booking.getOpenId())
                    .build();
            HttpResponse httpResponse = cHttpClient.execute(request, clientContext);
            responseStr = EntityUtils.toString(httpResponse.getEntity());
            request.abort();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxHelper.success(responseStr);

    }

    @RequestMapping(value = "/handBooking", method = RequestMethod.POST)
    @ResponseBody
    public String handBooking() {
        bookingDoctorNewService.startTread();
        return "true";

    }

    @RequestMapping(value = "/removeSession", method = RequestMethod.POST)
    @ResponseBody
    public String removeSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("bookingBeginTime");
        session.removeAttribute("bookingEndTime");
        return "true";
    }

    @RequestMapping(value = "/getTime", method = RequestMethod.POST)
    @ResponseBody
    public String getTime(@Valid @RequestBody RegisterBean registerBean) {

        return "true";

    }

}
