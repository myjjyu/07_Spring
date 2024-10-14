package kr.gilju.logger;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import ua_parser.Client;
import ua_parser.Parser;

 
@Slf4j // log객체 생성
@Controller // 컨트롤러로 사용
public class HomeController {


  @GetMapping("/")
  public String helloworld(Model model, HttpServletRequest request) {
    /** 접근한 웹 브라우저의 IP */
    // 출처: https://velog.io/@chullll/spring-boot-IPv4-%EC%84%A4%EC%A0%95 String ip =
    String ip = request.getHeader("X-Forwarded-For");
    if (ip == null) {
      ip = request.getHeader("Proxy-Client-IP");
      log.info(">>>> Proxy-Client-IP : " + ip);
    }
    if (ip == null) {
      ip = request.getHeader("WL-Proxy-Client-IP"); // 웹로직 
      log.info(">>>> WL-Proxy-Client-IP : " + ip);
    }
    if (ip == null) {
      ip = request.getHeader("HTTP_CLIENT_IP");
      log.info(">>>> HTTP_CLIENT_IP : " + ip);
    }
    if (ip == null) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
      log.info(">>>> HTTP_X_FORWARDED_FOR : " + ip);
    }
    if (ip == null) {
      ip = request.getRemoteAddr();
    }

    model.addAttribute("clientIp", ip);
    log.debug(">>>>>> Client IP : ip");


   /** 접근한 웹 브라우저의 UserAgent값 얻기 */
    String ua = request.getHeader("user-agent"); 
    model.addAttribute("ua", ua);
    log.debug(">>>>>> user-agent : ua");

    Parser uaParser =new Parser();
    Client c = uaParser.parse(ua);

    model.addAttribute("uac", c.toString());
    log.debug(">>>>>> user-agent : " + c.toString());

/** 브라우저 정보를 model 객체에 추가*/ 
model.addAttribute("browserFamily", c.userAgent.family);
model.addAttribute("browserMajor", c.userAgent.major);
model.addAttribute("browserMinor", c.userAgent.minor);

log.debug("browserFamily : " +c.userAgent.family);
log.debug("browserMajor: " + c.userAgent.major);
log.debug("browserMinor : " + c.userAgent.minor);

/** OS정보를  model 객체에 추가*/ 
model.addAttribute("osFamily", c.os.family);
model.addAttribute("osMajor", c.os.major);
model.addAttribute("osMinor", c.os.minor);

log.debug("osFamily : " +c.os.family);
log.debug("osMajor: " + c.os.major);
log.debug("osMinor : " + c.os.minor);

// 디바이스 정보를 model 객체에 추가
model.addAttribute("deviceFamily", c.device.family);

log.debug("deviceFamily : " + c.device.family);

// 웹 브라우저가 컨트롤러에 전달한 요청확인
String url = request.getRequestURL().toString();

//접속방식 조회
String methodName = request.getMethod();

//url에서 "?" 이우헤 전달되는 get파라미터 문자열을 모두 가져온다
String queryString = request.getQueryString();

//가져온갓ㅂ이 있다면 url과 결함하여 완전한 url을 구성한다 
if(queryString != null){
  url = url + "?" + queryString;
}

model.addAttribute("method", methodName);
model.addAttribute("url", url);

return "index";
  }
}
