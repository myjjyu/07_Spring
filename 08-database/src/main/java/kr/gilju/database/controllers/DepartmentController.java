package kr.gilju.database.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import kr.gilju.database.exceptions.ServiceNoResultException;
import kr.gilju.database.helpers.WebHelper;
import kr.gilju.database.models.Department;
import kr.gilju.database.services.DepartmentService;

@Controller
public class DepartmentController {
  /** 학과 관리 서비스 객체 주입 */
  @Autowired
  private DepartmentService departmentService;

  /** WebHelper 주입 */
  @Autowired
  private WebHelper webHelper;

  /**
   * 학과 목록 화면
   * 
   * @param model    모델
   * @param response 학과 목록 화면을 구현한 view 경로
   * @return
   */
  @GetMapping("/department")
  public String index(Model model) {

    List<Department> departments = null;

    try {
      departments = departmentService.getList(null);
    } catch (ServiceNoResultException e) {
      webHelper.serverError(e);
      return null;
    } catch (Exception e) {
      webHelper.serverError(e);
      return null;
    }

    model.addAttribute("departments", departments);
    return "/department/index";
  }

  /**
   * 학과 상세 화면
   * 
   * @param model
   * @param deptno
   * @return
   */

  @GetMapping("department/detail/{deptno}")
  public String detail(Model model,
      @PathVariable("deptno") int deptno) {

    // 조회 조건에 사용할 변수를 beans에 저장
    Department params = new Department();
    params.setDeptno(deptno);

    // 조회 결과를 저장할 객체 선언
    Department department = null;

    try {
      // 데이터 조회
      department = departmentService.getItem(params);
    } catch (ServiceNoResultException e) {
      webHelper.serverError(e);
    } catch (Exception e) {
      webHelper.serverError(e);
    }

    // view 에 데이터 전달
    model.addAttribute("department", department);
    return "/department/detail";
  }
}