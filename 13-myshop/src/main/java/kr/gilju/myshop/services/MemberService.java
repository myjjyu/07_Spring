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
   * 
   * @param input
   * @return
   * @throws Exception
   */
  public Member findId(Member input) throws Exception;
}
