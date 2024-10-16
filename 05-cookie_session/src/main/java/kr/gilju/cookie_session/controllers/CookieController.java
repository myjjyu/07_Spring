package kr.gilju.cookie_session.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import kr.gilju.cookie_session.helpers.UtilHelper;

@Controller
public class CookieController {
  /**
   * 쿠키 저장을 위한 작성 페이지
   */
  @GetMapping("/cookie/home")
  public String home(Model model,
      @CookieValue(value = "name", defaultValue = "") String myCookeiName,
      @CookieValue(value = "age", defaultValue = "0") int myCookeiAge) {

    /** 컨트롤러에서 쿠키를 식별하기 위한 처리 */
    try {
      // 저장시에 UnsupportedEncoding이 적용되었으므로 UnsupportedEncoding이 별도로 필요함
      myCookeiName = URLDecoder.decode(myCookeiName, "utf-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    // 추출한 값을 view에게 전달
    model.addAttribute("myCookieName", myCookeiName);
    model.addAttribute("myCookieAge", myCookeiAge);

    return "/cookie/home";
  }

  /**
   * 쿠키를 저장하기 위한 action 페이지
   */
  @PostMapping("/cookie/save")
  public String save(HttpServletResponse response,

      // cookie_name,cookie_time,cookie_var 는 html 네임 속성값임
      @RequestParam(value = "cookie_name", defaultValue = "") String cookieName,
      @RequestParam(value = "cookie_time", defaultValue = "0") int cookieTime,
      @RequestParam(value = "cookie_var", defaultValue = "") String cookieVar) {

    /** 1) 파라미터를 쿠키에 저장하기 위한 urlencoding 처리 */
    // 쿠키 저장을 위해서는 urlencoding 처리가 필요하다
    if (!cookieVar.equals("")) {
      try {
        // tyr,catch 문은 무조건 ▼ 구문 먼저 적고 command + . 눌러서 tyr,catch 문 생성
        cookieVar = URLEncoder.encode(cookieVar, "utf-8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }

    /** 2) 쿠키 저장하기 */
    // 개발자도구->애플리케이션->쿠키에서 시간설정해놓은 만큼 기록남았다가 삭제됨

    // 저장할 쿠키 객체 생성
    Cookie cookie = new Cookie(cookieName, cookieVar);

    // 유효경로 --> 사이트 전역에 대한 설정
    cookie.setPath("/");

    // 유효 도메인(로컬 개발시에는 설정할 필요 없음 )
    // --> "www.naver.com" 인 경우 ".naver.com"으로 설정
    // cookie.setDomain("localhost");

    // 유효시간 설정(0이하이면 즉시 삭제, 초 단위)
    // 설정하지 않을 경우 브라우저를 닫기 전까지 유지됨
    cookie.setMaxAge(cookieTime);

    // 쿠키저장
    response.addCookie(cookie);

    /** 2) 강제 페이지 이동 */
    // 이 페이지에 머물렀다는 사실이 웹 브라우저의 history에 남지 않는다
    // 즉 이 페이지에 머물렀다는 기록이 남으면 안되기 때문에 ▼ 구문을 설정해야함
    return "redirect:/cookie/home";
  }

  /**
   * 팝업창 제어 페이지
   */
  @GetMapping("/cookie/popup")
  public String popup(Model model,
      @CookieValue(value = "no-open", defaultValue = "") String noOpen) {

    // 쿠키값을 view에게 전달
    model.addAttribute("noOpen", noOpen);

    return "/cookie/popup";
  }

  @PostMapping("/cookie/popup_close")
  public String popupClose(HttpServletResponse response,
      @RequestParam(value = "no-open", defaultValue = "") String noOpen) {

        /** 1) 쿠키 저장하기 */
        // 60초 간 유요한 쿠키 설정
        // 실제 상용화 시에는 domain을 설정해야 한다
        UtilHelper.getInstance().writeCookie(response, "no-open", noOpen, 60);

        /** 2) 강제 페이지 이동 */
        return "redirect:/cookie/popup";

  }
}