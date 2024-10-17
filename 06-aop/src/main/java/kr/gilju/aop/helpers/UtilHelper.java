package kr.gilju.aop.helpers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 유틸리티 기능을 제공하는 클래스
 */
@Component // 스프링에게 이 클래스가 빈(bean)임을 알려줌
public class UtilHelper {
  /** 싱클톤 객체 */
  // private static UtilHelper current;

  // public static UtilHelper getInstance() {
  //   if (current == null) {
  //     current = new UtilHelper();
  //   }

  //   return current;
  // }

  // private UtilHelper() {
  //   super();
  // }

  /**
   * 랜덤 숫자를 생성하는 메서드
   * 
   * @param min 최소값
   * @param max 최대값
   * @return 생성된 랜덤 숫자
   */
  public int random(int min, int max) {
    int num = (int) ((Math.random() * (max - min + 1)) + min);
    return num;
  }

  /**
   * 클라이언트의 ip주소를 가져오는 메서드
   * 
   * @param request HttpServletRequest객체
   * @return ip주소
   */
  public String getClientIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");

    if (ip == null) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null) {
      ip = request.getHeader("WL-Proxy-Client-IP"); // 웹로직
    }
    if (ip == null) {
      ip = request.getHeader("HTTP_CLIENT_IP");
    }
    if (ip == null) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (ip == null) {
      ip = request.getRemoteAddr();
    }

    return ip;
  }

  /**
   * 쿠키값을 저장한다
   * 
   * @param response HttpServletResponse객체
   * @param name     쿠키이름
   * @param value    쿠키값
   * @param maxAge   쿠키 유효 시간(0이면 지정안함, 음수일 경우 즉시 삭제)
   * @param domain   쿠키 도메인
   * @param path     쿠키경로
   */
  public void writeCookie(HttpServletResponse response, String name, String value, int maxAge, String domain,
      String path) {
    if (value != null && !value.equals("")) {
      try {
        value = URLEncoder.encode(value, "utf-8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }

    Cookie cookie = new Cookie(name, value);
    cookie.setPath(path);

    if (domain != null) {
      cookie.setDomain(domain);
    }

    if (maxAge != 0) {
      cookie.setMaxAge(maxAge);
    }

    response.addCookie(cookie);
  }

  /**
   * 쿠키값을 저장한다.path값을 "/"로 강제 설정한다
   * 
   * @param response HttpServletResponse객체
   * @param name     쿠키이름
   * @param value    쿠키값
   * @param maxAge   쿠키 유효 시간(0이면 지정안함, 음수일 경우 즉시 삭제)
   * @param domain   쿠키 도메인
   * @param path     쿠키경로
   * 
   * @see #writeCookie(HttpServletResponse,String,String,int,String,String)
   */
  public void writeCookie(HttpServletResponse response, String name, String value, int maxAge, String domain) {
    this.writeCookie(response, name, value, maxAge, domain, "/");
  }

  /**
   * 쿠키값을 저장한다.path값을 "/"로 domain을 null로 강제 설정한다
   * 
   * @param response HttpServletResponse객체
   * @param name     쿠키이름
   * @param value    쿠키값
   * @param maxAge   쿠키 유효 시간(0이면 지정안함, 음수일 경우 즉시 삭제)
   * @param domain   쿠키 도메인
   * @param path     쿠키경로
   * 
   * @see #writeCookie(HttpServletResponse,String,String,int,String,String)
   */

  public void writeCookie(HttpServletResponse response, String name, String value, int maxAge) {
    this.writeCookie(response, name, value, maxAge, null, "/");
  }

  /**
   * 쿠키값을 저장한다.path값을 "/"로 domain을 null로, maxAge를 0으로 강제 설정한다
   * 
   * @param response HttpServletResponse객체
   * @param name     쿠키이름
   * @param value    쿠키값
   * @param maxAge   쿠키 유효 시간(0이면 지정안함, 음수일 경우 즉시 삭제)
   * @param domain   쿠키 도메인
   * @param path     쿠키경로
   * 
   * @see #writeCookie(HttpServletResponse,String,String,int,String,String)
   */
  public void writeCookie(HttpServletResponse response, String name, String value) {
    this.writeCookie(response, name, value, 0, null, "/");
  }

  /**
   * 쿠키값을 삭제한다
   * 
   * @param response HttpServletResponse 객체
   * @param name     쿠키이름
   */
  public void deleteCookie(HttpServletResponse response, String name) {
    this.writeCookie(response, name, null, -1, null, "/");
  }
}