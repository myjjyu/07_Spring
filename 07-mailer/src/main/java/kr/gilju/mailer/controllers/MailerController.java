package kr.gilju.mailer.controllers;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MailerController {

  @Autowired
  JavaMailSender javaMailSender;

  @GetMapping("/")
  public String index() {
    return "index";
  }

  @PostMapping("/sendmail")
  public String postMethodName(
      @RequestParam("sender-name") String senderName,
      @RequestParam("sender-email") String senderEmail,
      @RequestParam("receiver-name") String receiverName,
      @RequestParam("receiver-email") String receiverEmail,
      @RequestParam("subject") String subject,
      @RequestParam("content") String content) {

    /** 1) 메일 발송 정보 로그 확인 */
    log.debug(content);
    log.debug("---------------------------------------------");
    log.debug(String.format("SenderName: %s", senderName));
    log.debug(String.format("SenderEmail: %s", senderEmail));
    log.debug(String.format("RecvName: %s", receiverName));
    log.debug(String.format("RecvEmail: %s", receiverEmail));
    log.debug(String.format("Subject: %s", subject));
    log.debug(String.format("Content: %s", content));
    log.debug("---------------------------------------------");

    /** 2) java mail 라이브러리를 활용한 메일 발송 */
    MimeMessage message = javaMailSender.createMimeMessage();

    MimeMessageHelper helper = new MimeMessageHelper(message);

    // 제목, 내용, 수신자 설정
    try {
      helper.setSubject(subject);
      helper.setText(content, true);
      helper.setTo(new InternetAddress(receiverEmail, receiverName, "UTF-8"));
      helper.setFrom(new InternetAddress(senderEmail, senderName, "UTF-8"));
      helper.setText(content, true);
    } catch (MessagingException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    // 메일 보내기
    javaMailSender.send(message);

    return "redirect:/";
  }
}
