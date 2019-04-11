package com.github.soyanga.common.impl;

import com.github.soyanga.common.Shape;

/**
 * @program: springcore-case
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-10 00:01
 * @Version 1.0
 */
public class Rectangle implements Shape {
    private final double width;
    private final double height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double computerArea() {
        return this.getHeight() * this.getWidth();
    }

    public double computerSide() {
        return 2 * (this.getHeight() * this.getWidth());
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "width=" + width +
                ", height=" + height +
                ", area=" + this.computerArea() +
                ", side=" + this.computerSide() +
                '}';
    }
}
