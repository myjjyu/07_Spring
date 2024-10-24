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

import kr.gilju.database.models.Student;

@Mapper
public interface StudentMapper {

  // 학생 등록
  @Insert("INSERT INTO student(" +
      "name, userid, grade, idnum, birthdate," +
      "tel, height, weight, deptno, profno" + 
      ") VALUES (" +
      "#{name}, #{userid}, #{grade}, #{idnum}, #{birthdate}," +
      "#{tel}, #{height}, #{weight}, #{deptno}, #{profno}" +
      ")")

  @Options(useGeneratedKeys = true, keyProperty = "studno")
  int insert(Student input);

  // 학생 정보 수정
  @Update("UPDATE student SET " +
      "name=#{name}," +
      "userid=#{userid}, " +
      "grade=#{grade}," +
      "idnum=#{idnum}, " +
      "birthdate=#{birthdate}, " +
      "tel=#{tel}," +
      "height=#{height}," +
      "weight=#{weight}, " +
      "deptno=#{deptno}," +
      "profno=#{profno} " +
      "WHERE studno=#{studno}")
  int update(Student input);

  // 학생 삭제
  @Delete("DELETE FROM student WHERE studno=#{studno}")
  int delete(Student input);

  // 학과를 삭제하기 전에 학과에 소속된 학생 데이터를 삭제 
  @Delete("DELETE FROM student WHERE deptno=#{deptno}")
  int deleteByDeptno(Student input);

  // 교수를 삭제하기 전에 교수에게 소속된 학생들과의 연결을 해제
  // --> profno 컬럼이 null 허용으로 설정되야 함
  @Update("UPDATE student SET profno = Null WHERE profno = #{profno}")
  int updateByProfno(Student input);

  // 학생 한 명 조회
  @Select("SELECT studno, name, userid, grade, idnum, " +
      "DATE_FORMAT(birthdate, '%Y-%M-%d')as birthdate, " +
      "tel, height, weight, deptno, profno " +
      "FROM student WHERE studno=#{studno}")
  @Results(id = "studentMap", value = {
      @Result(property = "studno", column = "studno"),
      @Result(property = "name", column = "name"),
      @Result(property = "userid", column = "userid"),
      @Result(property = "grade", column = "grade"),
      @Result(property = "idnum", column = "idnum"),
      @Result(property = "birthdate", column = "birthdate"),
      @Result(property = "tel", column = "tel"),
      @Result(property = "height", column = "height"),
      @Result(property = "weight", column = "weight"),
      @Result(property = "deptno", column = "deptno"),
      @Result(property = "profno", column = "profno")
  })
  Student selectItem(Student input);

  // 학생 목록 조회
  @Select("SELECT studno, name, userid, grade, idnum, " +
      "DATE_FORMAT(birthdate, '%Y-%M-%d')as birthdate, " +
      "tel, height, weight, deptno, profno " +
      "FROM student")
  @ResultMap("studentMap")
  List<Student> selectList(Student input);
}
