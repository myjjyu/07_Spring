package kr.gilju.database.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.gilju.database.exceptions.ServiceNoResultException;
import kr.gilju.database.mappers.StudentMapper;
import kr.gilju.database.models.Student;
import kr.gilju.database.services.StudentService;

/**
 * 학과 관리 기능과 관련된 MyBatis Mapper를 간접적으로 호출하기 위한 기능 명세
 * 
 * 1) 모든 메서드를 재정의 한 직후 리턴값 먼저 정의
 */

 @Service
public class StudentServiceImpl implements StudentService {


    /** sql문을 구현하고 있는 mapper객체 주입 */
    @Autowired
    private StudentMapper studentMapper;


    @Override
    public Student addItem(Student input) throws ServiceNoResultException, Exception {
    int rows = studentMapper.insert(input);

    if (rows == 0) {
        throw new ServiceNoResultException("저장된 데이터가 없습니다");
    }
    
    return studentMapper.selectItem(input);
    }

    @Override
    public Student editItem(Student input) throws ServiceNoResultException, Exception {
    int rows = studentMapper.update(input);

    if (rows == 0) {
        throw new ServiceNoResultException("수정된 데이터가 없습니다");
    }
    
    return studentMapper.selectItem(input);
    }

    @Override
    public int deleteItem(Student input) throws ServiceNoResultException, Exception {

        int rows = studentMapper.delete(input);

        if (rows == 0) {
            throw new ServiceNoResultException("삭제된 데이터가 없습니다");
        }

        return rows;
    }

    @Override
    public Student getItem(Student input) throws ServiceNoResultException, Exception {
        Student output = studentMapper.selectItem(input);

        if (output == null) {
            throw new ServiceNoResultException("조회된 데이터가 없습니다");
        }

        return output;
    }

    @Override
    public List<Student> getList(Student input) throws ServiceNoResultException, Exception {
        return studentMapper.selectList(input);
    }
}