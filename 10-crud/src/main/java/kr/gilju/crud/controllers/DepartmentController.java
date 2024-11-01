package kr.gilju.crud.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DepartmentController {
/**
 * 목록조회
 * @return
 */
  @GetMapping({ "/department", "/department/list" })
  public String list() {
    return "department/list";
  }

  /**
   * 상세조회
   * @param model
   * @param deptno
   * @return
   */
  @GetMapping("/department/view/{deptno}")
  public String view(Model model, @PathVariable("deptno") int deptno) {
    model.addAttribute("deptno", deptno);
    return "department/view";
  }
  

  /**
   * 등록
   * @return
   */
  @GetMapping("/department/add")
  public String add() {
    return "department/add";
  }


  /**
   * 수정
   * @param model
   * @param deptno
   * @return
   */
  @GetMapping("/department/edit/{deptno}")
  public String edit(Model model, @PathVariable("deptno") int deptno) {
    model.addAttribute("deptno", deptno);
    return "department/edit";
  }
}
