package kr.gilju.aop.services;

public interface MyCalcService {
  /**
   * 두개의 정수를 더하는 메서드
   * 
   * @param x -첫번째 정수
   * @param y - 두번째 정수
   * @return int
   */
  public int plus(int x, int y);

  /**
   * 두개의 정수를 더하는 메서드
   * 
   * @param x -첫번째 정수
   * @param y - 두번째 정수
   * @return int
   */
  public int minus(int x, int y);
}
