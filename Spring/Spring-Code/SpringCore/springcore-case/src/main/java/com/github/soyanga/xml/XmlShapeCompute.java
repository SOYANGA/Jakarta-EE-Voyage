package com.github.soyanga.xml;

import com.github.soyanga.common.Shape;
import com.github.soyanga.common.impl.Circular;
import com.github.soyanga.common.impl.Rectangle;
import com.github.soyanga.common.impl.Triangle;

/**
 * @program: springcore-case
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-10 00:13
 * @Version 1.0
 */
public class XmlShapeCompute {

    private Shape circular;
    private Shape rectangle;
    private Shape triangle;

    public Shape computeShape(String shapeName) {
        if (shapeName == null || shapeName.length() == 0) {
            throw new IllegalArgumentException("Not found shape");
        }
        if (shapeName.equals("circular")) {
            return circular;
        }
        if (shapeName.equals("rectangle")) {
            return rectangle;
        }
        if (shapeName.equals("triangle")) {
            return triangle;
        }
        throw new IllegalArgumentException("Not found" + shapeName);
    }

    public Shape getCircular() {
        return circular;
    }

    public void setCircular(Shape circular) {
        this.circular = circular;
    }

    public Shape getRectangle() {
        return rectangle;
    }

    public void setRectangle(Shape rectangle) {
        this.rectangle = rectangle;
    }

    public Shape getTriangle() {
        return triangle;
    }

    public void setTriangle(Shape triangle) {
        this.triangle = triangle;
    }

//    public static void main(String[] args) {
//        XmlShapeCompute xmlShapeCompute = new XmlShapeCompute();
//        System.out.println(xmlShapeCompute.computeShape(args[0]));
//    }
}
