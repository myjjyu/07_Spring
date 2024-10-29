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

        /**
         * 학생 정보를 입력한다
         * 
         * @param input 입력할 학생 정보에 대한 모델객체
         * @return 입력된 데이터수
         */
        @Insert("INSERT INTO student(" +
                        "name, userid, grade, idnum, birthdate," +
                        "tel, height, weight, deptno, profno" +
                        ") VALUES (" +
                        "#{name}, #{userid}, #{grade}, #{idnum}, #{birthdate}," +
                        "#{tel}, #{height}, #{weight}, #{deptno}, #{profno}" +
                        ")")

        @Options(useGeneratedKeys = true, keyProperty = "studno")
        int insert(Student input);

        /**
         * 학생 정보를 수정한다
         * 
         * @param input
         * @return
         */

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

        /**
         * 학생 정보를 삭제한다
         * 
         * @param input
         * @return
         */
        @Delete("DELETE FROM student WHERE studno=#{studno}")
        int delete(Student input);

        /**
         * 학과를 삭제하기 전에 학과에 소속된 학생 데이터를 삭제
         * 
         * @param input
         * @return
         */
        @Delete("DELETE FROM student WHERE deptno=#{deptno}")
        int deleteByDeptno(Student input);

        /**
         * 교수를 삭제하기 전에 교수에게 소속된 학생들과의 연결을 해제
         * --> profno 컬럼이 null 허용으로 설정되야 함
         * 
         * @param input
         * @return
         */
        @Update("UPDATE student SET profno = Null WHERE profno = #{profno}")
        int updateByProfno(Student input);

        /**
         * 학생 한명 조회(단일행 조회를 수행하는 메서드 정의)
         * 
         * @param input
         * @return
         */
        @Select("SELECT " +
                        "studno, s.name AS name, s.userid AS userid, grade, idnum, " +
                        "DATE_FORMAT(birthdate, '%Y-%m-%d') AS birthdate, " +
                        "tel, height, weight, p.name AS pname, d.dname AS dname, " + // dname과 pname 가져오기
                        "s.deptno AS deptno, s.profno AS profno " + // deptno와 profno를 명확하게 선택
                        "FROM student s " +
                        "INNER JOIN department d ON s.deptno = d.deptno " +
                        "LEFT OUTER JOIN professor p ON s.profno = p.profno " +
                        "WHERE studno = #{studno}")
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
                        @Result(property = "deptno", column = "deptno"), // deptno 매핑
                        @Result(property = "profno", column = "profno"), // profno 매핑
                        @Result(property = "dname", column = "dname"), // dname 매핑
                        @Result(property = "pname", column = "pname") // pname 매핑
        })
        public Student selectItem(Student input);

        /**
         * 다중행 조회
         * 소속된 학과 이름과 담당 교수 이름을 포함
         * 
         * @param input 검색 조건을 포함한 Student 객체
         * @return 조회된 Student 리스트
         */
        @Select("<script>" +
                        "SELECT " +
                        "studno, s.name AS name, s.userid AS userid, grade, idnum, " +
                        "DATE_FORMAT(birthdate, '%Y-%m-%d') AS birthdate, " +
                        "tel, height, weight, " + // 학생 정보 선택
                        "d.dname AS dname, p.name AS pname " + // 학과 이름과 교수 이름 선택
                        "FROM student s " +
                        "INNER JOIN department d ON s.deptno = d.deptno " + // 학과 테이블과 결합
                        "LEFT OUTER JOIN professor p ON s.profno = p.profno " +
                        "<where>" +
                        "<if test='name != null'>s.name LIKE concat('%', #{name}, '%')</if>" + // 이름 조건
                        "<if test='userid != null'> OR s.userid LIKE concat('%', #{userid}, '%')</if>" + // 사용자 ID 조건
                        "</where>" +
                        "ORDER BY studno DESC" + // 학번 기준 내림차순 정렬
                        "<if test='listCount > 0'>LIMIT #{offset}, #{listCount}</if>" + // 페이징 처리
                        "</script>")
        @ResultMap("studentMap")
        List<Student> selectList(Student input);

        /**
         * 검색 결과의 수를 조회하는 메서드
         * 목록 조회와 동일한 검색 조건을 적용해야 한다
         * 
         * @param input 조회 조건을 담고 있는 Student 객체
         * @return 검색 결과 수
         */
        @Select("<script> " +
                        "SELECT COUNT(*) AS cnt " +
                        "FROM student s " +
                        "INNER JOIN department d ON s.deptno = d.deptno " +
                        // department 테이블을 d라는 별칭으로 지정하고, 학생의 deptno와 학과의 deptno를 기준으로 결합
                        "LEFT OUTER JOIN professor p ON s.profno = p.profno " +
                        // professor 테이블을 p라는 별칭으로 지정하고, 학생의 profno와 교수의 profno를 기준으로 결합
                        // -->이로 인해 학생에게 담당 교수의 정보를 포함할 수 있다
                        // --> null 값도 가져오려고 LEFT OUTER JOIN 사용
                        "<where>" +
                        "<if test='name != null'>s.name LIKE concat('%', #{name}, '%')</if>" + // 이름 조건
                        "<if test='userid != null'> OR s.userid LIKE concat('%', #{userid}, '%')</if>" + // 사용자 ID 조건
                        "</where>" +
                        "</script>")
        public int selectCount(Student input);
}
