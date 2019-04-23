package com.github.soyanga.spel.pojo;

import com.github.soyanga.spel.pojo.PlaceOfBirth;
import lombok.Data;

import java.util.Date;

/**
 * @program: springcore-case-spel
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-16 00:35
 * @Version 1.0
 */
@Data
public class User {
    /**
     *    
     * 用户名    
     */
    private String userName;
    /**
     *     * 近访问时间    
     */
    private Date lastVisit;
    /**
     *     * 用户积分    
     */
    private Integer credits;
    /**
     *     * 用户出生地    
     */
    private PlaceOfBirth placeOfBirth;
}
