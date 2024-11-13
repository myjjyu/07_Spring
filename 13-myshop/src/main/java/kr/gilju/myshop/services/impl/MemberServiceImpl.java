package kr.gilju.myshop.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.gilju.myshop.exceptions.ServiceNoResultException;
import kr.gilju.myshop.mappers.MemberMapper;
import kr.gilju.myshop.models.Member;
import kr.gilju.myshop.services.MemberService;
import lombok.extern.slf4j.Slf4j;

/**
 * 학과 관리 기능과 관련된 MyBatis Mapper를 간접적으로 호출하기 위한 기능 명세
 * 
 * 1) 모든 메서드를 재정의 한 직후 리턴값 먼저 정의
 */

@Slf4j

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

    /**
     * 아이디 검사 & 중복검사
     */
    @Override
    public void isUniqueUserId(String user_id) throws Exception {
        Member input = new Member();
        input.setUser_id(user_id);

        int output = 0;

        try {
            output = memberMapper.selectCount(input);

            if (output > 0) {
                throw new Exception("사용할 수 없는 아이디 입니다");
            }
        } catch (Exception e) {
            log.error("아이디 중복 검사에 실패했습니다", e);
            throw e;
        }
    }

    // 이메일 검사 & 중복검사
    @Override
    public void isUniqueEmail(String email) throws Exception {
        Member input = new Member();
        input.setEmail(email);

        int output = 0;

        try {
            output = memberMapper.selectCount(input);

            if (output > 0) {
                throw new Exception("사용할 수 없는 이메일 입니다");
            }
        } catch (Exception e) {
            log.error("이메일 중복 검사에 실패했습니다", e);
            throw e;
        }
    }

    // 조회한 아이디가 일치하는게 없으면 강제 에러냄
    // => 작성후 단위테스트
    // 아이디 찾기
    @Override
    public Member findId(Member input) throws Exception {
        Member output = null;

        try {
            output = memberMapper.findId(input);

            if (output == null) {
                throw new Exception("조회된 데이터가 없습니다");
            }
        } catch (Exception e) {
            log.error("아이디 검색에 실패했습니다", e);
            throw e;
        }
        return output;
    }

    /**
     * 비밀번호 재발급
     */
    @Override
    public void resetPw(Member input) throws Exception {
        int rows = 0;

        try {
            rows = memberMapper.resetPw(input);

            if (rows == 0) {
                throw new Exception("아이디와 이메일을 확인하세요");
            }
        } catch (Exception e) {
            log.error("Member 데이터 수정에 실패했습니다", e);
            throw e;
        }
    }

    /**
     * sql 이 에러라면 캐치로 건너뜀
     * ==> 단위테스트 진행하기
     */
    @Override
    public Member login(Member input) throws Exception {
        Member output = null;

        try {
            output = memberMapper.login(input);

            if (output == null) {
                throw new Exception("아이디와 혹은 이메일을 확인하세요");
            }
        } catch (Exception e) {
            log.error("Member 데이터 조회에 실패했습니다", e);
            throw e;
        }

        // 데이터 조회에 성공했다면 마지막 로그인 시각 갱신
        try {
            int rows = memberMapper.updateLoginDate(output);

            if (rows == 0) {
                throw new Exception("존재하지 않는 회원에 대한 요청입니다");
            }
        } catch (Exception e) {
            log.error("Member 데이터 갱신에 실패했습니다", e);
            throw e;
        }
        return output;
    }

    // 회원탈퇴
    @Override
    public int out(Member input) throws Exception {
        int rows = 0;

        try {
            rows = memberMapper.out(input);
            if (rows == 0) {
                throw new Exception("비밀번호 확인이 잘못되었거나 존재하지 앟는 회원에 대한 요청입니다");
            }
        } catch (Exception e) {
            log.error("Member 데이터 수정에 실패했습니다", e);
            throw e;
        }
        return rows;
    }
}