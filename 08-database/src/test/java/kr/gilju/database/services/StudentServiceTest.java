package kr.gilju.database.services;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.gilju.database.exceptions.ServiceNoResultException;
import kr.gilju.database.models.Student;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class StudentServiceTest {
  @Autowired
  private StudentService studentService;

  @Test
  @DisplayName("학생추가 테스트")
  void insertStudent() throws Exception {
    Student student = new Student();
    student.setName("김이삼");
    student.setUserid("kim123");
    student.setGrade(3);
    student.setIdnum("1234567890123");
    student.setBirthdate("2000-03-15");
    student.setTel("010-1234-5678");
    student.setHeight(175);
    student.setWeight(65);
    student.setDeptno(102);
    student.setProfno(9902);

    Student result = null;

    try {
      result = studentService.addItem(student);
    } catch (ServiceNoResultException e) {
      log.error("SQL문 처리 결과 없음", e);
    } catch (Exception e) {
      log.error("Mapper 구현 에러", e);
      throw e;
    }

    if (result != null) {
      log.debug("result: " + result);
      log.debug("new prfno: " + student.getProfno());
    }
  }

  @Test
  @DisplayName("학생 수정 테스트")
  void updateStudent() throws Exception {
    Student student = new Student();
    student.setStudno(20103); // 수정할 학생 학번
    student.setName("김수정");
    student.setUserid("soo1234");
    student.setGrade(4);
    student.setIdnum("9876543210987");
    student.setBirthdate("1999-12-01");
    student.setTel("010-9876-5432");
    student.setHeight(180);
    student.setWeight(70);
    student.setDeptno(103);
    student.setProfno(null);

    Student result = null;

    try {
      result = studentService.editItem(student);
    } catch (ServiceNoResultException e) {
      log.error("SQL문 처리 결과 없음", e);
    } catch (Exception e) {
      log.error("Mapper 구현 에러", e);
      throw e;
    }

    if (result != null) {
      log.debug("result: " + result.toString());
    }
  }

  @Test
  @DisplayName("학생 삭제 테스트")
  void deleteStudent() throws Exception {
    Student student = new Student();
    student.setStudno(10106);

    try {
      studentService.deleteItem(student);
    } catch (ServiceNoResultException e) {
      log.error("SQL문 처리 결과 없음", e);
    } catch (Exception e) {
      log.error("Mapper 구현 에러", e);
      throw e;
    }
  }


  @Test
  @DisplayName("단일행 학생 조회 테스트")
  void selectOneStudent() throws Exception {
    Student student = new Student();
    student.setProfno(10204);

    Student result = null;

    try {
      result = studentService.getItem(student);
    } catch (ServiceNoResultException e) {
      log.error("SQL문 처리 결과 없음", e);
    } catch (Exception e) {
      log.error("Mapper 구현 에러", e);
      throw e;
    }

    if (result != null) {
      log.debug("result: " + result.toString());
    }
  }

  @Test
  @DisplayName("다중행 학생 조회 테스트")
  void selectListStudent() throws Exception {
    List<Student> output = null;

    Student input = new Student();
    input.setName("김진영");

    try {
      output = studentService.getList(input);
    } catch (ServiceNoResultException e) {
      log.error("SQL문 처리 결과 없음", e);
    } catch (Exception e) {
      log.error("Mapper 구현 에러", e);
      throw e;
    }

    if (output != null) {
      for (Student item : output) {
        log.debug("result: " + item.toString());
      }
    }
  }
}