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
import kr.gilju.database.models.Student;
import kr.gilju.database.services.DepartmentService;
import kr.gilju.database.services.ProfessorService;
import kr.gilju.database.services.StudentService;

@Controller
public class StudentController {
  /** 학생 관리 서비스 객체 주입 */
  @Autowired
  private StudentService studentService;

  /** 학과 서비스 객체 주입 추가 */
  @Autowired
  private DepartmentService departmentService;

  /** 교수 서비스 객체 주입 추가 */
  @Autowired
  private ProfessorService professorService;

  /** WebHelper 주입 */
  @Autowired
  private WebHelper webHelper;

  /**
   * 학생 목록 화면
   * 
   * @param model    모델
   * @param response 학생 목록 화면을 구현한 view 경로
   * @return
   */
  @GetMapping("/student")
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
    Student input = new Student();
    input.setName(keyword);
    input.setUserid(keyword);

    List<Student> output = null;

    try {
      // 전체 게시글 수 조회
      totalCount = studentService.getCount(input);
      // 페이지 번호 계산 --> 계산 결과를 로그로 출력된 것이다
      pagination = new Pagination(nowPage, totalCount, listCount, pageCount);

      // sql의 Limit 절에서 사용될 값을 beans 의 static 변수에 저장
      Student.setOffset(pagination.getOffset());
      Student.setListCount(pagination.getListCount());

      output = studentService.getList(input);
    } catch (Exception e) {
      webHelper.serverError(e);
    }

    model.addAttribute("students", output);
    model.addAttribute("keyword", keyword);
    model.addAttribute("pagination", pagination);

    return "/student/index";
  }

  /**
   * 학생 상세 화면
   * 
   * @param model  모델
   * @param profno 담당 교수 번호
   * @return
   */

  @GetMapping("student/detail/{studno}")
  public String detail(Model model,
      @PathVariable("studno") int studno) {

    // 조회 조건에 사용할 변수를 beans에 저장
    Student input = new Student();
    input.setStudno(studno);

    // 조회 결과를 저장할 객체 선언
    Student output = null;

    try {
      // 데이터 조회
      output = studentService.getItem(input);
    } catch (Exception e) {
      webHelper.serverError(e);
    }

    // view 에 데이터 전달
    model.addAttribute("student", output);
    return "/student/detail";
  }

  /**
   * 학생 등록 화면
   * 
   * @return 학생 등록 화면을 구현한 뷰 경로
   */
  @GetMapping("/student/add")
  public String add(Model model) {
    // 모든 학과와 교수 목록을 조회하여 뷰에 전달한다
    List<Department> output = null; // 학과 목록
    List<Professor> output2 = null; // 교수 목록

    try {
      output = departmentService.getList(null); // 학과 목록 조회
      output2 = professorService.getList(null); // 교수 목록 조회
      
    } catch (Exception e) {
      webHelper.serverError(e); // 서버 오류 처리
    }

    model.addAttribute("departments", output); // 학과 목록을 모델에 추가
    model.addAttribute("professors", output2); // 교수 목록을 모델에 추가
    return "/student/add"; // 학생 등록 화면 뷰 경로 반환
  }

  /**
   * 학생 등록 처리
   * 액션 페이지들은 뷰를 사용하지 않고 다른페이지로 이동해야 하므로
   * 메서드 상단에 ResponseBody 를 적용하여 뷰없이 직접 응답을 구현한다
   * 
   * @param request
   * @param name
   * @param userid
   * @param grade
   * @param idnum
   * @param birthdate
   * @param tel
   * @param height
   * @param weight
   * @param deptno
   * @param profno
   */
  @ResponseBody
  @PostMapping("/student/add_ok")
  public void addOk(HttpServletRequest request,
      @RequestParam("name") String name,
      @RequestParam("userid") String userid,
      @RequestParam("grade") int grade,
      @RequestParam("idnum") String idnum,
      @RequestParam("birthdate") String birthdate,
      @RequestParam("tel") String tel,
      @RequestParam("height") int height,
      @RequestParam("weight") int weight,
      @RequestParam("deptno") int deptno,
      @RequestParam(value = "profno", required = false) Integer profno) {

    String referer = request.getHeader("referer");

    if (referer == null || !referer.contains("/student")) {
      webHelper.badRequest("올바르지 않은 접근 입니다");
      return;
    }

    // 저장할 값을을 beans에 담는다
    Student input = new Student();
    input.setName(name);
    input.setUserid(userid);
    input.setGrade(grade);
    input.setIdnum(idnum);
    input.setBirthdate(birthdate);
    input.setTel(tel);
    input.setHeight(height);
    input.setWeight(weight);
    input.setDeptno(deptno);
    input.setProfno(profno);

    try {
      studentService.addItem(input);
    } catch (Exception e) {
      webHelper.serverError(e);
    }

    // insert, update, delete 처리를 수행하는 경우에는 리다이렉트로 이동
    // insert 결과를 확인할 수 있는 상세 페이지로 이동해야 한다
    // 상세 페이지에 조회 대상의 pk 값을 전달해야 한다
    // 등록하면 새로 생성된 학과 번호를 들고 해당 페이지로 이동(주소창 보면 새로 생성된 학과 번호 확인 가능함)
    // 학과번호가 department 에서의 pk값임
    webHelper.redirect("/student/detail/" + input.getStudno(), "등록되었습니다");
  }

  /**
   * 학생삭제
   * 
   * @param request
   * @param studno
   */

  @ResponseBody
  @GetMapping("/student/delete/{studno}")
  public void delete(HttpServletRequest request,
      @PathVariable("studno") int studno) {

    String referer = request.getHeader("referer");

    if (referer == null || !referer.contains("/student")) {
      webHelper.badRequest("올바르지 않은 접근 입니다");
      return;
    }

    Student input = new Student();
    input.setStudno(studno);

    try {
      studentService.deleteItem(input);
    } catch (Exception e) {
      webHelper.serverError(e);
    }

    webHelper.redirect("/student", "삭제되었습니다");
  }

  /**
   * 학생 수정 페이지
   * 
   * @param moder  모델객체
   * @param deptno 학과번호
   * @return 뷰페이지의 경로
   */
  @GetMapping("/student/edit/{studno}")
  public String edit(Model model,
      @PathVariable("studno") int studno) {

    // 파라미터로 받은 pk값을 빈즈에객체에 담기
    // --> 검색조건으로 사용하기 위힘
    Student input = new Student();
    input.setStudno(studno);

    // 수정할 데이터의 현재값을 조회한다
    Student output = null;
    List<Department> output2 = null; // 학과 목록
    List<Professor> output3 = null; // 교수 목록

    try {
      output = studentService.getItem(input);
      output2 = departmentService.getList(null); // 학과 목록 조회
      output3 = professorService.getList(null); // 교수 목록 조회

    } catch (Exception e) {
      webHelper.serverError(e);
    }

    // 뷰에데이터 전달
    model.addAttribute("student", output);
    model.addAttribute("departments", output2); // 학과 목록을 모델에 추가
    model.addAttribute("professors", output3); // 교수 목록을 모델에 추가

    return "/student/edit";
  }

  @ResponseBody
  @PostMapping("/student/edit_ok/{studno}")
  public void edit_ok(
      @PathVariable("studno") int studno,
      @RequestParam("name") String name,
      @RequestParam("userid") String userid,
      @RequestParam("grade") int grade,
      @RequestParam("idnum") String idnum,
      @RequestParam("birthdate") String birthdate,
      @RequestParam("tel") String tel,
      @RequestParam("height") int height,
      @RequestParam("weight") int weight,
      @RequestParam("deptno") int deptno,
      // @RequestParam("profno") Integer profno
      @RequestParam(value = "profno", required = false) Integer profno) {

    // 수정할 값을을 beans에 담는다
    Student input = new Student();
    input.setStudno(studno);
    input.setName(name);
    input.setUserid(userid);
    input.setGrade(grade);
    input.setIdnum(idnum);
    input.setBirthdate(birthdate);
    input.setTel(tel);
    input.setHeight(height);
    input.setWeight(weight);
    input.setDeptno(deptno);
    input.setProfno(profno);

    // 데이터를 수정한다
    try {
      studentService.editItem(input);
    } catch (Exception e) {
      webHelper.serverError(e);
    }

    // 수정결과를 확인하기 위해서 상세 페이지로 이동
    webHelper.redirect("/student/detail/" + input.getStudno(), "수정되었습니다");

  }
}
