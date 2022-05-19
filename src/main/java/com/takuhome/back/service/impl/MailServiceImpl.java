package com.takuhome.back.service.impl;

import com.takuhome.back.service.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * 邮件发送实现层
 *
 * @Title:MailServiceImpl
 * @Author:NekoTaku
 * @Date:2021/12/02 11:13
 * @Version:1.0
 */

@Service
public class MailServiceImpl implements IMailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;//发送者

    /**
     *
     * @param email 发送的邮箱地址
     * @param subject 发送的主题
     * @param content 发送的地址
     */
    @Override
    public void sendMail(String email, String subject, String content) {
//        log.info();
        MimeMessage message;

        message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setSubject(subject);
            helper.setTo(email);
            helper.setText(content,true);
            mailSender.send(message);

//            log.info("邮件发送成功");
        } catch (MessagingException e) {
//            e.printStackTrace();
//            log.info("邮箱发送失败",e);
            System.out.println("邮件发送失败!");
        }
    }
}
