package com.github.soyanga.springBootBasic;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;


/**
 * 入口类一般放在根包底下
 *
 * @author jiujiu糖
 */
@SpringBootApplication()

@ImportResource(locations = {"classpath:application-bean.xml"})
public class SpringBootActionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootActionApplication.class, args);
    }
}
