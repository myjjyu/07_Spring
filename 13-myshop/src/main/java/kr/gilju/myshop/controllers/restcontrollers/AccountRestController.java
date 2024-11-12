package kr.gilju.myshop.controllers.restcontrollers;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.gilju.myshop.helpers.FileHelper;
import kr.gilju.myshop.helpers.MailHelper;
import kr.gilju.myshop.helpers.RestHelper;
import kr.gilju.myshop.helpers.UtilHelper;
import kr.gilju.myshop.models.Member;
import kr.gilju.myshop.models.UploadItem;
import kr.gilju.myshop.services.MemberService;

@RestController
public class AccountRestController {
  @Autowired
  private RestHelper restHelper;

  @Autowired
  private FileHelper fileHelper;

  @Autowired
  private UtilHelper utilHelper;

  @Autowired
  private MailHelper mailHelper;

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

  @PostMapping("/api/account/join")
  public Map<String, Object> join(
      @RequestParam("user_id") String user_id,
      @RequestParam("user_pw") String user_pw,
      @RequestParam("user_name") String user_name,
      @RequestParam("email") String email,
      @RequestParam("phone") String phone,
      @RequestParam("birthday") String birthday,
      @RequestParam("gender") String gender,
      @RequestParam("postcode") String postcode,
      @RequestParam("addr1") String addr1,
      @RequestParam("addr2") String addr2,
      @RequestParam(value = "photo", required = false) MultipartFile photo) {
    /** 1) 입력값에 대한 유효성 검사 */
    // 여기서는 생략!!!

    /** 2) 아이디 중복 검사 */
    try {
      memberService.isUniqueUserId(user_id);
    } catch (Exception e) {
      return restHelper.badRequest(e);
    }

    /** 3) 이메일 중복 검사 */
    try {
      memberService.isUniqueUserId(email);
    } catch (Exception e) {
      return restHelper.badRequest(e);
    }

    /** 4) 업로드 받기 */
    UploadItem uploadItem = null;

    try {
      uploadItem = fileHelper.saveMultipartFile(photo);
    } catch (NullPointerException e) {
      // 업로드된 항목이 있는 경우는 에러가 아니므로 계속 진행
    } catch (Exception e) {
      // 업로드된 항목이 있으나, 이를 처리하다가 에러가 발생한 경우
      return restHelper.serverError(e);
    }

    /** 5) 정보를 service에 전달하기 위한 객체 구성 */
    Member member = new Member();
    member.setUser_id(user_id);
    member.setUser_pw(user_pw);
    member.setUser_name(user_name);
    member.setEmail(email);
    member.setPhone(phone);
    member.setBirthday(birthday);
    member.setGender(gender);
    member.setPostcode(postcode);
    member.setAddr1(addr1);
    member.setAddr2(addr2);

    // 업로드 된 이미지의 이름을 표시할 필요가 없다면 저장된 경로만 DB에 저장하면 됨
    if (uploadItem != null) {
      member.setPhoto(uploadItem.getFilePath());
    }

    /** 6) DB에 저장 */

    try {
      memberService.addItem(member);
    } catch (Exception e) {
      return restHelper.serverError(e);
    }

    return restHelper.sendJson();
  }

  /**
   * 
   * @param user_name
   * @param email
   * @return
   */
  @PostMapping("/api/account/find_id")
  public Map<String, Object> findId(
      @RequestParam("user_name") String user_name,
      @RequestParam("email") String email) {

    Member input = new Member();
    input.setUser_name(user_name);
    input.setEmail(email);

    Member output = null;

    try {
      output = memberService.findId(input);
    } catch (Exception e) {
      return restHelper.serverError(e);
    }

    Map<String, Object> data = new LinkedHashMap<String, Object>();
    data.put("item", output.getUser_id());

    return restHelper.sendJson(data);
  }

  /**
   * 비밀번호 재 발급
   * 
   * @param user_name
   * @param email
   * @return
   */
  @PutMapping("/api/account/reset_pw")
  public Map<String, Object> resetPw(
      @RequestParam("user_id") String user_id,
      @RequestParam("email") String email) {

    /** 1) 임시 비빌번호를 DB에 갱신하기 */
    String newPassword = utilHelper.randomPassword(8);
    Member input = new Member();
    input.setUser_id(user_id);
    input.setEmail(email);
    input.setUser_pw(newPassword);

    try {
      memberService.resetPw(input);
    } catch (Exception e) {
      return restHelper.serverError(e);
    }


    /** 2) 이메일 발송을 위한 템플릿 처리 */
    // 메일 템플릿 파일경로
    ClassPathResource resource = new ClassPathResource("mail_templates/reset_pw.html");
    String mailTemplatePath = null;

    try {
      mailTemplatePath = resource.getFile().getAbsolutePath();
    } catch (IOException e) {
      return restHelper.serverError("메일 템플릿을 찾을 수 없습니다");
    }

    // 메일 템플릿 파일 가져오기
    String template = null;

    try {
      template = fileHelper.readString(mailTemplatePath);
    } catch (Exception e) {
      return restHelper.serverError("메일 템플릿을 읽을 수 없습니다");
    }

    // 메일 템플릿 안의 치환자 처리 
    template = template.replace("{{user_id}}", user_id);
    template = template.replace("{{password}}", newPassword);

    /** 3) 메일 발송 */
    String subject = user_id + "님의 비밀번호가 재설정 되엇습니다";

    try {
      mailHelper.sendMail(email, subject, template);
    } catch (Exception e) {
      return restHelper.serverError(e);
    }

    return restHelper.sendJson();
  }


  // 로그인
  // 삭제
  // ==> 단위테스트진행(썬더클라이언트)

  @PostMapping("/api/account/login")
  public Map<String, Object> login(
    // 세션을 사용해야 하므로 reqest 객체가 필요하다
    HttpServletRequest request,
    @RequestParam("user_id") String user_id,
    @RequestParam("user_pw") String user_pw) {
    /** 1) 입력값에 대한 유효성 검사 */
    // 여기서는 생략

    /** 2) 입력값을 빈즈객체에 저장 */
    Member input = new Member();
    input.setUser_id(user_id);
    input.setUser_pw(user_pw);

    /** 3) 로그인 시도 */
    Member output = null;

    try {
        output = memberService.login(input);
    } catch (Exception e) {
      output.setPhoto(fileHelper.getUrl(output.getPhoto()));
      return restHelper.serverError(e);
    }

    /** 4) 로그인에 성공했다면 회원정보를 세션에 저장한다 */
    HttpSession session = request.getSession();
    session.setAttribute("memberInfo", output);
   
    /** 5) 로그인이 처리되었음을 응답한다 */
    return restHelper.sendJson();
    }

    @GetMapping("/account/logout")
    public Map<String, Object> logout(HttpServletRequest request){
      HttpSession session = request.getSession();
      session.invalidate();
      return restHelper.sendJson();
    }
}
