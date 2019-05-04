package com.github.soyanga.springBootBasic.component;

import lombok.Data;

/**
 * @program: springBoot-Basics
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-05-03 11:02
 * @Version 1.0
 */

public class Car {
    /**
     * 颜色
     */
    private String color = "RED";

    /**
     * 材质
     */
    private String material = "真皮";

    /**
     * 品牌
     */
    private String brand = "大众";

    private Car() {

    }


    /**
     * 构建者模式
     */
    public static class CarBuilder {

        private Car car = new Car();

        public CarBuilder color(String color) {
            this.car.setColor(color);
            return this;
        }

        public CarBuilder material(String material) {
            this.car.setMaterial(material);
            return this;
        }

        public CarBuilder brand(String brand) {
            this.car.setBrand(brand);
            return this;
        }

        public Car build() {
            return this.car;
        }

    }


    public static CarBuilder newCar() {
        return new CarBuilder();
    }


    //getter setter 方法
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return "Car{" +
                "color='" + color + '\'' +
                ", material='" + material + '\'' +
                ", brand='" + brand + '\'' +
                '}';
    }

//    public static void main(String[] args) {
//        Car car1 = new CarBuilder().build();
//        System.out.println(car1);
//        System.out.println(new CarBuilder().color("WHITE").material("织物").build());
//    }
}
