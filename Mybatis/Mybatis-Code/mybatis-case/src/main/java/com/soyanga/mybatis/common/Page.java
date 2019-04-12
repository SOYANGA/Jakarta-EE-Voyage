package com.soyanga.mybatis.common;

/**
 * @program: mybatis-case
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-04 10:26
 * @Version 1.0
 */
public class Page {
    /**
     * 分页的第几页
     */
    private Integer pageNumber = 1;
    /**
     * 每一页的个数
     */
    private Integer pageSize = 2;
    /**
     * 每一页的偏移量
     */
    private Integer pageOffset = (this.getPageNumber() - 1) * this.getPageSize();

    /**
     * 页面排序升序/降序
     */
    private String orderColumnName;

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    //每次取得时候重新计算一下

    public Integer getPageOffset() {
        this.pageOffset = (this.getPageNumber() - 1) * this.getPageSize();
        return pageOffset;
    }

    public void setPageOffset(Integer pageOffset) {
        this.pageOffset = pageOffset;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderColumnName() {
        return orderColumnName;
    }

    public void setOrderColumnName(String orderColumnName) {
        this.orderColumnName = orderColumnName;
    }

    @Override
    public String toString() {
        return "Page{" +
                "pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                ", pageOffset=" + pageOffset +
                ", orderColumnName='" + orderColumnName + '\'' +
                '}';
    }
}
