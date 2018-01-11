package net.coderlin.jmail_demo;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Title: JMailDemoTest
 * Description:
 *
 * @author Lin Hui
 * Created on 2018/1/11 8:34
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JMailDemoTest {
    private static final Logger logger = Logger.getLogger(JMailDemoTest.class);
    /**
     * 发送人用户名
     */
    private static final String SENDER_USERNAME = "sender_username";
    /**
     * 发送人密码
     */
    private static final String SENDER_PASSWORD = "sender_password";
    /**
     * 发送人邮箱
     */
    private static final String SENDER_EMAIL = "sender_email@qq.com";
    /**
     * 收件人邮箱
     */
    private static final String RECIPIENT_EMAIL = "recipient@163.com";

    /**
     * 只依赖于javax.jmail
     */
    @Test
    public void testJMail() throws MessagingException {
        Properties props = new Properties();
        props.setProperty("mail.debug", "true");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.host", "smtp.qq.com");
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.ssl.enable", "true");

        Session session = Session.getInstance(props);

        Message msg = new MimeMessage(session);
        msg.setSubject("JavaMail邮件测试");
        msg.setText("这是一封由JavaMail发送的邮件！");
        msg.setFrom(new InternetAddress(SENDER_EMAIL));
        Transport transport = session.getTransport();
        transport.connect(SENDER_USERNAME, SENDER_PASSWORD);
        transport.sendMessage(msg, new Address[]{new InternetAddress(RECIPIENT_EMAIL)});
        transport.close();
    }

    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String username;

    /**
     * 增加Spring Boot依赖
     * message.from要与application.yml中的spring.mail.username一致
     */
    @Test
    public void testSpringMail() {
        logger.info("username: " + username);
        logger.info("SENDER_USERNAME: " + SENDER_USERNAME);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(RECIPIENT_EMAIL);
        message.setSubject("SpringMail邮件测试");
        message.setText("这是一封由SpringMail发送的邮件！");
        javaMailSender.send(message);
    }
}
