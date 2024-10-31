package kr.gilju.crud.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Department 테이블의 구조를 정의하는 클래스
 */
@Data
public class Department {
  private int deptno; // 학과번호
  private String dname; // 학과이름
  private String loc; // 학과위치

  /**
   * 한 페이지에 표시될 목록 수 
   * mysql 의 limit 절에서 사용할 값이므로 beans에 추가한다
   * 
   * 1) staic 변수로 선언하여 모든 객체가 공유한다
   * 2) staic 변수는 객체를 생성하지 않고도 사용할수 있다
   * 3) staic 변수에 lombok을 적용하려면
   * @Getter, @Setter를 개별적으로 적용한다
   */
  @Getter
  @Setter
  private static int listCount = 0;


  /**
   * mysql의 limit 절에 사용될 offset값
   * mysql의 limit 절에서 사용할 값이므로 beans에 추가한다 
   * 
   * offset위치부터 listCount만큼의 데이터를 가져온다
   */
  @Getter
  @Setter
  private static int offset = 0;
}
