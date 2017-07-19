package com.suryani.manage.schedule.service;

import com.suryani.manage.common.CHttpClient;
import common.SpringServiceTest;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.inject.Inject;

/**
 * @author soldier
 */
public class HospitalRegisterServiceTest extends SpringServiceTest {
    @Inject
    HospitalRegisterService hospitalRegisterService;
    @Inject
    CHttpClient cHttpClient;

    @Test
    @Rollback
    public void test() {
        try {
//        logger.info("预约中");
//            RequestBuilder requestBuilder = RequestBuilder.get()
//                    .setUri("http://localhost:8089/booking/home?menuId=4f2da5a6-f1ab-4226-9f77-cb84d8f59f36")
//                    .setHeader("Host", "wechat.xmsmjk.com")
//                    .setHeader("Referer", "http://wechat.xmsmjk.com/zycapwxsehr/view/appointment/confirm.jsp")
//                    .setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9B176 MicroMessenger/4.3.2");
//
//            HttpUriRequest request = requestBuilder.build();
//            HttpResponse httpResponse = cHttpClient.execute(request);
//            String responseStr = EntityUtils.toString(httpResponse.getEntity());
//            System.out.println(responseStr);
//        logger.info(responseStr);
//        try {
            hospitalRegisterService.testDo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
