package kr.gilju.myshop.models;

import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Member 테이블의 구조를 정의하는 클래스
 */

/*
 * 세션에 저장할 클래스 타입은 Serializable 인터페이스를 상속해야한다 ==> 이진수로 바꾸는법(순서대로 차곡차곡 만들어랏)
 * 즉 Serializable 는 ==> 객채직렬화 임
 * implements Serializable 바이트 배열로 바꿔주는 역할을함
 */

 
@Data
public class Member implements Serializable {
  private int id; // 일련번호 (Primary Key)
  private String user_id; // 아이디
  private String user_pw; // 비밀번호(암호화저장)
  private String user_name; // 회원이름
  private String email; // 이메일
  private String phone; // 연락처
  private String birthday; // 생년월일
  private String gender; // 성별 (M=남자, F=여자)
  private String postcode; // 우편번호
  private String addr1; // 검색된주소
  private String addr2; // 나머지주소
  private String photo; // 프로필사진 정보
  private String is_out; // 탈퇴 여부 (Y/N)
  private String is_admin; // 관리자 여부 (Y/N)
  private String login_data; // 마지막 로그인 일시
  private String reg_data; // 등록일시
  private String edit_date; // 변경일시


  //------ 여기 추가 후 재 로그인 필요
  private String new_user_pw; // 회원정보 수정에서 사용할 신규 비밀번호

  @Getter
  @Setter
  private static int listCount = 0;

  @Getter
  @Setter
  private static int offset = 0;
}
