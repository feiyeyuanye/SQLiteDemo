package com.example.sqliteapplication.bean;

/**
 * Created by xwxwaa on 2019/6/11.
 */

public class OrderBean {

    private int id;
    private String customName;
    private int orderPrice;

    public OrderBean() {

    }

    public OrderBean(int id, String customName, int orderPrice) {
        this.id = id;
        this.customName = customName;
        this.orderPrice = orderPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomName() {
        return customName == null ? "" : customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    @Override
    public String toString() {
        return "OrderBean{" +
                "id=" + id +
                ", customName='" + customName + '\'' +
                ", orderPrice=" + orderPrice +
                '}';
    }
}
