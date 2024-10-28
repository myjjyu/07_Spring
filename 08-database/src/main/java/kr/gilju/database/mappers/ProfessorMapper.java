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
        /**
         * 교수 정보를 입력한다
         * 
         * @param input 입력할 교수 정보에 대한 모델 객체
         * @return 입력된 데이터 수
         */

        @Insert("INSERT INTO professor(" +
                        "name, userid, position, sal, hiredate, comm, deptno" +
                        ") VALUES (" +
                        "#{name}, #{userid}, #{position}, #{sal}, #{hiredate}, #{comm}, #{deptno}" +
                        ")")
        @Options(useGeneratedKeys = true, keyProperty = "profno", keyColumn = "profno")
        public int insert(Professor input);

        /**
         * 교수 정보를 수정한다
         * 
         * @param input 수정할 교수 정보에 대한 모델객체
         * @return 수정 데이터 수
         */
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

        /**
         * 교수 정보 삭제
         * 
         * @param input 삭제할 교수 정보에 대한 모델 객체
         * @return 삭제된 데이터 수
         */
        @Delete("DELETE FROM professor WHERE profno=#{profno};")
        public int delete(Professor input);

        /**
         * 특정 학과에 소속된 교수를 일괄 삭제한다
         * 
         * @param input 삭제할 교수 정보데 대한 모델객체
         * @return 삭제된 데이터 수
         */
        @Delete("DELETE FROM professor WHERE deptno=#{deptno};")
        public int deleteByDeptno(Professor input);

        /**
         * 단일행 조회를 수행하는 메서드 정의
         * 소속된 학과 이름을 포함한다
         * 
         * @param input 조회할 데이터에 대한 모델객체
         * @return 조회된 데이터
         */
        @Select("SELECT " +
                        "profno, name, userid, position, sal," +
                        "DATE_FORMAT(hiredate, '%Y-%m-%d') AS hiredate, comm, deptno " +
                        "FROM professor p " +
                        "INNER JOIN department d ON p.deptno = d.deptno " +
                        "WHERE profno=#{profno}")
        @Results(id = "professorMap", value = {
                        @Result(property = "profno", column = "profno"),
                        @Result(property = "name", column = "name"),
                        @Result(property = "userid", column = "userid"),
                        @Result(property = "position", column = "position"),
                        @Result(property = "sal", column = "sal"),
                        @Result(property = "hiredate", column = "hiredate"),
                        @Result(property = "comm", column = "comm"),
                        @Result(property = "deptno", column = "deptno"),
                        @Result(property = "dname", column = "dname"),})
        // property= 멤버변수 ,column= 셀렉트 결과
        Professor selectItem(Professor input);

        /**
         * 다중행 조회를 수행하는 메서드 정의
         * 소속된 학과 이름을 포함한다
         * 
         * @param input 조회할 데이터에 대한 모델 객체
         * @return 조회된 데이터
         */
        @Select("<script>" +
                        "SELECT " +
                        "profno, name, userid, position, sal," +
                        "DATE_FORMAT(hiredate, '%Y-%m-%d') AS hiredate, comm," +
                        "p.deptno as deptno, dname " +
                        "FROM professor p " +
                        "INNER JOIN department d ON p.deptno = d.deptno " +
                        "<where>" +
                        "<if test='name != null'>name LIKE concat('%', #{name}, '%')</if>" +
                        "<if test='userid != null'>OR userid LIKE concat('%', #{userid}, '%')</if>" +
                        "</where>" +
                        "ORDER BY profno DESC" +
                        "<if test='listCount > 0'>LIMIT #{offset}, #{listCount}</if>" +
                        "</script>")
        @ResultMap("professorMap")
        List<Professor> selectList(Professor input);

        /**
         * 검색결과의 수를 조회하는 메서드
         * 목록 조회와 동일한 검색 조건을 적용해야 한다
         * 
         * @param input 조회 조건을 담고 있는 객체
         * @return 조회 결과 수
         */
        @Select("<script>" +
                        "SELECT COUNT(*) AS cnt " +
                        "FROM professor p " +
                        "INNER JOIN department d ON p.deptno = d.deptno " +
                        "<where>" +
                        "<if test='name != null'>name LIKE concat('%', #{name}, '%')</if>" +
                        "<if test='userid != null'>OR userid LIKE concat('%', #{userid}, '%')</if>" +
                        "</where>" +
                        "</script>")

        public int selectCount(Professor input);
}
