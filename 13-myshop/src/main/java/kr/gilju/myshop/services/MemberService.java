package kr.gilju.myshop.services;

import java.util.List;

import kr.gilju.myshop.models.Member;

public interface MemberService {

  public Member addItem(Member params) throws Exception;

  public Member editItem(Member params) throws Exception;

  public int deleteItem(Member params) throws Exception;

  public Member getItem(Member params) throws Exception;

  public List<Member> getList(Member params) throws Exception;

  public void isUniqueUserId(String user_id) throws Exception;

  public void isUniqueEmail(Member input) throws Exception;

  public Member findId(Member input) throws Exception;

  public void resetPw(Member input) throws Exception;

  public Member login(Member input) throws Exception;

  public int out(Member input) throws Exception;

  public List<Member> processOutMembers() throws Exception;
}
