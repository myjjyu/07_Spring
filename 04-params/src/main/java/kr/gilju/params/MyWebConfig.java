package kr.gilju.params;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.gilju.params.interceptors.MyInterceptor;

@Configuration
@SuppressWarnings("null")
public class MyWebConfig implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    //직접 정의한 myinterceptor를 spring에 등록
    InterceptorRegistration ir = registry.addInterceptor(new MyInterceptor());
    // 해당경로는 인터셉터가 가로채지 않는다 
    ir.excludePathPatterns("/error", "/robots.txt", "/favicon.ico", "/assets/**");
  }
}