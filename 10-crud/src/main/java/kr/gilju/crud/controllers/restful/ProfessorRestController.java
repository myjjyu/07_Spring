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
import kr.gilju.crud.models.Professor;
import kr.gilju.crud.services.DepartmentService;
import kr.gilju.crud.services.ProfessorService;

@RestController
public class ProfessorRestController {
  /** 교수 관리 서비스 객체 주입 */
  @Autowired
  private ProfessorService professorService;

  /** 학과 관리 서비스 객체 주입 */
  @Autowired
  private DepartmentService departmentService;

  /** WebHelper 주입 */
  @Autowired
  private RestHelper restHelper;

  /**
   * 교수 목록 화면
   * 
   * @param model    모델
   * @param response 교슈 목록 화면을 구현한 view 경로
   * @return
   */
  @GetMapping({"/api/professor"})
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
        return restHelper.serverError(e);
      }

      Map<String, Object> data = new LinkedHashMap<String, Object>();
      data.put("keyword", keyword);
      data.put("item", output);
      data.put("pagination", pagination);
  
      return restHelper.sendJson(data);
  }

  /**
   * 교수 상세 화면
   * 
   * @param model
   * @param profno
   * @return
   */

  @GetMapping("api/professor/{profno}")
  public Map<String, Object> detail(Model model,
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
      return restHelper.serverError(e);
    }

     // view 에 데이터 전달
     Map<String, Object> data = new LinkedHashMap<String, Object>();
     data.put("item", output);
 
     return restHelper.sendJson(data);
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
  @PostMapping("/api/professor")
  public  Map<String, Object> addOk(HttpServletRequest request,
      @RequestParam("name") String name,
      @RequestParam("userid") String userid,
      @RequestParam("position") String position,
      @RequestParam("sal") int sal,
      @RequestParam("hiredate") String hiredate,
      // @RequestParam("comm")Integer comm,
      @RequestParam(value = "comm", required = false) Integer comm,
      @RequestParam("deptno") int deptno) {


    // 저장할 값을을 beans에 담는다
    Professor input = new Professor();
    input.setName(name);
    input.setUserid(userid);
    input.setPosition(position);
    input.setSal(sal);
    input.setHiredate(hiredate);
    input.setComm(comm);
    input.setDeptno(deptno);

    Professor output = null;

    try {
      output = professorService.addItem(input);
    } catch (Exception e) {
      return restHelper.serverError(e);
    }

    Map<String, Object> data = new LinkedHashMap<String, Object>();
    data.put("item", output);

    return restHelper.sendJson(data);
  }


  /**
   * 교수 삭제 처리
   * 
   * @param request
   * @param profno
   */

  @DeleteMapping("/api/professor/{profno}")
  public Map<String, Object> delete(HttpServletRequest request,
      @PathVariable("profno") int profno) {

    String referer = request.getHeader("referer");

    Professor input = new Professor();
    input.setProfno(profno);

    try {
      professorService.deleteItem(input);
    } catch (Exception e) {
      restHelper.serverError(e);
    }

    return restHelper.sendJson();
  }

  /**
   * 수정처리
   * @param profno
   * @param name
   * @param userid
   * @param position
   * @param sal
   * @param hiredate
   * @param comm
   * @param deptno
   */

  @PutMapping("/api/professor/{profno}")
  public Map<String, Object> edit_ok(
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

    Professor output = null;

    // 데이터를 수정한다

    try {
      output = professorService.editItem(input);
    }catch (Exception e) {
      return restHelper.serverError(e);
    }

    Map<String, Object> data = new LinkedHashMap<String, Object>();
    data.put("item", output);

    return restHelper.sendJson(data);
  }
}