package kr.gilju.database.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.gilju.database.mappers.ProfessorMapper;
import kr.gilju.database.mappers.StudentMapper;
import kr.gilju.database.models.Professor;
import kr.gilju.database.models.Student;
import kr.gilju.database.services.ProfessorService;
import lombok.extern.slf4j.Slf4j;

/**
 * 교수 관리 기능에 대한 비즈니스 로직 처리를 담당하는 서비스 계층의 구현체
 */

@Slf4j
@Service
public class ProfessorServiceImpl implements ProfessorService {

    /** sql문을 구현하고 있는 mapper객체 주입 */
    @Autowired
    private ProfessorMapper professorMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public Professor addItem(Professor input) throws Exception {
        int rows = 0;

        try {
            rows = professorMapper.insert(input);

            if (rows == 0) {
                throw new Exception("저장된 데이터가 없습니다");
            }
        } catch (Exception e) {
            log.error("데이터 저장에 실패했습니다", e);
            throw e;
        }

        return professorMapper.selectItem(input);
    }

    @Override
    public Professor editItem(Professor input) throws Exception {
        int rows = 0;

        try {
            rows = professorMapper.update(input);

            if (rows == 0) {
                throw new Exception("수정된 데이터가 없습니다");
            }
        } catch (Exception e) {
            log.error("데이터 수정에 실패했습니다", e);
            throw e;
        }

        return professorMapper.selectItem(input);
    }

    @Override
    public int deleteItem(Professor input) throws Exception {
        int rows = 0;

        // 학과 데이터 삭제를 위한 참조관계에 있는 자식 데이터를 순서대로 삭제
        Student student = new Student();
        student.setProfno(input.getProfno());

        try {
            studentMapper.deleteByDeptno(student);
            rows = professorMapper.delete(input);

            if (rows == 0) {
                throw new Exception("삭제된 데이터가 없습니다");
            }
        } catch (Exception e) {
            log.error("데이터 삭제에 실패했습니다", e);
            throw e;
        }

        return rows;
    }

    @Override
    public Professor getItem(Professor input) throws Exception {
        Professor output = null;

        try {
            output = professorMapper.selectItem(input);

            if (output == null) {
                // 객체가 없다는 내용의 에러를 강제 발생시킴
                throw new Exception("조회된 데이터가 없습니다");
            }
        } catch (Exception e) {
            log.error("교수 조회에 실패했습니다", e);
            throw e;
        }

        return output;
    }

    @Override
    public List<Professor> getList(Professor input) throws Exception {
        List<Professor> output = null;

        try {
            output = professorMapper.selectList(input);
        } catch (Exception e) {
            log.error("교수 목록 조회에 실패했습니다", e);
            throw e;
        }

        return output;
    }

    @Override
    public int getCount(Professor input) throws Exception {
        int output = 0;

        try {
            output = professorMapper.selectCount(input);
        } catch (Exception e) {
            log.error("데이터 집계에 실패했습니다", e);
            throw e;
        }

        return output;
    }

}
