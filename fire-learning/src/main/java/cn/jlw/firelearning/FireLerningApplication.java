package cn.jlw.firelearning;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@MapperScan(basePackages = "cn.jlw.firelearning.mapper")
public class FireLerningApplication {

    public static void main(String[] args) {
        SpringApplication.run(FireLerningApplication.class, args);
    }

}
