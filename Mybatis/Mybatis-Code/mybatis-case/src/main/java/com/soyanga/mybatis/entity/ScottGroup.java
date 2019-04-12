package com.soyanga.mybatis.entity;

import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * @program: mybatis-case
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-03-28 22:15
 * @Version 1.0
 */
public class ScottGroup {
    private Integer deptno;
    private String dname;
    private String loc;

    public Integer getDeptno() {
        return deptno;
    }

    public void setDeptno(Integer deptno) {
        this.deptno = deptno;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    @Override
    public String toString() {
        return "ScottGroup{" +
                "deptno=" + deptno +
                ", dname='" + dname + '\'' +
                ", loc='" + loc + '\'' +
                '}';
    }
}
