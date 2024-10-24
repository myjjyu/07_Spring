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
import kr.gilju.database.exceptions.ServiceNoResultException;
import kr.gilju.database.helpers.WebHelper;
import kr.gilju.database.models.Student;
import kr.gilju.database.services.StudentService;

@Controller
public class StudentController {
  /** 학생 관리 서비스 객체 주입 */
  @Autowired
  private StudentService studentService;

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
  public String index(Model model) {

    List<Student> students = null;

    try {
      students = studentService.getList(null);
    } catch (ServiceNoResultException e) {
      webHelper.serverError(e);
      return null;
    } catch (Exception e) {
      webHelper.serverError(e);
      return null;
    }

    model.addAttribute("students", students);
    return "/student/index";
  }

  /**
   * 학생 상세 화면
   * 
   * @param model
   * @param profno
   * @return
   */

  @GetMapping("student/detail/{studno}")
  public String detail(Model model,
      @PathVariable("studno") int studno) {

    // 조회 조건에 사용할 변수를 beans에 저장
    Student params = new Student();
    params.setStudno(studno);

    // 조회 결과를 저장할 객체 선언
    Student student = null;

    try {
      // 데이터 조회
      student = studentService.getItem(params);
    } catch (ServiceNoResultException e) {
      webHelper.serverError(e);
    } catch (Exception e) {
      webHelper.serverError(e);
    }

    // view 에 데이터 전달
    model.addAttribute("student", student);
    return "/student/detail";
  }

  /**
   * 학생 등록 화면
   * 
   * @return 교수 등록 화면을 구현한 뷰 경로
   */
  @GetMapping("student/add")
  public String add() {
    return "/student/add";
  }

  /**
   * 학생 수정
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
      @RequestParam(value = "profno", required = false) Integer profno
      ) {

    String referer = request.getHeader("referer");

    if (referer == null || !referer.contains("/student")) {
      webHelper.badRequest("올바르지 않은 접근 입니다");
      return;
    }

    // 저장할 값을을 beans에 담는다
    Student student = new Student();
    student.setName(name);
    student.setUserid(userid);
    student.setGrade(grade);
    student.setIdnum(idnum);
    student.setBirthdate(birthdate);
    student.setTel(tel);
    student.setHeight(height);
    student.setWeight(weight);
    student.setDeptno(deptno);
    student.setProfno(null);

    try {
      studentService.addItem(student);
    } catch (ServiceNoResultException e) {
      webHelper.serverError(e);
    } catch (Exception e) {
      webHelper.serverError(e);
    }

    // insert, update, delete 처리를 수행하는 경우에는 리다이렉트로 이동
    // insert 결과를 확인할 수 있는 상세 페이지로 이동해야 한다
    // 상세 페이지에 조회 대상의 pk 값을 전달해야 한다
    // 등록하면 새로 생성된 학과 번호를 들고 해당 페이지로 이동(주소창 보면 새로 생성된 학과 번호 확인 가능함)
    // 학과번호가 department 에서의 pk값임
    webHelper.redirect("/student/detail/" + student.getStudno(), "등록되었습니다");
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

    Student student = new Student();
    student.setStudno(studno);

    try {
      studentService.deleteItem(student);
    } catch (ServiceNoResultException e) {
      webHelper.serverError(e);
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
    Student params = new Student();
    params.setStudno(studno);

    // 수정할 데이터의 현재값을 조회한다
    Student student = null;

    try {
      student = studentService.getItem(params);
    } catch (ServiceNoResultException e) {
      webHelper.serverError(e);
    } catch (Exception e) {
      webHelper.serverError(e);
    }

    // 뷰에데이터 전달
    model.addAttribute("student", student);

    return "/Student/edit";
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
    Student student = new Student();
    student.setStudno(studno);
    student.setName(name);
    student.setUserid(userid);
    student.setGrade(grade);
    student.setIdnum(idnum);
    student.setBirthdate(birthdate);
    student.setTel(tel);
    student.setHeight(height);
    student.setWeight(weight);
    student.setDeptno(deptno);
    student.setProfno(profno);

    // 데이터를 수정한다

    try {
      studentService.editItem(student);
    } catch (ServiceNoResultException e) {
      webHelper.serverError(e);
    } catch (Exception e) {
      webHelper.serverError(e);
    }

    // 수정결과를 확인하기 위해서 상세 페이지로 이동
    webHelper.redirect("/student/detail/" + student.getStudno(), "수정되었습니다");

  }
}
