package com.zs.captcha;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author word
 */
@SpringBootApplication
@MapperScan("com.zs.captcha.mapper")
public class CaptchaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CaptchaApplication.class, args);
    }

}
