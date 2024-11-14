package kr.gilju.myshop;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.gilju.myshop.mappers.MemberMapper;
import kr.gilju.myshop.models.Member;
import kr.gilju.myshop.services.MemberService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class MembersServiceTest {

  @Autowired
  private MemberMapper memberMapper;

  @Autowired
  private MemberService memberService;

  @Test
  @DisplayName("회원 추가 테스트")
  void insertMembers() {
    Member input = new Member();
    input.setUser_id("헬로");
    input.setUser_pw("월드");
    input.setUser_name("메가");
    input.setEmail("abcd@naver.com");
    input.setPhone("010-1234-5678");
    input.setBirthday("2021-06-01");
    input.setGender("F");
    input.setPostcode("12345");
    input.setAddr1("서울시 강남구");
    input.setAddr2("테스트동");
    input.setPhoto(null);

    int output = memberMapper.insert(input);

    // 저장된 데이터의 수
    log.debug("output: " + output);
    // 생성된 pk값
    log.debug("new member id: " + input.getId());
  }

  @Test
  @DisplayName("회원 수정 테스트")
  void updateMembers() {
    Member input = new Member();
    input.setUser_id("헬로");
    input.setUser_pw("메가");
    input.setUser_name("스터디");
    input.setEmail("test@naver.com");
    input.setPhone("010-1234-1234");
    input.setBirthday("2021-06-01");
    input.setGender("F");
    input.setPostcode("12345");
    input.setAddr1("서울시 서초구");
    input.setAddr2("행복동");
    input.setPhoto(null);

    int output = memberMapper.update(input);

    log.debug("output: " + output);
  }

  @Test
  @DisplayName("회원 삭제 테스트")
  void deleteMembers() {
    Member input = new Member();
    input.setId(2);

    int output = memberMapper.delete(input);

    log.debug("output: " + output);
  }

  @Test
  @DisplayName("하나의 회원 조회 테스트")
  void selectOneMember() {
    Member input = new Member();
    input.setId(3);

    Member output = memberMapper.selectItem(input);
    log.debug("output: " + output.toString());
  }

  @Test
  @DisplayName("회원 목록 조회 테스트")
  void selectListMembers() {
    List<Member> output = memberMapper.selectList(null);

    for (Member item : output) {
      log.debug("output: " + item.toString());
    }
  }

  @Test
  @DisplayName("비밀번호 재발급 테스트")
  void resetpw() {
    Member input = new Member();
    input.setUser_id("rlfwn12");
    input.setUser_pw("song1004");
    input.setEmail("song@hanmail.net");

    int output = memberMapper.resetPw(input);

    log.debug("output: " + output);
  }

  @Test
  @DisplayName("로그인 테스트")
  void login() {
    Member input = new Member();
    input.setId(26);
    input.setUser_id("hello123");
    input.setUser_pw("rlfwn123");

    Member output = memberMapper.login(input);
    log.debug("output: " + output.toString());
  }

  @Test
  @DisplayName("현재시간 업데이트 테스트")
  void update() {
    Member input = new Member();
    input.setId(26);

    int output = memberMapper.updateLoginDate(input);

    log.debug("output: " + output);
  }

  @Test
  @DisplayName("회원탈퇴 테스트")
  void out() {
    Member input = new Member();
    input.setId(28);
    input.setUser_id("song123");
    input.setUser_pw("123");

    int output = memberMapper.out(input);
    log.debug("output: " + output);
  }

  @Test
  @DisplayName("탈퇴한 회원삭제")
  void deleteMemberTest() {
    int output = memberMapper.deleteOutMembers();

    log.debug("output: " + output);
  }

  @Test
  @DisplayName("탈퇴한 회원삭제 테스트")
  void processOutMembers() throws Exception {
    List<Member> output = null;

    try {
      output = memberService.processOutMembers();
    } catch (Exception e) {
      log.error("-----서비스 구현 에러----");
      throw e;
    }

    if (output != null) {
      for (Member item : output) {
        log.debug("output : " + item.toString());
      }
    }
  }

  @Test
  @DisplayName("아이디 이메일 중복체크 테스트")
  void selectCountTest() {

    Member input = new Member();
    input.setEmail("rlfwn528@gmail.com");
    input.setId(24);

    int output = memberMapper.selectCount(input);
    log.debug("output: " + output);

    // 중복이면 0 중복아니면 1
  }

  @Test
  @DisplayName("내 정보수정 업데이트 테스트")
  void updateTest() {
    // 테스트용 input 데이터
    Member input = new Member();
    input.setUser_name("rlfwn0528");
    input.setNew_user_pw("12345");
    input.setEmail("rlfwn528@gmail.com");
    input.setPhone("01050921795");
    input.setBirthday("2024-10-31");
    input.setGender("F");
    input.setPostcode("06035");
    input.setAddr1("서울 강남구 가로수길 5 (신사동)");
    input.setAddr2("12345");
    input.setId(33);

    // 새로운 비밀번호를 설정할 경우 현재비밀번호 적기
    input.setNew_user_pw("1234");

    // memberMapper.update 호출
    int output = memberMapper.update(input);

    // 반환값 확인
    log.debug("output: " + output);
  }
}