package kr.gilju.myshop;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.gilju.myshop.mappers.MemberMapper;
import kr.gilju.myshop.models.Member;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class MembersMapperTest {

    @Autowired
    private MemberMapper memberMapper;

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
  void resetpw(){
    Member input = new Member();
    input.setUser_id("rlfwn12");  
    input.setUser_pw("song1004");
    input.setEmail("song@hanmail.net");

    int output = memberMapper.resetPw(input);

    log.debug("output: " + output);
  }
  

}