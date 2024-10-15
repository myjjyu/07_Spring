package kr.gilju.params.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GetController {
  @GetMapping("/get/home")
  public String home() {
    return "get/home";
  }

  /** GET 방식의 파라미터를 전송받기 위한 컨트롤러 메서드 */
  @GetMapping("/get/result")
  public String result(Model model,
      // get파라미터 받기 ==> ?answer=100
      // 기본값이 필요없는 경우 `@RequestParam("answer") int myAnsewer` 로 사용 가능
      @RequestParam(value = "answer", defaultValue = "0") int myAnsewer) {

    String result = null;

    if (myAnsewer == 300) {
      result = "정답";
    } else {
      result = "오답";
    }

    // model 객체에 데이터를 담아서 전달
    model.addAttribute("result", result);
    model.addAttribute("myAnsewer", myAnsewer);

    return "get/result";
  }
}
