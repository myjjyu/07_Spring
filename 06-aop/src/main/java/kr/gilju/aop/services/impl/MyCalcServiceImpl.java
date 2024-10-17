package kr.gilju.aop.services.impl;

import org.springframework.stereotype.Service;

import kr.gilju.aop.services.MyCalcService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MyCalcServiceImpl implements MyCalcService {
  public MyCalcServiceImpl() {
    log.debug("MyCalcServiceImpl() 생성자 호출됨!");
  }

  @Override
  public int plus(int x, int y) {
    return x + y;
  }

  @Override
  public int minus(int x, int y) {
    return x - y;
  }
}
