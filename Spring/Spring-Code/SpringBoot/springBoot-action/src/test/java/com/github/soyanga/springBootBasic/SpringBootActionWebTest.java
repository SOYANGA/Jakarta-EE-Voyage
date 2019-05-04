//package com.github.soyanga.springBootBasic;
//
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.web.client.RestTemplate;
//
///**
// * @program: springBoot-action
// * @Description: springBoot web测试
// * @Author: SOYANGA
// * @Create: 2019-05-04 17:58
// * @Version 1.0
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//public class SpringBootActionWebTest {
//    @Autowired
//    private TestRestTemplate testrestTemplate;
//
//    @Test
//    public void test1() {
//        ResponseEntity<String> stringResponseEntity = testrestTemplate.getForEntity("http://localhost:80/mail/simpleMail1", String.class);
//        Assert.assertEquals(HttpStatus.OK, stringResponseEntity.getStatusCode());
//        Assert.assertEquals("success", stringResponseEntity.getBody());
//    }
//
//    @Test
//    public void test2() {
//        ResponseEntity<String> stringResponseEntity = testrestTemplate.getForEntity("http://localhost:80/mail/simpleMail2", String.class);
//        Assert.assertEquals(HttpStatus.OK, stringResponseEntity.getStatusCode());
//        Assert.assertEquals("success", stringResponseEntity.getBody());
//    }
//
//    @Test
//    public void test3() {
//        ResponseEntity<String> stringResponseEntity = testrestTemplate.getForEntity("http://localhost:80/mail//multipartMail", String.class);
//        Assert.assertEquals(HttpStatus.OK, stringResponseEntity.getStatusCode());
//        Assert.assertEquals("success", stringResponseEntity.getBody());
//    }
//
//    @Test
//    public void test4() {
//        ResponseEntity<String> stringResponseEntity = testrestTemplate.getForEntity("http://localhost:80/mail/innerMail", String.class);
//        Assert.assertEquals(HttpStatus.OK, stringResponseEntity.getStatusCode());
//        Assert.assertEquals("success", stringResponseEntity.getBody());
//    }
//}
