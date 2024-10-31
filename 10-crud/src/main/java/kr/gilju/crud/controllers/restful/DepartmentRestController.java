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
import kr.gilju.crud.models.Department;
import kr.gilju.crud.services.DepartmentService;

@RestController
public class DepartmentRestController {
  /** 학과 관리 서비스 객체 주입 */
  @Autowired
  private DepartmentService departmentService;

  /** RestHelper 주입 */
  @Autowired
  private RestHelper restHelper;

  /**
   * 학과 목록 화면
   * 
   * @param model    모델
   * @param response 학과 목록 화면을 구현한 view 경로
   * @return
   */
  @GetMapping({ "/api/department" })
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
    Department input = new Department();
    input.setDname(keyword);
    input.setLoc(keyword);

    List<Department> output = null;

    try {
      // 전체 게시글 수 조회
      totalCount = departmentService.getCount(input);
      // 페이지 번호 계산 --> 계산 결과를 로그로 출력된 것이다
      pagination = new Pagination(nowPage, totalCount, listCount, pageCount);

      // sql의 Limit 절에서 사용될 값을 beans 의 static 변수에 저장

      Department.setOffset(pagination.getOffset());
      Department.setListCount(pagination.getListCount());

      output = departmentService.getList(input);
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
   * 학과 상세 화면
   * 
   * @param model
   * @param deptno
   * @return 학과 상세 화면을 구현한 view 경로
   */

  @GetMapping("/api/department/{deptno}")
  public Map<String, Object> detail(Model model,
      @PathVariable("deptno") int deptno) {

    // 조회 조건에 사용할 변수를 beans에 저장
    Department input = new Department();
    input.setDeptno(deptno);

    // 조회 결과를 저장할 객체 선언
    Department output = null;

    try {
      // 데이터 조회
      output = departmentService.getItem(input);
    } catch (Exception e) {
      return restHelper.serverError(e);
    }

    // view 에 데이터 전달
    Map<String, Object> data = new LinkedHashMap<String, Object>();
    data.put("item", output);

    return restHelper.sendJson(data);
  }

  /**
   * 학과 등록처리
   * action 페이지들은 view 를 사용하지 않고 다른 페이지로 이동해야 하므로
   * 메서드 상단에 @ResponseBody 를 적용하여 view 없이 직접 응답을 구현한다
   * 
   * @param dname 학과이름
   * @param loc   학과위치
   */
  @PostMapping("/api/department")
  public Map<String, Object> addOk(HttpServletRequest request,
      @RequestParam("dname") String dname,
      @RequestParam("loc") String loc) {

    // 저장할 값을을 beans에 담는다
    Department input = new Department();
    input.setDname(dname);
    input.setLoc(loc);

    Department output = null;

    try {
      output = departmentService.addItem(input);
    } catch (Exception e) {
      return restHelper.serverError(e);
    }

    Map<String, Object> data = new LinkedHashMap<String, Object>();
    data.put("item", output);

    return restHelper.sendJson(data);
  }

  /**
   * 학과 삭제 처리
   * 
   * @param deptno 학과번호
   */
  @DeleteMapping("/api/department/{deptno}")
  public Map<String, Object> delete(HttpServletRequest request,
      @PathVariable("deptno") int deptno) {
    // 이전 페이지 경로 검사 --> 정상적인 경로로 접근했는지 여부 확인
    String referer = request.getHeader("referer");

    Department input = new Department();
    input.setDeptno(deptno);

    try {
      departmentService.deleteItem(input);
    } catch (Exception e) {
      restHelper.serverError(e);
    }

    return restHelper.sendJson();
  }

  /**
   * 수정처리
   * 
   * @param deptno
   * @param dname
   * @param loc
   */
  @PutMapping("/api/department/{deptno}")
  public Map<String, Object> edit_ok(
      @PathVariable("deptno") int deptno,
      @RequestParam("dname") String dname,
      @RequestParam("loc") String loc) {

    // 수정에 사용돌 값을 beans에 담는다
    Department input = new Department();
    input.setDeptno(deptno);
    input.setDname(dname);
    input.setLoc(loc);

    Department output = null;

    // 데이터를 수정한다
    try {
      output = departmentService.editItem(input);
    } catch (Exception e) {
      return restHelper.serverError(e);
    }

    Map<String, Object> data = new LinkedHashMap<String, Object>();
    data.put("item", output);

    return restHelper.sendJson(data);
  }
}
