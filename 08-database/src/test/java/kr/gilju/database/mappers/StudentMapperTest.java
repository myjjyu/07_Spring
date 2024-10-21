package kr.gilju.database.mappers;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.gilju.database.models.Student;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class StudentMapperTest {

  @Autowired
  private StudentMapper studentMapper;

  @Test
  @DisplayName("학생 추가 테스트")
  void insertStudent() {
    Student input = new Student();
    input.setName("김이삼");
    input.setUserid("kim123");
    input.setGrade(3);
    input.setIdnum("1234567890123");
    input.setBirthdate("2000-03-15");
    input.setTel("010-1234-5678");
    input.setHeight(175);
    input.setWeight(65);
    input.setDeptno(102);
    input.setProfno(null);

    int output = studentMapper.insert(input);

    log.debug("output: " + output);
    log.debug("new profno: " + input.getStudno());
  }

  @Test
  @DisplayName("학생 수정 테스트")
  void updateStudent() {
    Student input = new Student();
    input.setStudno(20106); // 수정할 학생 학번
    input.setName("김수정");
    input.setUserid("soo1234");
    input.setGrade(4);
    input.setIdnum("987654-3210987");
    input.setBirthdate("1999-12-01");
    input.setTel("010-9876-5432");
    input.setHeight(180);
    input.setWeight(70);
    input.setDeptno(103);
    input.setProfno(null);

    int output = studentMapper.update(input);

    log.debug("output: " + output);
  }

  @Test
  @DisplayName("학생 삭제 테스트")
  void deleteStudent() {
    Student input = new Student();
    input.setStudno(20106); // 삭제할 학생 학번

    int output = studentMapper.delete(input);
    log.debug("output: " + output);
  }

  @Test
  @DisplayName("한 명의 학생 조회 테스트")
  void selectOneStudent() {
    Student input = new Student();
    input.setStudno(10202); // 조회할 학생 학번

    Student output = studentMapper.selectItem(input);

    log.debug("output: " + output.toString());
  }

  @Test
  @DisplayName("학생 목록 조회 테스트")
  void selectListStudent() {
    List<Student> output = studentMapper.selectList(null);

    for (Student item : output) {
      log.debug("output: " + item.toString());
    }
  }
}
