package kr.gilju.database.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Professor 테이블의 구조를 정의하는 클래스
 */
@Data
public class Professor {
  private int profno; // 교수 번호
  private String name; // 교수이름
  private String userid; // 교수아이디
  private String position; // 위치/직책
  private int sal; // 교수 급여
  private String hiredate; // 입사날짜
  private Integer comm; // 성과금 / 숫자, null 둘다 가능
  private int deptno; // 학과번호

  
  private String dname; // 학과명(조인을 통해 조합된 값)

  @Getter
  @Setter
  private static int listCount = 0;

  @Getter
  @Setter
  private static int offset = 0;
}
