package com.github.soyanga.common.impl;

import com.github.soyanga.common.Shape;

/**
 * @program: springcore-case
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-10 00:01
 * @Version 1.0
 */
public class Triangle implements Shape {
    private final double a;
    private final double b;
    private final double c;

    public Triangle(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public double computerArea() {
        double p = this.computerSide() / 2;

        double s = Math.sqrt(
                p * (p - this.getA()) * (p - this.getB()) * (p - this.getC())
        );
        return s;
    }

    public double computerSide() {
        return this.getA() + this.getB() + this.getC();
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", area=" + this.computerArea() +
                ", side=" + this.computerSide() +
                '}';
    }
}
