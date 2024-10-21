package kr.gilju.database.models;

import lombok.Data;

/**
 * Department 테이블의 구조를 정의하는 클래스
 */
@Data
public class Department {
  private int deptno;  // 학과번호
  private String dname; // 학과이름
  private String loc; //학과위치
}
