package com.github.soyanga.springBootBasic;


import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ImportResource;


/**
 * 入口类一般放在根包底下
 */
//添加自动扫描包，com.github.soyanga.springBootDemoa.control
//禁止DataSourceAutoConfiguration自动注入
@SpringBootApplication(

//        scanBasePackages = {
//                "com.github.soyanga.springBootDemo1.control"
//        },
//        exclude = {
//                DataSourceAutoConfiguration.class
//        }
)
@ImportResource(locations = {"classpath:application-bean.xml"})
public class SpringBootDemo1Application {

    public static void main(String[] args) {
//        ApplicationContext context = SpringApplication.run(SpringBootDemo1Application.class, args);
//
//        ExampleBean2 exampleBean2 = context.getBean(ExampleBean2.class);
//        System.out.println(exampleBean2);

//        SpringApplication application = new SpringApplication(SpringBootDemo1Application.class);
//        application.setBannerMode(Banner.Mode.OFF); //取消Banner
//        application.run(args);

        //Fluent API
        new SpringApplicationBuilder(SpringBootDemo1Application.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }
}
