package kr.gilju.database.mappers;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.gilju.database.models.Professor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@SpringBootTest
public class ProfessorMapperTest {
  

  @Autowired
  private ProfessorMapper professorMapper;
  

  @Test
  @DisplayName("교수 추가 테스트")
  void insertProfessor() {
    Professor input = new Professor();
    input.setName("헬로월드");
    input.setUserid("helloworld");
    input.setPosition("강사");
    input.setSal(500000);
    input.setHiredate("2023-04-11");
    input.setComm(1000000);
    input.setDeptno(102);

    int output = professorMapper.insert(input);

    log.debug("output: " + output);
    log.debug("new Profno: " + input.getProfno());
  }
  
  @Test
  @DisplayName("교수 수정 테스트")
  void updateProfessor() {
    Professor input = new Professor();
    input.setProfno(9902);
    input.setName("헬로월드");
    input.setUserid("helloworld");
    input.setPosition("강사");
    input.setSal(500000);
    input.setHiredate("2023-04-11");
    input.setComm(1000000);
    input.setDeptno(102);

    int output = professorMapper.update(input);

    log.debug("output: " + output);
  }

  @Test
  @DisplayName("교수 삭제 테스트")
  void deleteProfessor() {
    Professor input = new Professor();
    input.setDeptno(202);

    int output = professorMapper.delete(input);
    log.debug("output: " + output);
  }

  @Test
  @DisplayName("하나의 학과 조회 테스트")
  void selectOneProfessor() {
    Professor input = new Professor();
    input.setProfno(9902);

    Professor output = professorMapper.selectItem(input);
    log.debug("output: " + output.toString());
  }

  @Test
  @DisplayName("하나의 목록 조회 테스트")
  void selectListProfessor() {
    List<Professor> output = professorMapper.selectList(null);

    for (Professor item : output) {
      log.debug("output: " + item.toString()); 
    }
  }
}
