package kr.gilju.database.services;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.gilju.database.exceptions.ServiceNoResultException;
import kr.gilju.database.models.Department;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class DepartmentServiceTest {
  @Autowired
  private DepartmentService departmentService;

  @Test
  @DisplayName("학과추가 테스트")
  void insertDepartMent() throws Exception {
    Department input = new Department();
    input.setDname("스프링학과");
    input.setLoc("G강의실");

    Department output = null;

    try {
      output = departmentService.addItem(input);
    } catch (ServiceNoResultException e) {
      log.error("SQL문 처리 결과 없음", e);
    } catch (Exception e) {
      log.error("Mapper 구현 에러", e);
      throw e;
    }

    if (output != null) {
      log.debug("output: " + output.toString());
    }
  }

  @Test
  @DisplayName("학과 수정 테스트")
  void updateDepartment() throws Exception {
    Department input = new Department();
    input.setDeptno(102);
    input.setDname("스프링 학과");
    input.setLoc("G강의실");

    Department output = null;

    try {
      output = departmentService.editItem(input);
    } catch (ServiceNoResultException e) {
      log.error("SQL문 처리 결과 없음", e);
    } catch (Exception e) {
      log.error("Mapper 구현 에러", e);
      throw e;
    }

    if (output != null) {
      log.debug("output: " + output.toString());
    }
  }

  @Test
  @DisplayName("학과 삭제 테스트")
  void deleteDepartment() throws Exception {
    Department input = new Department();
    input.setDeptno(203);

    try {
      departmentService.deleteItem(input);
    } catch (ServiceNoResultException e) {
      log.error("SQL문 처리 결과 없음", e);
    } catch (Exception e) {
      log.error("Mapper 구현 에러", e);
      throw e;
    }
  }

  @Test
  @DisplayName("하나의 학과 조회 테스트")
  void selectOneDepartment() throws Exception {
    Department input = new Department();
    input.setDeptno(102);

    Department output = null;

    try {
      output = departmentService.getItem(input);
    } catch (ServiceNoResultException e) {
      log.error("SQL문 처리 결과 없음", e);
    } catch (Exception e) {
      log.error("Mapper 구현 에러", e);
      throw e;
    }

    if (output != null) {
      log.debug("output: " + output.toString());
    }
  }

  @Test
  @DisplayName("하나의 목록 조회 테스트")
  void selectListDepartment() throws Exception {
    List<Department> output = null;

    Department input = new Department();
    input.setDname("학과");

    try {
      output = departmentService.getList(input);
    } catch (ServiceNoResultException e) {
      log.error("SQL문 처리 결과 없음", e);
    } catch (Exception e) {
      log.error("Mapper 구현 에러", e);
      throw e;
    }

    if (output != null) {
      for (Department item : output) {
        log.debug("output: " + output.toString());
      }
    }
  }
}