package kr.gilju.crud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.gilju.crud.interceptors.MyInterceptor;

@Configuration
@SuppressWarnings("null")
public class MyWebConfig implements WebMvcConfigurer {

  //MyInterceptor 객체를 자동 주입 받는다
  // 이 과정에서 MyInterceptor안에 @autowired로 선언된 유틸헬퍼 객체도 자동 주입된다

  @Autowired
  private MyInterceptor myInterceptor;


  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    //직접 정의한 myinterceptor를 spring에 등록
    InterceptorRegistration ir = registry.addInterceptor(myInterceptor);
    // 해당경로는 인터셉터가 가로채지 않는다 
    ir.excludePathPatterns("/error", "/robots.txt", "/favicon.ico", "/assets/**");
  }
}