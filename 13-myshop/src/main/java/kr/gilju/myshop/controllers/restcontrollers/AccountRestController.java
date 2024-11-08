package kr.gilju.myshop.controllers.restcontrollers;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kr.gilju.myshop.helpers.FileHelper;
import kr.gilju.myshop.helpers.RestHelper;
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
}
