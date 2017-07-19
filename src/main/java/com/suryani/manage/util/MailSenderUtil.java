package com.suryani.manage.util;

import org.springframework.stereotype.Component;

import com.quidsi.core.mail.Mail;
import com.quidsi.core.mail.MailSender;

@Component
public class MailSenderUtil {

    public void sendMail(String subject, String htmlBody) {
        MailSender sender = new MailSender();
        sender.setPort(25);
        sender.setHost("mail.suryani.cn");
        sender.setUsername("soldier.xu@suryani.cn");
        sender.setPassword("xsj19880929HP");
        Mail mail = new Mail();
        mail.setSubject(subject);
        mail.addTo("weixinmq@qq.com");
        mail.setFrom("soldier.xu@suryani.cn");
        mail.setHTMLBody(htmlBody);
        sender.send(mail);
    }

}
