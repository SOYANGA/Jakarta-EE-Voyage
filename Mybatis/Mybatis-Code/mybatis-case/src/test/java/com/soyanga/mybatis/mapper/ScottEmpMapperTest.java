package com.soyanga.mybatis.mapper;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.soyanga.mybatis.common.Page;
import com.soyanga.mybatis.entity.ScottEmp;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static java.time.LocalDateTime.now;

/**
 * @program: mybatis-case
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-03 20:21
 * @Version 1.0
 */
public class ScottEmpMapperTest {

    private static SqlSessionFactory sqlSessionFactory;

    private final Logger logger = LoggerFactory.getLogger(ScottEmpMapperTest.class);

    static {
        try {
            sqlSessionFactory =
                    new SqlSessionFactoryBuilder()
                            .build(
                                    Resources.getResourceAsStream("mybatis-config.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_insertScottEMp() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);
        ScottEmp scottEmp = new ScottEmp();
        scottEmp.setEmpno(1314);
        scottEmp.setEname("SOYANGA");
        scottEmp.setJob("BOSS");
        scottEmp.setMgr(10000);
        scottEmp.setSal(BigDecimal.valueOf(8888.88));
        scottEmp.setCommon(BigDecimal.valueOf(6666.66));
//        scottEmp.setDeptno(10);
        scottEmp.setHiredate(new Date());

        logger.info("Insert Before********************* :{} ", scottEmp);
        int effect = scottEmpMapper.insertScottEMp(scottEmp);
        logger.info("Insert Result = {}", effect);
        logger.info("Insert After :{} ", scottEmp);

        sqlSession.close();
    }

    @Test
    public void test_updateScottEmp() {

        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);
        int effect = scottEmpMapper.updateScottEmp(1314, new Date());
        logger.info("Update empno=1314 result = {}", effect);
        sqlSession.close();
    }

    @Test
    public void test_updateScottEmpByObject() {

        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);

        ScottEmp scottEmp = new ScottEmp();
        scottEmp.setEmpno(1314);
        scottEmp.setHiredate(new Date());
        int effect = scottEmpMapper.updateScottEmpByObject(scottEmp);
        logger.info("Update empno=1314 result = {}", effect);
        sqlSession.close();
    }

    @Test
    public void test_deleteScottEmpByEmpno() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);

        int effect = scottEmpMapper.deleteScottEmpByEmpno(1314);
        logger.info("delete empno = 1314 result = {}", effect);
        sqlSession.close();

    }

    @Test
    public void test_queryScottEmpByEmpno() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);

        ScottEmp scottEmp = scottEmpMapper.queryScottEmpByEmpno(1314);
        logger.info("queryScottEmpByEmpno result: {}", scottEmp);
        sqlSession.close();
    }

    @Test
    public void test_queryScottEmpByEmpnoReturnHashMap() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);

        Map scottemp = scottEmpMapper.queryScottEmpByEmpnoReturnHashMap(1314);
        logger.info("queryScottEmpByEmpnoReturnHashMap result: {}", scottemp);
//        System.out.println(scottemp.get("empno"));
        sqlSession.close();
    }

    @Test
    public void test_queryScottEmpAll() {
        //第一次打开SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);
        List<ScottEmp> scottEmpList = scottEmpMapper.queryScottEmpAll();
        logger.info("test_queryScottEmpAll  FirstResult: {}", scottEmpList);
        sqlSession.close();

        //第二次打开SqlSession
        sqlSession = sqlSessionFactory.openSession(true);
        scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);
        List<ScottEmp> scottEmpList2 = scottEmpMapper.queryScottEmpAll();
        logger.info("test_queryScottEmpAll SecondResult: {}", scottEmpList2);
        sqlSession.close();

        //第二次打开SqlSession
        sqlSession = sqlSessionFactory.openSession(true);
        scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);
        List<ScottEmp> scottEmpList3 = scottEmpMapper.queryScottEmpAll();
        logger.info("test_queryScottEmpAll ThirResult: {}", scottEmpList3);
        sqlSession.close();
    }

    @Test
    public void test_queryScottAllReturnHashMap() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);

        List<Map> scottEmpMap = scottEmpMapper.queryScottAllReturnHashMap();
        logger.info("test_queryScottAllReturnHashMap result: {}", scottEmpMap);
        sqlSession.close();
    }

    @Test
    public void test_queryScottEmpByDivPage() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);

        int pageSize = 2;
        String orderColumnName = "empno";
        //查询前两页 利用 (pageNumber - 1) * pageSize;取出偏移量
        for (int i = 1; i < 3; i++) {
            int pageNumber = i;
            int pageOffset = (pageNumber - 1) * pageSize;
            List<ScottEmp> scottEmpList = scottEmpMapper.queryScottEmpByDivPage(pageSize, pageOffset, orderColumnName);
            logger.info("CureentPage={} result={}", pageNumber, scottEmpList);
//            System.out.format("CureentPage= %d" + "result=" + scottEmpList, i);
        }
        sqlSession.close();
    }

    @Test
    public void test_queryScottEmpByDivPageObject() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);

        Page page = new Page();
        page.setOrderColumnName("empno");
        //查询前两页 利用 (pageNumber - 1) * pageSize;取出偏移量
        for (int i = 1; i < 3; i++) {
            page.setPageNumber(i);
            List<ScottEmp> scottEmpList = scottEmpMapper.queryScottEmpByDivPageObject(page);
            logger.info("CureentPage={} result={}", page, scottEmpList);
//            System.out.format("CureentPage= %d" + "result=" + scottEmpList, i);
        }
        sqlSession.close();
    }


    @Test
    public void test_queryScottEmpWhithdeptnoAndJob() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);
        List<ScottEmp> scottEmpList = scottEmpMapper.queryScottEmpWhithdeptnoAndLikeJob(null);
        logger.info("test_queryScottEmpWhithdeptnoAndJob  Result {}", scottEmpList);
        sqlSession.close();
    }

    @Test
    public void test_queryScottEmpWhithdeptnoAndLikeJobAndLikeEname() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);
        List<ScottEmp> scottEmpList = scottEmpMapper.queryScottEmpWhithdeptnoAndLikeJobAndLikeEname("CLERK", "JAMES");
        logger.info("test_queryScottEmpWhithdeptnoAndJob  Result {}", scottEmpList);
        sqlSession.close();
    }

    @Test
    public void test_queryScottEmpWhithdeptnoAndLikeJobAndLikeEname2() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);
        List<ScottEmp> scottEmpList = scottEmpMapper.queryScottEmpWhithdeptnoAndLikeJobAndLikeEname2(null, "CLERK", "JAMES");
        logger.info("test_queryScottEmpWhithdeptnoAndJob2  Result {}", scottEmpList);
        sqlSession.close();
    }

    @Test
    public void test_queryScottEmpWhithDeptnoAndLikeJobOrEname() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);
        List<ScottEmp> scottEmpList = scottEmpMapper.queryScottEmpWhithDeptnoAndLikeJobOrEname(null, null);
        logger.info("queryScottEmpWhithDeptnoAndLikeJobOrEname  Result {}", scottEmpList);
        sqlSession.close();
    }

    @Test
    public void test_updateScottEmpByObjectWithSet() {

        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);
        ScottEmp scottEmp = new ScottEmp();
        scottEmp.setEmpno(1314);
        scottEmp.setHiredate(new Date());
        scottEmp.setEname("JINEE");
        int effect = scottEmpMapper.updateScottEmpByObjectWithSet(scottEmp);
        logger.info("Update empno=1314 result = {}", effect);
        sqlSession.close();
    }

    @Test
    public void test_queryScottempByList() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);
        List<Integer> deptnolist = new ArrayList<Integer>();
        deptnolist.add(10);
//        deptnolist.add(20);
//        deptnolist.add(30);
        List<ScottEmp> scottEmpList = scottEmpMapper.queryScottempByList(deptnolist);
        logger.info("queryScottEmpWhithDeptnoAndLikeJobOrEname  Result {}", scottEmpList);
        sqlSession.close();
    }

    @Test
    public void test_queryScottempByArray() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);
        List<ScottEmp> scottEmpList = scottEmpMapper.queryScottempByArray(new Integer[]{10});
        logger.info("queryScottEmpWhithDeptnoAndLikeJobOrEname  Result {}", scottEmpList);
        sqlSession.close();
    }

    //TDD 测试驱动开发
    @Test
    public void test_queryScottempByMap() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);
        Map map = new HashMap();
        map.put("ename", "%J%");
        map.put("deptnos", new Integer[]{10, 20, 30});
        List<ScottEmp> scottEmpList = scottEmpMapper.queryScottempByMap(map);
        logger.info("queryScottEmpWhithDeptnoAndLikeJobOrEname  Result {}", scottEmpList);
        sqlSession.close();
    }

    /**
     * pageHelper使用的第一种方法 RowBounds方式的调用
     */
    @Test
    public void test_pageHelper() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        List<ScottEmp> scottEmps = sqlSession.selectList(
                "com.soyanga.mybatis.mapper.ScottEmpMapper.queryScottEmpAll",
                null,
                new RowBounds(0, 2)
        );
        logger.info("test_pageHelper Result {}", scottEmps);
        sqlSession.close();
    }

    /**
     * pageHelper使用的第二、三种方法 Mapper接口方式的调用，推荐这种使用方式。
     * 两种均可
     * PageHelper.startPage(2, 2);
     * PageHelper.offsetPage(2, 2);
     */
    @Test
    public void test_pageHelper2() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);
        //默认做了分页计算
//        PageHelper.startPage(2, 2);
        PageHelper.offsetPage(2, 2);
        List<ScottEmp> scottEmps = scottEmpMapper.queryScottEmpAll();
        logger.info("Result {}", scottEmps);
        sqlSession.close();
    }

    /**
     * PageHelper
     * 第四种，参数方法调用
     * 存在以下 Mapper 接口方法，你不需要在 xml 处理后两个参数
     * 只需要在mapper plugin插件配置中添加如下属性
     * 配置supportMethodsArguments=true
     */
    @Test
    public void test_queryScottEmpByDivPage3() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);
        for (int pageNum = 1; pageNum < 3; pageNum++) {
            List<ScottEmp> scottEmps = scottEmpMapper.queryScottEmpByDivPage3(2, pageNum);
            logger.info("pageNum: {}   Result {}", pageNum, scottEmps);
        }
        sqlSession.close();
    }

    /**
     * PageHelper 使用
     * 第六种，ISelect 接口方式
     * jdk6,7用法，创建接口
     */
    @Test
    public void test_queryByISelect() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);

        com.github.pagehelper.Page<Object> page = PageHelper.startPage(2, 2).doSelectPage(new ISelect() {
            @Override
            public void doSelect() {
                scottEmpMapper.queryScottEmpAll();
            }
        });
        logger.info("Current page: {}", page);
        sqlSession.close();
    }

    /**
     * PageHelper 使用
     * 第8种方式 jdk8 lambda用法
     * 也可以直接返回PageInfo，注意doSelectPageInfo方法和doSelectPage
     */
    @Test
    public void test_queryByIselect2() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);
        PageInfo pageInfo = PageHelper.startPage(2, 2).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                scottEmpMapper.queryScottEmpAll();
            }
        });

        //JDK 8lambda
//        PageInfo pageInfo2 = PageHelper.startPage(2, 2).doSelectPageInfo(() -> scottEmpMapper.queryScottEmpAll());
        logger.info("Current page: {}", pageInfo);
        sqlSession.close();
    }

    /**
     * PageHelper 使用
     * 第7种方式
     * jdk8 lambda用法 jdk6,7用法，创建接口
     */
    @Test
    public void test_queryByLambda() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);
        com.github.pagehelper.Page page = PageHelper.startPage(1, 2).doSelectPage(() -> scottEmpMapper.queryScottEmpAll());
        logger.info("Current page: {}", page);
        sqlSession.close();
    }

    /**
     * PageHelper 中关于查询结果个数Count 的个数
     * 动态SQL查询使用的查询 结果Count 的数目
     */
    @Test
    public void test_queryResultCount() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);
        long count = PageHelper.count(new ISelect() {
            @Override
            public void doSelect() {
                scottEmpMapper.queryScottEmpAll();
            }
        });
        //JDK8 Lambda
//        long count2 = PageHelper.count(() -> scottEmpMapper.queryScottEmpAll());
        logger.info("Count : {}", count);
    }


    /**
     * 查询创建时间在startTime,endTime之间的便签信息
     * select * from emp where hiredate >= startTime and hiredate <= endTime
     * select * form emp  where hiredate between startTime and endTime
     */
    @Test
    public void test_queryScottempByCreatTime() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ScottEmpMapper scottEmpMapper = sqlSession.getMapper(ScottEmpMapper.class);
        List<ScottEmp> scottEmps = scottEmpMapper.queryScottempByCreatTime(
                LocalDateTime.of(1987, 4, 19, 0, 0, 0, 0),
                LocalDateTime.now()
        );
        logger.info("test_queryScottempByCreatTime Result: {}", scottEmps);
        sqlSession.close();
    }
}

