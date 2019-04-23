package com.github.soyanga.spel.pojo;

import lombok.Data;

/**
 * @program: springcore-case-spel
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-16 00:37
 * @Version 1.0
 */
@Data
public class PlaceOfBirth {
    //国家    
    private final String nation;
    //地区    
    private final String district;

    public PlaceOfBirth(String nation, String district) {
        this.nation = nation;
        this.district = district;
    }
}
