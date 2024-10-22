package kr.gilju.database.services;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.gilju.database.exceptions.ServiceNoResultException;
import kr.gilju.database.models.Professor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class ProfessorServiceTest {
  @Autowired
  private ProfessorService professorService;

  @Test
  @DisplayName("교수 추가 테스트")
  void insertDepartMent() throws Exception {
    Professor professor = new Professor();
    professor.setName("헬로월드");
    professor.setUserid("helloworld");
    professor.setPosition("강사");
    professor.setSal(500000);
    professor.setHiredate("2023-04-11");
    professor.setComm(1000000);
    professor.setDeptno(102);

    Professor result = null;

    try {
      result = professorService.addItem(professor);
    } catch (ServiceNoResultException e) {
      log.error("SQL문 처리 결과 없음", e);
    } catch (Exception e) {
      log.error("Mapper 구현 에러", e);
      throw e;
    }

    if (result != null) {
      log.debug("result: " + result);
      log.debug("new prfno: " + professor.getProfno());
    }
  }

  @Test
  @DisplayName("교수 수정 테스트")
  void updateDepartment() throws Exception {
    Professor professor = new Professor();
    professor.setProfno(9924);
    professor.setName("헬로월드");
    professor.setUserid("helloworld");
    professor.setPosition("강사");
    professor.setSal(500000);
    professor.setHiredate("2023-04-11");
    professor.setComm(1000000);
    professor.setDeptno(102);

    Professor result = null;

    try {
      result = professorService.editItem(professor);
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
  @DisplayName("교수 삭제 테스트")
  void deleteProfessor() throws Exception {
    Professor professor = new Professor();
    professor.setProfno(9918);

    try {
      professorService.deleteItem(professor);
    } catch (ServiceNoResultException e) {
      log.error("SQL문 처리 결과 없음", e);
    } catch (Exception e) {
      log.error("Mapper 구현 에러", e);
      throw e;
    }
  }


  @Test
  @DisplayName("단일행 교수 조회 테스트")
  void selectOneProfessor() throws Exception {
    Professor professor = new Professor();
    professor.setProfno(10204);

    Professor result = null;

    try {
      result = professorService.getItem(professor);
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
  void selectListProfessor() throws Exception {
    List<Professor> output = null;

    Professor input = new Professor();
    input.setName("김진영");

    try {
      output = professorService.getList(input);
    } catch (ServiceNoResultException e) {
      log.error("SQL문 처리 결과 없음", e);
    } catch (Exception e) {
      log.error("Mapper 구현 에러", e);
      throw e;
    }

    if (output != null) {
      for (Professor item : output) {
        log.debug("result: " + item.toString());
      }
    }
  }
}