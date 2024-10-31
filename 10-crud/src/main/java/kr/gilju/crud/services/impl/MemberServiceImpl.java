package kr.gilju.crud.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.gilju.crud.exceptions.ServiceNoResultException;
import kr.gilju.crud.mappers.MemberMapper;
import kr.gilju.crud.models.Member;
import kr.gilju.crud.services.MemberService;

/**
 * 학과 관리 기능과 관련된 MyBatis Mapper를 간접적으로 호출하기 위한 기능 명세
 * 
 * 1) 모든 메서드를 재정의 한 직후 리턴값 먼저 정의
 */

 @Service
public class MemberServiceImpl implements MemberService {


    /** sql문을 구현하고 있는 mapper객체 주입 */
    @Autowired
    private MemberMapper memberMapper;


    @Override
    public Member addItem(Member input) throws ServiceNoResultException, Exception {
    int rows = memberMapper.insert(input);

    if (rows == 0) {
        throw new ServiceNoResultException("저장된 데이터가 없습니다");
    }
    
    return memberMapper.selectItem(input);
    }

    @Override
    public Member editItem(Member input) throws ServiceNoResultException, Exception {
    int rows = memberMapper.update(input);

    if (rows == 0) {
        throw new ServiceNoResultException("수정된 데이터가 없습니다");
    }
    
    return memberMapper.selectItem(input);
    }

    @Override
    public int deleteItem(Member input) throws ServiceNoResultException, Exception {

        int rows = memberMapper.delete(input);

        if (rows == 0) {
            throw new ServiceNoResultException("삭제된 데이터가 없습니다");
        }

        return rows;
    }

    @Override
    public Member getItem(Member input) throws ServiceNoResultException, Exception {
      Member output = memberMapper.selectItem(input);

        if (output == null) {
            throw new ServiceNoResultException("조회된 데이터가 없습니다");
        }

        return output;
    }

    @Override
    public List<Member> getList(Member input) throws ServiceNoResultException, Exception {
        return memberMapper.selectList(input);
    }
}