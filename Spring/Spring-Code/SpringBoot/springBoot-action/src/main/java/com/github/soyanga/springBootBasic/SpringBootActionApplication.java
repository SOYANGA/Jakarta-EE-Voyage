package com.github.soyanga.springBootBasic;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;


/**
 * 入口类一般放在根包底下
 *
 * @author jiujiu糖
 */
@SpringBootApplication()

@ImportResource(locations = {"classpath:application-bean.xml"})
public class SpringBootActionApplication extends SpringBootServletInitializer {

    //Tomcat中会调用该方法启动
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SpringBootActionApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootActionApplication.class, args);
    }
}


//@SpringBootApplication()
//
//@ImportResource(locations = {"classpath:application-bean.xml"})
//public class SpringBootActionApplication {
//
//    public static void main(String[] args) {
//        SpringApplication.run(SpringBootActionApplication.class, args);
//    }
//}