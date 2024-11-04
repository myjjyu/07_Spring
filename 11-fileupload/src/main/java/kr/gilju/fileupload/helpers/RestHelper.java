package kr.gilju.fileupload.helpers;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class RestHelper {

  @Autowired
  private HttpServletResponse response;

  /**
   * Json 형식의 응답을 출력하기 위한 메서드
   * 
   * @param status  http 상태코드
   * @param message 결과메시지
   * @param data    json으로 변환할 데이터 컬렉션
   * @param error   에러메시지
   * @return Map<String, Object>
   */
  public Map<String, Object> sendJson(int status, String message, Map<String, Object> data, Exception error) {

    // 1) 제이슨 형식 출력을 위한 http 헤더 설정
    // 제이슨 형식임을 명시함
    response.setContentType("application/json; charset=UTF-8");
    // http 상태코드 설정 (200, 400, 500등 )
    response.setStatus(status);

    // cors 허용
    // 보안에 좋지 않기 때문에 이 옵션을 허용할 경우 인증키 등의 보안장치가 필요함
    // 여기에서는 실습을 위해 허용함
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE");
    response.setHeader("Access-Control-Max-Age", "3600");
    response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
    response.setHeader("Access-Control-Allow-Origin", "*");

    // 2) 제이슨으로 변환될 map객체 구성
    Map<String, Object> result = new LinkedHashMap<String, Object>();

    result.put("timestamp", LocalDateTime.now().toString());
    result.put("status", status);
    result.put("message", message);

    // data 가 전달되었다면 result 에 병합한다
    if (data != null) {
      result.putAll(data);
    }

    // error가 전달되었다면 result에 포함한다
    if (error != null) {
      result.put("error", error.getClass().getName());
      result.put("message", error.getMessage());

      // printStackTrace() 의 출력 내용을 문자열로 반환 받는다
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      PrintStream ps = new PrintStream(bos);
      error.printStackTrace(ps);

      String trace = bos.toString();
      result.put("trace", trace);
    }
    return result;
  }

  /**
   * json 형식의 응답을 출력하기 위한 메서드
   * 
   * @param data json 으로 변환할 데이터 컬렉션
   * @return Map<String, Object>
   */
  public Map<String, Object> sendJson(Map<String, Object> data) {
    return this.sendJson(200, "OK", data, null);
  }

  /**
   * json 형식의 응답을 출력하기 위한 메서드
   * 특별한 결과값 없이 요청에 대한 성공 여부만을 알리기 위해 사용한다
   * 
   * @return Map<String, Object>
   */
  public Map<String, Object> sendJson() {
    return this.sendJson(200, "OK", null, null);
  }

  /**
   * 에러 상황을 json형식으로 출력하기 위한 메서드
   * 
   * @param status  http 상태코드
   * @param message 결과 메시지
   * @param error   에러 이름
   * @return Map<String, Object>
   */
  public Map<String, Object> sendError(int status, String message) {
    Exception error = new Exception(message);
    return this.sendJson(status, null, null, error);
  }

  /**
   * json 형식으로 에러메시지를 리턴한다
   * http 상태코드는 400 으로설정하고, 결과 메시지는 파라미터로 전달되는 값을 설정한다
   * 파라미터 유효성 검사 실패등의 경우에 사용한다
   * ex) 로그인 잘못입력시 등등
   * 
   * @param message 에러메시지
   * @return Map<String, Object>
   */
  public Map<String, Object> badRequest(String message) {
    return this.sendError(400, message);
  }

  /**
   * json 형식으로 에러메시지를 리턴한다
   * http 상태코드는 400 으로설정하고, 결과 메시지는 파라미터로 전달되는 error객체를 사용한다
   * 파라미터 유효성 검사 실패등의 경우에 사용한다
   * ex) 로그인 잘못입력시 등등
   * 
   * @param error 에러객체
   * @return Map<String, Object>
   */
  public Map<String, Object> badRequest(Exception error) {
    return this.sendJson(400, null, null, error);
  }

  /**
   * json 형식으로 에러메시지를 리턴한다
   * http 상태코드는 500 으로설정하고, 결과 메시지는 파라미터로 전달되는 값을 설정한다
   * 400에러 이외희 모든 경우에 사용한다.
   * 주로 DB연동 등의 처리에서 발생한 에러를 처리한다
   * 
   * @param message 에러메시지
   * @return Map<String, Object>
   */
  public Map<String, Object> serverError(String message) {
    return this.sendError(500, message);
  }

  /**
   * json 형식으로 에러메시지를 리턴한다
   * http 상태코드는 500 으로설정하고, 결과 메시지는 파라미터로 전달되는 error객체를 사용한다
   * 400에러 이외희 모든 경우에 사용한다.
   * 주로 DB연동 등의 처리에서 발생한 에러를 처리한다
   * 
   * @param error 에러객체
   * @return Map<String, Object>
   */
  public Map<String, Object> serverError(Exception error) {
    return this.sendJson(500, null, null, error);
  }
}