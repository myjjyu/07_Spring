package kr.gilju.myshop.models;

import lombok.Data;

/**
 * Member 테이블의 구조를 정의하는 클래스
 */
@Data
public class Member {
  private int id;                // 일련번호 (Primary Key)
  private String user_id;         // 아이디
  private String user_pw;         // 비밀번호(암호화저장)
  private String user_name;       // 회원이름
  private String email;          // 이메일
  private String phone;          // 연락처
  private String birthday;       // 생년월일
  private String gender;         // 성별 (M=남자, F=여자)
  private String postcode;       // 우편번호
  private String addr1;          // 검색된주소
  private String addr2;          // 나머지주소
  private byte[] photo;          // 프로필사진 정보
  private String is_out;          // 탈퇴 여부 (Y/N)
  private String is_admin;        // 관리자 여부 (Y/N)
  private String login_data;       // 마지막 로그인 일시
  private String reg_data;         // 등록일시
  private String edit_date;        // 변경일시
}
