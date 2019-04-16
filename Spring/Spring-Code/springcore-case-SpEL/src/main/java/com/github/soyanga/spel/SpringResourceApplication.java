package com.github.soyanga.spel;

import org.springframework.core.io.*;

import java.io.*;
import java.net.MalformedURLException;

/**
 * @program: springcore-case-SpEL
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-16 21:49
 * @Version 1.0
 */
public class SpringResourceApplication {

    //file:D:/db.properties   file:linux--> /home/lisan/db.propertoes
    // http://www.zhangsan.com/files/db.propertoes
    //classpath:com/soyanga/db.propertoes
    //classpath:db.propertoes

    /**
     * file:D:/db.properties   file:linux--> /home/lisan/db.propertoes
     * http://www.zhangsan.com/files/db.propertoes
     * classpath:com/soyanga/db.propertoes
     * classpath:db.propertoes
     * 通过文件名去判定资源所属的具体的Resource类
     *
     * @param filename 如上
     * @return 文件加载输入流
     */
    public static Resource loadResource(String filename) {
        if (filename == null || filename.length() == 0) {
            return null;
        }
        if (filename.startsWith("file:")) {
            return new FileSystemResource(filename.substring("file:".length()));
        }
        if (filename.startsWith("classpath:")) {
            return new ClassPathResource(filename.substring("classpath:".length()));

        }
        if (filename.startsWith("http:") || filename.startsWith("ftp:")) {
            try {
                return new UrlResource(filename);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {
//        Resource resource = loadResource("http://www.zhangsan.com/files/db.propertoes");
//        System.out.println(resource);

        Resource resource2 = loadResource("classpath:database.properties");

        String filename = "C:\\Users\\32183\\Desktop\\database.properties";
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            inputStream = resource2.getInputStream();
            outputStream = new FileOutputStream(filename);
            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bufferLength);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
