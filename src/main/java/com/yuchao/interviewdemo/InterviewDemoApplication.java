package com.yuchao.interviewdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.yuchao.interviewdemo.mapper")
public class InterviewDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterviewDemoApplication.class, args);
    }

}
