package kr.gilju.myshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class MyshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyshopApplication.class, args);
	}

}
