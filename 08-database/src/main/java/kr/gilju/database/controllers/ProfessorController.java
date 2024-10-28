package kr.gilju.database.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import kr.gilju.database.helpers.Pagination;
import kr.gilju.database.helpers.WebHelper;
import kr.gilju.database.models.Department;
import kr.gilju.database.models.Professor;
import kr.gilju.database.services.DepartmentService;
import kr.gilju.database.services.ProfessorService;

@Controller
public class ProfessorController {
  /** 교수 관리 서비스 객체 주입 */
  @Autowired
  private ProfessorService professorService;

  /** 학과 관리 서비스 객체 주입 */
  @Autowired
  private DepartmentService departmentService;

  /** WebHelper 주입 */
  @Autowired
  private WebHelper webHelper;

  /**
   * 교수 목록 화면
   * 
   * @param model    모델
   * @param response 교슈 목록 화면을 구현한 view 경로
   * @return
   */
  @GetMapping("/professor")
  public String index(Model model,
      // 검색어 파라미터 (페이지가 처음 열릴때는 값이 없음. 필수(required)가 아님)
      @RequestParam(value = "keyword", required = false) String keyword,
      // 페이지 구현에서 사용할 현재 페이지 번호
      @RequestParam(value = "page", defaultValue = "1") int nowPage) {

    int totalCount = 0; // 전체 게시글 수
    int listCount = 5; // 한 페이지당 표시할 목록 수
    int pageCount = 2; // 한 그룹당 표시할 페이지 번호 수

    // 페이지 번호를 계산한 결과가 저장될 객체
    Pagination pagination = null;

    // 조회 조건에 사용할 객체
    Professor input = new Professor();
    input.setName(keyword);
    input.setUserid(keyword);

    List<Professor> output = null;

    try {
      // 전체 게시글 수 조회
      totalCount = professorService.getCount(input);
      // 페이지 번호 계산 --> 계산 결과를 로그로 출력된 것이다
      pagination = new Pagination(nowPage, totalCount, listCount, pageCount);

       // sql의 Limit 절에서 사용될 값을 beans 의 static 변수에 저장 
       Professor.setOffset(pagination.getOffset());
       Professor.setListCount(pagination.getListCount());

       output = professorService.getList(input);
      } catch (Exception e) {
        webHelper.serverError(e);
      }

      model.addAttribute("professors", output);
      model.addAttribute("keyword", keyword);
      model.addAttribute("pagination", pagination);
      
    return "/professor/index";
  }

  /**
   * 교수 상세 화면
   * 
   * @param model
   * @param profno
   * @return
   */

  @GetMapping("professor/detail/{profno}")
  public String detail(Model model,
      @PathVariable("profno") int profno) {

    // 조회 조건에 사용할 변수를 beans에 저장
    Professor input = new Professor();
    input.setProfno(profno);

    // 조회 결과를 저장할 객체 선언
    Professor output = null;

    try {
      // 데이터 조회
      output = professorService.getItem(input);
    } catch (Exception e) {
      webHelper.serverError(e);
    }

    // view 에 데이터 전달
    model.addAttribute("professor", output);
    return "/professor/detail";
  }

  /**
   * 교수 등록 화면
   * 
   * @return 교수 등록 화면을 구현한 뷰 경로
   */
  @GetMapping("/professor/add")
  public String add(Model model) {
    //모든 학과 목록을 조회하여 뷰에 전달한다
    List<Department> output = null;

    try {
        output = departmentService.getList(null);
    } catch (Exception e) {
      webHelper.serverError(e);
    }
    model.addAttribute("departments", output);
    return "/professor/add";
  }

  /**
   * 교수 등록 처리
   * 액션 페이지들은 뷰를 사용하지 않고 다른 페이지로 이동해야 하므로 
   * 메서드 상단에 ResponseBody를 적용하여 뷰없이 직접응답을 구현한다
   * 
   * @param name
   * @param userid
   * @param position
   * @param dname
   * @param hiredate
   * @param comm
   * @param deptno
   */
  @ResponseBody
  @PostMapping("/professor/add_ok")
  public void addOk(HttpServletRequest request,
      @RequestParam("name") String name,
      @RequestParam("userid") String userid,
      @RequestParam("position") String position,
      @RequestParam("sal") int sal,
      @RequestParam("hiredate") String hiredate,
      // @RequestParam("comm")Integer comm,
      @RequestParam(value = "comm", required = false) Integer comm,
      @RequestParam("deptno") int deptno) {

    String referer = request.getHeader("referer");

    if (referer == null || !referer.contains("/professor")) {
      webHelper.badRequest("올바르지 않은 접근 입니다");
      return;
    }

    // 저장할 값을을 beans에 담는다
    Professor input = new Professor();
    input.setName(name);
    input.setUserid(userid);
    input.setPosition(position);
    input.setSal(sal);
    input.setHiredate(hiredate);
    input.setComm(comm);
    input.setDeptno(deptno);

    try {
      professorService.addItem(input);
    } catch (Exception e) {
      webHelper.serverError(e);
    }

    // insert, update, delete 처리를 수행하는 경우에는 리다이렉트로 이동
    // insert 결과를 확인할 수 있는 상세 페이지로 이동해야 한다
    // 상세 페이지에 조회 대상의 pk 값을 전달해야 한다
    // 등록하면 새로 생성된 학과 번호를 들고 해당 페이지로 이동(주소창 보면 새로 생성된 학과 번호 확인 가능함)
    // 학과번호가 department 에서의 pk값임
    webHelper.redirect("/professor/detail/" + input.getProfno(), "등록되었습니다");
  }

  /**
   * 교수 삭제 처리
   * 
   * @param request
   * @param profno
   */

  @ResponseBody
  @GetMapping("/professor/delete/{profno}")
  public void delete(HttpServletRequest request,
      @PathVariable("profno") int profno) {

    String referer = request.getHeader("referer");

    if (referer == null || !referer.contains("/professor")) {
      webHelper.badRequest("올바르지 않은 접근 입니다");
      return;
    }

    Professor input = new Professor();
    input.setProfno(profno);

    try {
      professorService.deleteItem(input);
    } catch (Exception e) {
      webHelper.serverError(e);
    }

    webHelper.redirect("/professor", "삭제되었습니다");
  }

  /**
   * 교수 수정 페이지
   * 
   * @param moder  모델객체
   * @param deptno 학과번호
   * @return 뷰페이지의 경로
   */
  @GetMapping("/professor/edit/{profno}")
  public String edit(Model model,
      @PathVariable("profno") int profno) {

    // 파라미터로 받은 pk값을 빈즈에객체에 담기
    // --> 검색조건으로 사용하기 위힘
    Professor input = new Professor();
    input.setProfno(profno);

    // 수정할 데이터의 현재값을 조회한다
    Professor output = null;
    List<Department> output2 =null;

    try {
      output = professorService.getItem(input);
      output2 = departmentService.getList(null);
    }catch (Exception e) {
      webHelper.serverError(e);
    }

    // 뷰에데이터 전달
    model.addAttribute("professor", output);
    model.addAttribute("departments", output2);

    return "/professor/edit";
  }

  @ResponseBody
  @PostMapping("/professor/edit_ok/{profno}")
  public void edit_ok(
      @PathVariable("profno") int profno,
      @RequestParam("name") String name,
      @RequestParam("userid") String userid,
      @RequestParam("position") String position,
      @RequestParam("sal") int sal,
      @RequestParam("hiredate") String hiredate,
      // @RequestParam("comm") Integer comm,
      @RequestParam(value = "comm", required = false) Integer comm,
      @RequestParam("deptno") int deptno) {

    // 수정할 값을을 beans에 담는다
    Professor input = new Professor();
    input.setProfno(profno);
    input.setName(name);
    input.setUserid(userid);
    input.setPosition(position);
    input.setSal(sal);
    input.setHiredate(hiredate);
    input.setComm(comm);
    input.setDeptno(deptno);

    // 데이터를 수정한다

    try {
      professorService.editItem(input);
    }catch (Exception e) {
      webHelper.serverError(e);
    }

    // 수정결과를 확인하기 위해서 상세 페이지로 이동
    webHelper.redirect("/professor/detail/" + input.getProfno(), "수정되었습니다");

  }
}