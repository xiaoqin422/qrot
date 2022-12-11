package cn.stuxx.utils;

import cn.stuxx.exception.QRotException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 包名: com.study.seckill.util
 * 类名: EmailUtil
 * 创建用户: 25789
 * 创建日期: 2022年10月01日 19:56
 * 项目名: seckill
 *
 * @author: 秦笑笑
 **/
@Component
@Slf4j
public class EmailUtil {
    @Resource
    private JavaMailSender javaMailSender;

    /**
     * 发件人邮箱
     */
    @Value("${email.system}")
    private String mailbox;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        send(message);
        log.info("发送邮件。from:{},to:{},msg:{}", mailbox, to, message);
    }

    private void send(SimpleMailMessage mailMessage) {
        try {
            mailMessage.setFrom(mailbox);
            //抄送
            // mailMessage.setCc(mailbox);
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            log.error("发送失败！发送人:{}", mailMessage.getTo().toString());
            throw new QRotException("邮件服务异常", e);
        }
    }

}