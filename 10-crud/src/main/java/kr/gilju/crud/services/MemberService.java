package kr.gilju.crud.services;

import java.util.List;

import kr.gilju.crud.exceptions.ServiceNoResultException;
import kr.gilju.crud.models.Member;

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
