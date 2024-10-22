package kr.gilju.database.services;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.gilju.database.exceptions.ServiceNoResultException;
import kr.gilju.database.models.Member;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class MemberServiceTest {
  @Autowired
  private MemberService memberService;

  @Test
  @DisplayName("회원 추가 테스트")
  void insertMember() throws Exception {
    Member member = new Member();
    member.setUser_id("스프링");
    member.setUser_pw("스터디");
    member.setUser_name("헬로월드");
    member.setEmail("abcd@naver.com");
    member.setPhone("010-2345-0000");
    member.setBirthday("2011-06-06");
    member.setGender("F");
    member.setPostcode("2345");
    member.setAddr1("서울시 서초구");
    member.setAddr2("테스트동");
    member.setPhoto(null);
    member.setIs_out("N");
    member.setIs_admin("N");
    member.setLogin_data("2011-11-11");
    member.setReg_data("2011-11-11");
    member.setEdit_date("2011-11-11");

   Member result = null;

    try {
      result = memberService.addItem(member);
    } catch (ServiceNoResultException e) {
      log.error("SQL문 처리 결과 없음", e);
    } catch (Exception e) {
      log.error("Mapper 구현 에러", e);
      throw e;
    }

    if (result != null) {
      log.debug("result: " + result);
      log.debug("new User_name: " + member.getUser_name());
    }
  }

  @Test
  @DisplayName("회원 수정 테스트")
  void updateMember() throws Exception {
    Member member = new Member();
    member.setId(10);
    member.setUser_id("스프링");  
    member.setUser_pw("songgilju");
    member.setUser_name("메가스터디");
    member.setEmail("test@naver.com");
    member.setPhone("010-0000-0000");
    member.setBirthday("2021-06-01");  
    member.setGender("F");              
    member.setPostcode("12345");
    member.setAddr1("서울시 테스트구");
    member.setAddr2("태스트동");
    member.setPhoto(null);  
    member.setIs_admin("N");           
    member.setLogin_data("2011-11-11"); 
    member.setReg_data("2011-11-11");   
    member.setEdit_date("2011-11-11");  

    Member result = null;

    try {
      result = memberService.editItem(member);
    } catch (ServiceNoResultException e) {
      log.error("SQL문 처리 결과 없음", e);
    } catch (Exception e) {
      log.error("Mapper 구현 에러", e);
      throw e;
    }

    if (result != null) {
      log.debug("result: " + result.toString());
    }
  }

  @Test
  @DisplayName("회원 삭제 테스트")
  void deleteMember() throws Exception {
    Member member = new Member();
    member.setId(9);

    try {
      memberService.deleteItem(member);
    } catch (ServiceNoResultException e) {
      log.error("SQL문 처리 결과 없음", e);
    } catch (Exception e) {
      log.error("Mapper 구현 에러", e);
      throw e;
    }
  }

  @Test
  @DisplayName("하나의 회원 조회 테스트")
  void selectOneMember() throws Exception {
    Member member = new Member();
    member.setId(3);

    Member result = null;

    try {
      result = memberService.getItem(member);
    } catch (ServiceNoResultException e) {
      log.error("SQL문 처리 결과 없음", e);
    } catch (Exception e) {
      log.error("Mapper 구현 에러", e);
      throw e;
    }

    if (result != null) {
      log.debug("result: " + result.toString());
    }
  }

  @Test
  @DisplayName("회원 목록 조회 테스트")
  void selectListMember() throws Exception {
    List<Member> output = null;

    Member input = new Member();
    input.setUser_id("헬로");

    try {
      output = memberService.getList(input);
    } catch (ServiceNoResultException e) {
      log.error("SQL문 처리 결과 없음", e);
    } catch (Exception e) {
      log.error("Mapper 구현 에러", e);
      throw e;
    }

    if (output != null) {
      for (Member item : output) {
        log.debug("result: " + item.toString());
      }
    }
  }
}