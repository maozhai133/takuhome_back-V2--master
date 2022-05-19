package com.takuhome.back.service;

/**
 * 邮件发送服务
 *
 * @Title:MailService
 * @Author:NekoTaku
 * @Date:2021/12/02 11:12
 * @Version:1.0
 */
public interface IMailService {

    void sendMail(String email,String subject,String content);
}
