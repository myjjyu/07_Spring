package kr.gilju.database.services;

import java.util.List;

import kr.gilju.database.exceptions.ServiceNoResultException;
import kr.gilju.database.models.Member;

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
}
