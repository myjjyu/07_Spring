package kr.gilju.fileupload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.gilju.fileupload.interceptors.MyInterceptor;

@Configuration
@SuppressWarnings("null")
public class MyWebConfig implements WebMvcConfigurer {

  // MyInterceptor 객체를 자동 주입 받는다
  // 이 과정에서 MyInterceptor안에 @autowired로 선언된 유틸헬퍼 객체도 자동 주입된다
  @Autowired
  private MyInterceptor myInterceptor;

  /** 업로드 된 파일이 저장될 경로(applecation.properties로 부터 읽어옴) */
  @Value("${upload.dir}")
  private String uploadDir;

  /** 업로드된 파일이 노출돌 url 경로 (applecation.properties로 부터 읽어옴) */
  @Value("${upload.url}")
  private String uploadUrl;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // 직접 정의한 myinterceptor를 spring에 등록
    InterceptorRegistration ir = registry.addInterceptor(myInterceptor);
    // 해당경로는 인터셉터가 가로채지 않는다
    ir.excludePathPatterns("/error", "/robots.txt", "/favicon.ico", "/assets/**");
  }

  /** 설정파일에 명시된 업로드 저장 경로와 url상의 경로 맵핑 시킴 */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry){
    registry.addResourceHandler(String.format("%s/**", uploadUrl))
    .addResourceLocations(String.format("file://%s/", uploadDir));

  }
}