package kr.gilju.myshop.controllers.restcontrollers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.gilju.myshop.helpers.RestHelper;
import kr.gilju.myshop.services.MemberService;

@RestController
public class AccountRestController {
  @Autowired
  private RestHelper restHelper;

  @Autowired
  private MemberService memberService;

  @GetMapping("/api/account/id_unique_check")
  public Map<String, Object> idUniqueCheck(@RequestParam("user_id") String user_id) {
    try {
      memberService.isUniqueUserId(user_id);
    } catch (Exception e) {
      return restHelper.badRequest(e);
    }
    return restHelper.sendJson();
  }

  @GetMapping("/api/account/email_unique_check")
  public Map<String, Object> emailUniqueCheck(@RequestParam("email") String email) {
    try {
      memberService.isUniqueUserId(email);
    } catch (Exception e) {
      return restHelper.badRequest(e);
    }
    return restHelper.sendJson();
  }
}
