package kr.gilju.mailer.interceptors;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import io.micrometer.common.lang.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.gilju.mailer.helpers.UtilHelper;
import lombok.extern.slf4j.Slf4j;
import ua_parser.Client;
import ua_parser.Parser;


@Slf4j
@Component
@SuppressWarnings("null")
public class MyInterceptor implements HandlerInterceptor{
  /**페이지 실행 시작 시각을 저장할 변수 */
  long startTime = 0;

  /** 페이지의 실행 완료 시각을 저장할 변수 */
  long endTime = 0;


  /** 유틸헬퍼 객체를 자동 주입 받는다 */
  @Autowired
  private UtilHelper utilHelper;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
  throws Exception {

    log.info("----------------- new client connect -----------------");

  /** 1) 페이지 실행 시작 시각을 구한다  */
  startTime = System.currentTimeMillis();

  /** 2) 접속한 클라이언트 정보 확인하기  */
  String ua = request.getHeader("user-agent");
  Parser uaParser = new Parser();
  Client c = uaParser.parse(ua);

  String fmt = "[Client] %s, %s, %s, %s, %s, %s";

  String ipAddr = utilHelper.getClientIp(request);
  String osVersion = c.os.major + (c.os.minor != null ? "." + c.os.minor : "");
  String uaVersion = c.userAgent.major + (c.userAgent.minor != null ? "." + c.userAgent.minor : "");
  String clientInfo = String.format(fmt, ipAddr, c.device.family, c.os.family, osVersion, c.userAgent.family, uaVersion);

  log.info(clientInfo);

  /**3) 클라이언트의 요청 정보 확인하기 */

  String url = request.getRequestURL().toString();

  // get방식인지 post 방식인지 조회
  String methodName = request.getMethod();

  //url에서 "?"이후에 전달되는 get파라미터 문자열을 모두 가져온다 
  String queryString = request.getQueryString();

  // 가져온 값이 있다면 URL과 결합하여 완전한 URL을 구성한다 
  if (queryString != null) {
    url = url + "?" + queryString;
  }

  //획득한 정보를 로그로 표시한다 
  log.info(String.format("[%s] %s", methodName, url));

  /** 4) 클라이언트가 전달한 모든 파라미터 확인하기 */
  Map<String, String[]> params = request.getParameterMap();

  for(String key : params.keySet()) {
    String[] value = params.get(key);
    log.info(String.format("(parma)<---%s = %s", key, String.join(",", value)));
  }

  /**5) 클라이언트가 머물렀던 이전 페이지 확인학; */
  String referer = request.getHeader("referer");

  if(referer != null && endTime > 0) {
    log.info(String.format("- REFERER : time=%d, url=%s", (endTime - startTime), referer));
  }

  return HandlerInterceptor.super.preHandle(request,response,handler);
  }

 @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    endTime = System.currentTimeMillis();

    log.info(String.format("running time: %d(ms)", endTime-startTime));

    HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
  }

  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
  }
}
