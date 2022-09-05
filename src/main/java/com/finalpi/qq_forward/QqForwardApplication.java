package com.finalpi.qq_forward;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.finalpi.qq_forward.mapper")

public class QqForwardApplication {

    public static void main(String[] args) {
        SpringApplication.run(QqForwardApplication.class, args);
    }

}
