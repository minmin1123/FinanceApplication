package com.application.example.financeapplication.model;

/**
 * author:minmin
 * email:775846180@qq.com
 * time:2018/05/17
 * desc:
 * version:1.0
 */

public class Account {

    private String id;//唯一id=年+月+日+当前时钟+当前分钟+当前秒数

    private String year;//年（4位）

    private String month;//月（2位）

    private String day;//日（2位）

    private String name;//项目名称

    private int type = TYPE_INCOME;//项目的类型

    private String money;//费用

    private final static int TYPE_INCOME = 0;//类型为收入
    private final static int TYPE_EXPEND = 1;//类型为支出

    public String getId() {
        return id;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public String getMoney() {
        return money;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
