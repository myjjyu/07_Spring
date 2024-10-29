package kr.gilju.restfulapi.controllers;

import java.io.Console;
import java.util.LinkedHashMap;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class RestCalcController {




  @GetMapping("/my_calc")
  public Map<String, Object> getcalc(
      HttpServletResponse response,
      @RequestParam("x") int x,
      @RequestParam("y") int y) {

    // 3) 제이슨으로 변환될 map객체 구성
    Map<String, Object> result = new LinkedHashMap<String, Object>();

    result.put("x", x);
    result.put("y", y);
    result.put("result", x + y); // 더하기

    return result;
  }



  @PostMapping("/my_calc")
  public Map<String, Object> postcalc(
      HttpServletResponse response,
      @RequestParam("x") int x,
      @RequestParam("y") int y) {

    // 3) 제이슨으로 변환될 map객체 구성
    Map<String, Object> result = new LinkedHashMap<String, Object>();

    result.put("x", x);
    result.put("y", y);
    result.put("result", x + y); // 더하기

    return result;
  }

  @PutMapping("/my_calc")
  public Map<String, Object> putcalc(
      HttpServletResponse response,
      @RequestParam("x") int x,
      @RequestParam("y") int y) {

    // 3) 제이슨으로 변환될 map객체 구성
    Map<String, Object> result = new LinkedHashMap<String, Object>();

    result.put("x", x);
    result.put("y", y);
    result.put("result", x * y); //곱하기

    return result;
  }

  @DeleteMapping("/my_calc")
  public Map<String, Object> Deletecalc(
      HttpServletResponse response,
      @RequestParam("x") int x,
      @RequestParam("y") int y) {

    // 3) 제이슨으로 변환될 map객체 구성
    Map<String, Object> result = new LinkedHashMap<String, Object>();

    // result.put("x", x);
    // result.put("y", y);
    // result.put("result", x / y); // 나누기

    if (y != 0) {
    } else {
      result.put("x", x);
      result.put("y", y);
      result.put("result", "0으로 나눌 수 없습니다");
    }


    return result;
  }
}
