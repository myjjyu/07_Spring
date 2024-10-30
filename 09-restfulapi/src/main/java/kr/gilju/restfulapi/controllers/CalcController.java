package kr.gilju.restfulapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import kr.gilju.restfulapi.helpers.RestHelper;

@Controller
public class CalcController {
  

    @Autowired
  private RestHelper restHelper;
  /**
 * 발송폼을 출력하는 메소드
 * 웹 브라우저에게 HTML을 전송한다
 * 프론트엔드 역할
 * @return 
 */
  @GetMapping("/calc")
  public String calc() {
    return "calc.html";
  }

}