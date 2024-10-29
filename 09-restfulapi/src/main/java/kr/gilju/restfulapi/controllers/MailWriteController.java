package kr.gilju.restfulapi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MailWriteController {

/**
 * 메일 발송폼을 출력하는 메소드
 * 웹 브라우저에게 HTML을 전송한다
 * 프론트엔드 역할
 * @return 메일 발송 폼
 */
  @GetMapping("/mail_form")
  public String mailForm() {
    return "mail_form.html";
  }
}
 