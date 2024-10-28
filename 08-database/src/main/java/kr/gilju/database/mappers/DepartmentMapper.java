package kr.gilju.database.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.gilju.database.models.Department;

@Mapper
public interface DepartmentMapper {

  /**
   * 학과 정보를 입력한다
   * pK값은 파라미터로 전달된 인풋 객체에 참조로 처리된다
   * 
   * @param input 입력할 학과 정보에 대한 모델 객체
   * @return 입력된 데이터 수
   */

  @Insert("INSERT INTO department (dname, loc) VALUES (#{dname}, #{loc})")
  // INSERT 문에서 필요한 PK에 대한 옵션 정의
  // --> useGeneratedKeys: auto_increment 가 적둉된 테이블인 경우 사용
  // --> keyProperty : 파라미터로 전달되는 model 객체에서 pk에 대응되는 멤버변수
  // --> keyColumn: 테이블의 primary key 컬럼명
  @Options(useGeneratedKeys = true, keyProperty = "deptno", keyColumn = "deptno")
  public int insert(Department input);

  /**
   * UPDATE문을 수행하는 메서드 정의
   * 
   * @param input 수정할 데이터에 대한 모델객체
   * @return 수정된 데이터 수
   */
  @Update("UPDATE department SET dname=#{dname}, loc=#{loc} WHERE deptno=#{deptno}")
  public int update(Department input);

  /**
   * DELETE 문을 수행하는 메서드 정의
   * 
   * @param input
   * @return
   */
  @Delete("DELETE FROM department WHERE deptno=#{deptno};")
  public int delete(Department input);

  /**
   * 단일행 조회를 수행하는 메서드 정의
   * 
   * @param input 조회할 데이터에 대한 모델 객체
   * @return 조회된 데이터
   */
  @Select("SELECT deptno, dname, loc FROM department WHERE deptno=#{deptno}")
  // 죄회 결과와 리턴할 모델 객체를 연결하기 위한 규칙 정의
  // -->property 모델객체의 멤버변수 이름
  // -->column 셀렉트문에 명시된 필드이름 (as 옵션을 사용한 경우 별칭으로 명시)
  @Results(id = "departmentMap", value = {
      @Result(property = "deptno", column = "deptno"),
      @Result(property = "dname", column = "dname"),
      @Result(property = "loc", column = "loc")
  })
  public Department selectItem(Department input);

  /**
   * 다중행 조회를 수행하는 메서드 정의
   * 
   * @param input
   * @return
   */
  @Select("<script>" +
      "SELECT deptno, dname, loc FROM department " +
      "<where>" +
      "<if test='dname != null'>dname LIKE concat('%', #{dname}, '%')</if>" +
      "<if test='loc != null'>OR loc LIKE concat('%', #{loc}, '%')</if>" +
      "</where>" +
      "ORDER BY deptno DESC" +
      "<if test='listCount > 0'>LIMIT #{offset}, #{listCount}</if>" +
      "</script>")

  // 조회 결과와 모델 의 맵핑이 이전 규칙과 동일한 경우 id값으로 이전 규칙을 재사용
  @ResultMap("departmentMap")
  public List<Department> selectList(Department input);


  /**
   * 검색 결과의 수를 조회하는 메서드
   * 목록 조회와 동일한 검색 조건을 적용해야 한다
   * @param input 조회 조건을 담고있는 객체 
   * @return 조회 결과 수
   */
  @Select("<script>" +
      "SELECT COUNT(*) AS cnt FROM department " +
      "<where>" +
      "<if test='dname != null'>dname LIKE concat('%', #{dname}, '%')</if>" +
      "<if test='loc != null'>OR loc LIKE concat('%', #{loc}, '%')</if>" +
      "</where>" +
      "</script>")

      public int selectCount(Department input);

}