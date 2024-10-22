package kr.gilju.database.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.gilju.database.exceptions.ServiceNoResultException;
import kr.gilju.database.mappers.ProfessorMapper;
import kr.gilju.database.mappers.StudentMapper;
import kr.gilju.database.models.Professor;
import kr.gilju.database.models.Student;
import kr.gilju.database.services.ProfessorService;

/**
 * 학과 관리 기능과 관련된 MyBatis Mapper를 간접적으로 호출하기 위한 기능 명세
 * 
 * 1) 모든 메서드를 재정의 한 직후 리턴값 먼저 정의
 */

 @Service
public class ProfessorServiceImpl implements ProfessorService {


    /** sql문을 구현하고 있는 mapper객체 주입 */
    @Autowired
    private ProfessorMapper professorMapper;

    @Autowired
    private StudentMapper studentMapper;


    @Override
    public Professor addItem(Professor input) throws ServiceNoResultException, Exception {
    int rows = professorMapper.insert(input);

    if (rows == 0) {
        throw new ServiceNoResultException("저장된 데이터가 없습니다");
    }
    
    return professorMapper.selectItem(input);
    }

    @Override
    public Professor editItem(Professor input) throws ServiceNoResultException, Exception {
    int rows = professorMapper.update(input);

    if (rows == 0) {
        throw new ServiceNoResultException("수정된 데이터가 없습니다");
    }
    
    return professorMapper.selectItem(input);
    }

    @Override
    public int deleteItem(Professor input) throws ServiceNoResultException, Exception {

        // 학과 데이터 삭제를 위한 참조관계에 있는 자식 데이터를 순서대로 삭제
        Student student = new Student();
        student.setDeptno(input.getDeptno());
        studentMapper.deleteByDeptno(student);

        int rows = professorMapper.delete(input);

        if (rows == 0) {
            throw new ServiceNoResultException("삭제된 데이터가 없습니다");
        }

        return rows;
    }

    @Override
    public Professor getItem(Professor input) throws ServiceNoResultException, Exception {
        Professor output = professorMapper.selectItem(input);

        if (output == null) {
            // 객체가 없다는 내용의 에러를 강제 발생시킴
            throw new ServiceNoResultException("조회된 데이터가 없습니다");
        }

        return output;
    }

    @Override
    public List<Professor> getList(Professor input) throws ServiceNoResultException, Exception {
        return professorMapper.selectList(input);
    }
}