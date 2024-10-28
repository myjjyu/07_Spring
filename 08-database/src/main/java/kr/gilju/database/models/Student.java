package kr.gilju.database.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Student 테이블의 구조를 정의하는 클래스
 */
  @Data
  public class Student {
    
    private int studno; // 학번
    private String name; // 이름
    private String userid; // 아이디 (ex: ansel414)
    private int grade; // 학년
    private String idnum; // 주민번호 (ex: 123456-1234567)
    private String birthdate; // 생일 
    private String tel; // 전화번호 (ex: 055-261-8947)
    private int height; // 키
    private int weight; // 몸무게
    private int deptno; // 학과
    private Integer profno; // 담당 교수 번호

    private String dname; //학과명(조인을 통해 조합된 값)
    private String pName; //교수이름

    @Getter
    @Setter
    private static int listCount = 0;

    @Getter
    @Setter
    private static int offset = 0;
  }
  


