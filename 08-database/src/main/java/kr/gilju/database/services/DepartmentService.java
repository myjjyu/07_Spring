package kr.gilju.database.services;

import java.util.List;

import kr.gilju.database.exceptions.ServiceNoResultException;
import kr.gilju.database.models.Department;

/**
 * 학과 관리 기능과 관련된 MyBatis Mapper를 간접적으로 호출하기 위한 기능명세
 * 하나의 처리를 위해서 두개 이상의 Mapper 기능을 호출할 필요가 있을 경우
 * 이 인터페이스의 구현체(Impl)을 통해서 처리한다
 * 
 * 1) delete 기능의 경우 삭제된 데이터의 수를 의미하는 int를 리턴
 * 2) 목록 조회 기능의 경우 List<DTO클래스> 를 리턴
 * 3) 입력, 수정, 상세조회의 경우는 DTO 클래스를 리턴
 * 4) 모든 메소드는 throws Exception을 기술하여 예외가 발생할 경우 호출한 쪽에서 처리하도록 유도한다
 */


public interface DepartmentService {
  /**
   * 학과 정보를 새로 저장하고 저장된 정보를 조회하여 리턴한다
   * @param input - 저장할 정보를 담고 있는 Beans
   * @return Department - 저장된 데이터
   * @throws MyBatisException - SQL처리에 실패한 경우
   * @throws ServiceNoResultException - 저장된 데이터가 없는 경우
   */
  public Department addItem(Department params) throws ServiceNoResultException, Exception;

  /**
   * 학과 정보를 수정하고 수정된 정보를 조회하여 리턴한다
   * @param input - 수정할 정보를 담고 있는 Beans
   * @return Department - 수정된 데이터
   * @throws MyBatisException - SQL처리에 실패한 경우
   * @throws ServiceNoResultException - 수정된 데이터가 없는 경우
   */
  public Department editItem(Department input) throws ServiceNoResultException, Exception;

  /**
   * 학과 정보를 삭제한다 . 삭제된 데이터의 수가 리턴된다
   * @param input - 삭제할 학과의 일련번호를 담고 있는 Beans
   * @return int - 삭제된 데이터 수
   * @throws MyBatisException - SQL처리에 실패한 경우
   * @throws ServiceNoResultException - 삭제된 데이터가 없는 경우
   */
  public int deleteItem(Department input) throws ServiceNoResultException, Exception;

  /**
   * 학과정보 단일행조회
   * 학과 정보를 조회한다. 조회된 데이터가 없는 경우 예외가 발생한다
   * @param input - 조회할 학과의 일련번호를 담고 있는 Beans
   * @return Department - 조회된 데이터
   * @throws MyBatisException - SQL처리에 실패한 경우
   * @throws ServiceNoResultException - 조회된 데이터가 없는 경우
   */
  public Department getItem(Department input) throws ServiceNoResultException, Exception;

  /**
   * 학과정보 다중행조회
   * 학과 정보를 조회한다. 조회된 데이터가 없는 경우 예외가 발생한다
   * @param input - 조회할 학과의 일련번호를 담고 있는 Beans
   * @return Department - 조회된 데이터
   * @throws MyBatisException - SQL처리에 실패한 경우
   * @throws ServiceNoResultException - 조회된 데이터가 없는 경우
   */
  public List<Department> getList(Department input) throws ServiceNoResultException, Exception;


  /**
   * 학과 목록에 대한 카운트 결과를 반환한다
   * 
   * @param input 조회할 학과의 학과번호를 담고있는 beans
   * @return Department -조회된 데이터
   * @throws Exception -sql 처리에 실패한 경우
   */
  public int getCount(Department input) throws Exception;
}