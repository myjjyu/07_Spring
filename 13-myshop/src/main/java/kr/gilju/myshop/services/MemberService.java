package kr.gilju.myshop.services;

import java.util.List;

import kr.gilju.myshop.exceptions.ServiceNoResultException;
import kr.gilju.myshop.models.Member;

public interface MemberService {
  /**
   * 
   * @param params
   * @return
   * @throws ServiceNoResultException
   * @throws Exception
   */
  public Member addItem(Member params) throws ServiceNoResultException, Exception;

  /**
   * 
   * @param params
   * @return
   * @throws ServiceNoResultException
   * @throws Exception
   */
  public Member editItem(Member params) throws ServiceNoResultException, Exception;

  /**
   * 
   * @param params
   * @return
   * @throws ServiceNoResultException
   * @throws Exception
   */
  public int deleteItem(Member params) throws ServiceNoResultException, Exception;

  /**
   * 
   * @param params
   * @return
   * @throws ServiceNoResultException
   * @throws Exception
   */
  public Member getItem(Member params) throws ServiceNoResultException, Exception;

  /**
   * 
   * @param params
   * @return
   * @throws ServiceNoResultException
   * @throws Exception
   */
  public List<Member> getList(Member params) throws ServiceNoResultException, Exception;

  /**
   * 
   * @param userId
   * @throws Exception
   */
  public void isUniqueUserId(String user_id) throws Exception;

  /**
   * 
   * @param email
   * @throws Exception
   */
  public void isUniqueEmail(String email) throws Exception;

  /**
   * 아이디 찾기
   * @param input
   * @return
   * @throws Exception
   */
  public Member findId(Member input) throws Exception;


  /**
   * 비밀번호 재 발급
   * @param input
   * @throws Exception
   */
  public void resetPw(Member input) throws Exception;

  /**
   * 로그인 서비스
   * @param input
   * @throws Exception
   */
  public Member login(Member input) throws Exception;

}
