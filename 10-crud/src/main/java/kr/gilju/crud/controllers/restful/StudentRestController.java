package kr.gilju.crud.controllers.restful;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kr.gilju.crud.helpers.Pagination;
import kr.gilju.crud.helpers.RestHelper;
import kr.gilju.crud.models.Student;
import kr.gilju.crud.services.DepartmentService;
import kr.gilju.crud.services.ProfessorService;
import kr.gilju.crud.services.StudentService;

@RestController
public class StudentRestController {
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
  private RestHelper restHelper;

  /**
   * 학생 목록 화면
   * 
   * @param model    모델
   * @param response 학생 목록 화면을 구현한 view 경로
   * @return
   */
  @GetMapping({"/api/student"})
  public Map<String, Object> getList(
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
      return restHelper.serverError(e);
    }

    Map<String, Object> data = new LinkedHashMap<String, Object>();
    data.put("keyword", keyword);
    data.put("item", output);
    data.put("pagination", pagination);

    return restHelper.sendJson(data);
}

  /**
   * 학생 상세 화면
   * 
   * @param model  모델
   * @param profno 담당 교수 번호
   * @return
   */

  @GetMapping("/api/student/{studno}")
  public Map<String, Object> detail(Model model,
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
      return restHelper.serverError(e);
    }

    // view 에 데이터 전달
    Map<String, Object> data = new LinkedHashMap<String, Object>();
     data.put("item", output);
 
     return restHelper.sendJson(data);
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
  @PostMapping("/api/student")
  public Map<String, Object> addOk(HttpServletRequest request,
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

    Student output = null;

    try {
      output=studentService.addItem(input);
    } catch (Exception e) {
      return restHelper.serverError(e);
    }

    Map<String, Object> data = new LinkedHashMap<String, Object>();
    data.put("item", output);

    return restHelper.sendJson(data);
  }

  /**
   * 학생삭제
   * 
   * @param request
   * @param studno
   */

  @DeleteMapping("/api/student/{studno}")
  public Map<String, Object> delete(HttpServletRequest request,
      @PathVariable("studno") int studno) {

    String referer = request.getHeader("referer");


    Student input = new Student();
    input.setStudno(studno);

    try {
      studentService.deleteItem(input);
    } catch (Exception e) {
      restHelper.serverError(e);
    }

    return restHelper.sendJson();
  }

 
/**
 * 수정처리 
 * @param studno
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
  @PutMapping("/api/student/{studno}")
  public Map<String, Object> edit_ok(
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

    Student output = null;

    // 데이터를 수정한다
    try {
      output=studentService.editItem(input);
    } catch (Exception e) {
      return restHelper.serverError(e);
    }

    Map<String, Object> data = new LinkedHashMap<String, Object>();
    data.put("item", output);

    return restHelper.sendJson(data);
  }
}
