package com.yqh.falcon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan(basePackages = "com.yqh")
@MapperScan({"com.yqh.falcon.mapper"})
public class FalconApplication {

    public static void main(String[] args) {
        SpringApplication.run(FalconApplication.class, args);
    }

}
