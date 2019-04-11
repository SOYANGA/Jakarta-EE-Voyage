package com.github.soyanga.common.impl;

import com.github.soyanga.common.Shape;

/**
 * @program: springcore-case
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-09 23:57
 * @Version 1.0
 */
public class Circular implements Shape {
    private final double radius;

    public Circular(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }


    public double computerArea() {
        return Math.PI * Math.pow(this.radius, 2);
    }

    public double computerSide() {
        return 2 * Math.PI * this.getRadius();
    }

    @Override
    public String toString() {
        return "Circular{" +
                "radius=" + radius +
                ", area=" + this.computerArea() +
                ", side=" + this.computerSide() +
                '}';
    }
}
