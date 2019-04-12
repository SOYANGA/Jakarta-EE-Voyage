package com.soyanga.mybatis.mapper;


import com.soyanga.mybatis.common.Page;
import com.soyanga.mybatis.entity.ScottEmp;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: mybatis-case
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-03 19:46
 * @Version 1.0
 */
public interface ScottEmpMapper {
    //插入
    int insertScottEMp(ScottEmp scottEmp);

    //更新
    int updateScottEmp(@Param("empno") int empno, @Param("hiredate") Date hiredate);

    //更新
    int updateScottEmpByObject(ScottEmp scottEmp);

    //删除
    int deleteScottEmpByEmpno(int empno);

    //查询
    //根据列的别名和属性名对应返回ScottEmp对象
    ScottEmp queryScottEmpByEmpno(int empno);

    //根据列的别名和属性名对应返回ScottEmp对象 返回Map对象
    Map queryScottEmpByEmpnoReturnHashMap(int empno);

    //返回所有（ScottEmp对象集合）
    List<ScottEmp> queryScottEmpAll();

    /**
     * @return 返回所有（Map对象集合）
     */
    List<Map> queryScottAllReturnHashMap();

    /**
     * @param pageSize        页面有多少个数据
     * @param pageOffset      相对于第一个数据的偏移量（页面偏移量）
     * @param orderColumnName 排序升序或者降序
     * @return 查询结果
     */
    //按照 某一字段 倒叙，分页查询便签信息，每一页是固定条数  页数，每页条数
    List<ScottEmp> queryScottEmpByDivPage(
            @Param("pageSize") Integer pageSize,
            @Param("pageOffset") Integer pageOffset,
            @Param("orderColumnName") String orderColumnName
    );

    //PageHelper实现分页查询方式3
    List<ScottEmp> queryScottEmpByDivPage3(
            @Param("pageSize") Integer pageSize,
            @Param("pageNum") Integer pageNum
    );


    /**
     * SQL分页查询 抽象分页对象
     *
     * @param page 页面对象 其中包含上述属性
     * @return 查询结果
     */
    List<ScottEmp> queryScottEmpByDivPageObject(Page page);

    /**
     * if的使用
     * where deptno = 20 and job = CLICK
     *
     * @param job job最为if中的条件 即 where后面的增加的附加条件
     * @return 查询结果
     */
    List<ScottEmp> queryScottEmpWhithdeptnoAndLikeJob(@Param("job") String job);

    /**
     * 多重if
     * where deptno = 20 and job = CLICK and ename = Jack
     *
     * @param job
     * @param ename job ename 均为where后的附加条件
     * @return 查询结果
     */
    List<ScottEmp> queryScottEmpWhithdeptnoAndLikeJobAndLikeEname(
            @Param("job") String job,
            @Param("ename") String ename
    );

    /**
     * where的使用
     * deptni job ename均作为 where后面的条件
     *
     * @param deptno
     * @param job
     * @param ename
     * @return 查询结果
     */

    List<ScottEmp> queryScottEmpWhithdeptnoAndLikeJobAndLikeEname2(
            @Param("deptno") Integer deptno,
            @Param("job") String job,
            @Param("ename") String ename
    );

    /**
     * choose when otherwise 使用相当于 SwitchCase的使用 choose-Switch when-case otherwise = default
     *
     * @param job
     * @param ename
     * @return 查询结果
     */
    List<ScottEmp> queryScottEmpWhithDeptnoAndLikeJobOrEname(
            @Param("job") String job,
            @Param("ename") String ename
    );


    //动态更新 set用法
    int updateScottEmpByObjectWithSet(ScottEmp scottEmp);


    /**
     * 动态 foreach 用list集合遍历做参数 in(？，？，？)用法
     *
     * @param deptnos
     * @return 查询结果
     */
    List<ScottEmp> queryScottempByList(List deptnos);

    /**
     * 动态 foreach 用Array（数组）集合遍历做参数 in(？，？，？)用法
     *
     * @param deptnos
     * @return 查询结果
     */
    List<ScottEmp> queryScottempByArray(Integer[] deptnos);

    /**
     * 动态 foreach 用Map集合遍历做参数 in(？，？，？)用法
     * where ename like '%J%' and id in (?,?,?)
     *
     * @param deptnoMap
     * @return 查询结果
     */
    List<ScottEmp> queryScottempByMap(Map deptnoMap);


    /**
     * 查询创建时间在startTime,endTime之间的便签信息
     *
     * @return
     */
    List<ScottEmp> queryScottempByCreatTime(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}
