package kr.gilju.interceptor.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyController {

  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("where", "인덱스 페이지");
    return "myview";
  }

  @GetMapping("/hello")
  public String hello(Model model) {
    model.addAttribute("where", "hello 페이지");
    return "myview";
  }

  @GetMapping("/hi")
  public String hi(Model model) {
    model.addAttribute("where", "hi 페이지");
    return "myview";
  }

  @GetMapping("/bye")
  public String bye(Model model) {
    model.addAttribute("where", "bye 페이지");
    return "myview";
  }
}
