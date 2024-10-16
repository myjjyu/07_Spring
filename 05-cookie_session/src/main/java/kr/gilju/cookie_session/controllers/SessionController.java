package kr.gilju.cookie_session.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import ch.qos.logback.core.model.Model;

@Controller
public class SessionController {
    /**
   * 세션 저장을 위한 작성 페이지 
   */
   @GetMapping("/session/home")
   public String home(Model model) {
    return "/session/home";
   }


       /**
   * 심플 로그인 폼
   */
   @GetMapping("/session/login")
   public String home() {
    return "/session/login";
   }
}
