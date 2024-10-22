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

import kr.gilju.database.models.Professor;

@Mapper
public interface ProfessorMapper {

  @Insert("INSERT INTO professor(" +
      "name, userid, position, sal, hiredate, comm, deptno" +
      ") VALUES (" +
      "#{name}, #{userid}, #{position}, #{sal}, #{hiredate}, #{comm}, #{deptno}" +
      ")")
  @Options(useGeneratedKeys = true, keyProperty = "profno", keyColumn = "profno")
  public int insert(Professor input);

  @Update("UPDATE professor SET " +
      "name=#{name}," +
      "userid=#{userid}," +
      "position=#{position}," +
      "sal=#{sal}," +
      "hiredate=#{hiredate}," +
      "comm=#{comm}," +
      "deptno=#{deptno} " +
      "WHERE profno=#{profno}")
  int update(Professor input);

  @Delete("DELETE FROM professor WHERE profno=#{profno};")
  public int delete(Professor input);

  @Delete("DELETE FROM professor WHERE deptno=#{deptno};")
  public int deleteByDeptno(Professor input);

  @Select("SELECT " +
      "profno, name, userid, position, sal," +
      "DATE_FORMAT(hiredate, '%Y-%m-%d') AS hiredate, comm, deptno " +
      "FROM professor p " +
      "WHERE profno=#{profno}")
  @Results(id = "professorMap", value = {
      @Result(property = "profno", column = "profno"),
      @Result(property = "name", column = "name"),
      @Result(property = "userid", column = "userid"),
      @Result(property = "position", column = "position"),
      @Result(property = "sal", column = "sal"),
      @Result(property = "hiredate", column = "hiredate"),
      @Result(property = "comm", column = "comm"),
      @Result(property = "deptno", column = "deptno")
  })
  Professor selectItem(Professor input);

  @Select("SELECT " +
      "profno, name, userid, position, sal," +
      "DATE_FORMAT(hiredate, '%Y-%m-%d') AS hiredate, comm, deptno " +
      "FROM professor p ")
  @ResultMap("professorMap")
  public List<Professor> selectList(Professor input);
}
