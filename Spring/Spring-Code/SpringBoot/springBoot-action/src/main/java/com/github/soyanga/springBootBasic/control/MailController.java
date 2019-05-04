package com.github.soyanga.springBootBasic.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @program: springBoot-action
 * @Description: mail服务
 * @Author: SOYANGA
 * @Create: 2019-05-04 15:33
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/mail")
public class MailController {
    @Autowired
    private JavaMailSender mailSender;

    @RequestMapping(value = "/simpleMail1")
    public String mail1() {
        try {
            sendSimpleMail1();
            return "success";
        } catch (MessagingException e) {
            return "failed";
        }
    }

    /**
     * 第一种方式放松简单邮件
     * java类中的发送邮件方法
     * <p>
     * 构建发送邮件内容，以及发送方，发出方，邮件标题
     *
     * @throws MessagingException
     */
    public void sendSimpleMail1() throws MessagingException {
        //构建邮件主体
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        //填充发送给谁
        mimeMessage.setRecipient(
                Message.RecipientType.TO,
                new InternetAddress("1765268431@qq.com")
        );
        //填充发送方
        mimeMessage.setFrom(new InternetAddress("321830735@qq.com"));
        //填充邮件标题
        mimeMessage.setSubject("SpringBoot send email by style1");
        //填充邮件内容
        mimeMessage.setText("Hello this is a simple mail");
        //发送邮件
        mailSender.send(mimeMessage);
    }


    @RequestMapping(value = "/simpleMail2")
    public String mail2() {
        try {
            sendSimpleMail2();
            return "success";
        } catch (MessagingException e) {
            return "failed";
        }
    }

    /**
     * 第二种方式放松简单邮件  --->构建简单，推荐使用
     * Spring中发送邮件的方法
     * <p>
     * 构建邮件主体：发出方，发送方，邮件标题，邮件内容
     *
     * @throws MessagingException
     */
    public void sendSimpleMail2() throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom("321830735@qq.com");
        helper.setTo("1765268431@qq.com");
        // 密送
        helper.setBcc("872041146@qq.com");
        //抄送
        helper.setCc("SOYANGA@126.com");
        helper.setSubject("SpringBoot send email by style2");
        helper.setText("Hello this is a simple mail to Jennie");
        mailSender.send(mimeMessage);
    }


    @RequestMapping(value = "/multipartMail")
    public String mail3() {
        try {
            sendAttachments();
            return "success";
        } catch (MessagingException e) {
            return "failed";
        }
    }

    /**
     * 发送带附件邮件
     *
     * @throws MessagingException
     */
    public void sendAttachments() throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("321830735@qq.com");
        helper.setTo("1765268431@qq.com");
        // 密送
        helper.setBcc("872041146@qq.com");
        //抄送
        helper.setCc("SOYANGA@126.com");
        helper.setSubject("附件");
        helper.setText("Check out this image!");
        //添加附件以流的形式
        ClassPathResource resource = new ClassPathResource("感悟.png");
        helper.addAttachment("感悟.png", resource);
        mailSender.send(mimeMessage);
    }


    @RequestMapping(value = "/innerMail")
    public String mail4() {
        try {
            seendInnerResource();
            return "success";
        } catch (MessagingException e) {
            return "failed";
        }
    }

    /**
     * 带有内联资源邮件发送
     *
     * @throws MessagingException
     */
    public void seendInnerResource() throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("321830735@qq.com");
        helper.setTo("1765268431@qq.com");
        // 密送
        helper.setBcc("872041146@qq.com");
        //抄送
        helper.setCc("SOYANGA@126.com");
        helper.setSubject("内联资源");

        //资源引用处设置cid,资源标识 ---- >相当于网页
        helper.setText(
                "<html>" +
                        "<body>" +
                        "<h1>加油(ง •_•)ง 💪</h1>" +
                        "<img src='cid:img123'>" +
                        "</body>" +
                        "</html>", true);
        ClassPathResource resource = new ClassPathResource("TIM.png");
        //添加资源时指定cid,资源标识符
        helper.addInline("img123", resource);
        mailSender.send(mimeMessage);
    }

}
