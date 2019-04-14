package com.github.soyanga.auto;

import com.github.soyanga.Shape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @program: springcore-case-IoC
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-15 00:59
 * @Version 1.0
 */
@Service(value = "shapeService2")  //Bean id重命名 反之则为 对象默认名称
public class ShapeService {

    @Qualifier(value = "circle")  //多个实现子类，定义为实现类bean的名称
    @Autowired
    private Shape shape;

    @Autowired(required = false) //如果属性没有值也实例化 且不报错 类似于延迟注入
    private Person person;

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    @Override
    public String toString() {
        return "ShapeService{" +
                "shape=" + shape +
                ", person=" + person +
                '}';
    }
}
