package com.soyanga.mybatis.mapper;

import com.soyanga.mybatis.entity.ScottGroup;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @program: mybatis-case
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-03-28 23:30
 * @Version 1.0
 */
public interface ScottGroupMapper {

    //    @Select(value = {"select * from dept;"})
    List<ScottGroup> query();

    int insertScottGroup(ScottGroup scottGroup);
}
