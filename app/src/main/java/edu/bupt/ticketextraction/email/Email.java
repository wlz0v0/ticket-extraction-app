package edu.bupt.ticketextraction.email;


import android.util.Log;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/08/30
 *     desc   : 设置邮件内容并发送
 *              注意需要安装mail.jar和activation.jar两个包
 *              TODO 本类仍存在无法发送邮件的问题，等待一名大佬进行修复。
 *              TODO 目前的问题疑似是sendFromPassword错误，修改成错误的授权码会报相同的错误。
 *              TODO 目前可以使用python发邮件
 *     version:
 * </pre>
 */
public class Email {
    // 寄件人，武连增的QQ邮箱
    private final String sendFrom;
    // QQ邮箱授权码
    private final String sendFromPassword;
    // 收件人，用户填写
    private final String sendTo;
    // 邮件信息
    private MimeMessage message;
    // 邮件传输对象
    private Transport transport;

    public Email(String sendTo) {
        sendFrom = "1228393790@qq.com";
        sendFromPassword = "juamvioqcamqgajf";
        this.sendTo = sendTo;
        initEmailInfo();
    }

    // 设置邮件题目
    public void setTitle(String title) {
        try {
            message.setSubject(title);
            message.saveChanges();
        }
        catch (Exception e) {
            Log.e("Message error", e.toString());
        }
    }

    // 设置邮件正文，可以使用html
    public void setContent(String content) {
        try {
            message.setContent(content, "text/html;charset=UTF-8");
            message.saveChanges();
        }
        catch (Exception e) {
            Log.e("Message error", e.toString());
        }
    }

    public boolean send() {
        try {
            transport.connect(sendFrom,sendFromPassword);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 初始化邮件信息
    private void initEmailInfo() {
        // 创建配置文件
        Properties properties = createProperties();

        // 创建会话对象
        Session session = Session.getInstance(properties,
                new MyAuthenticator(sendFrom, sendFromPassword));
        // 设置debug模式
        session.setDebug(true);

        message = createMessage(session);

        try {
            transport = session.getTransport();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    private Properties createProperties() {
        Properties properties = new Properties();
        // 使用协议
        properties.setProperty("mail.transport.protocol", "smtp");
        // 邮箱的smtp服务器地址
        properties.setProperty("mail.host", "smtp.qq.com");
        // 请求认证
        properties.setProperty("mail.smtp.auth", "true");

        // QQ邮箱需要使用SSL安全连接
        // QQ邮箱的SSL端口为465或587
        final String smtp_port = "587";
        properties.setProperty("mail.smtp.port", smtp_port);
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        properties.setProperty("mail.smtp.socketFactory.port", smtp_port);

        return properties;
    }

    private MimeMessage createMessage(Session session) {
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(sendFrom, "票据识别", "UTF-8"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(sendTo));
            message.setSubject("测试邮件");
            message.setContent("test", "text/html;charset=UTF-8");
            message.setSentDate(new Date());
            message.saveChanges();
        } catch (Exception e) {
            Log.e("Email error:", e.toString());
        }
        return message;
    }
}
