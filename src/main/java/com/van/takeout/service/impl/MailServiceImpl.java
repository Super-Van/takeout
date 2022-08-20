package com.van.takeout.service.impl;

import com.van.takeout.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * 发送普通文本邮件
     *
     * @param from
     * @param to
     * @param text
     */
    public void sendMail(String from, String to, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("测试外卖登录");
        message.setText(text);
        javaMailSender.send(message);
    }
}
