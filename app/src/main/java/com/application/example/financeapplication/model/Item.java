package com.application.example.financeapplication.model;

/**
 * author:minmin
 * email:775846180@qq.com
 * time:2018/05/18
 * desc:
 * version:1.0
 */

public class Item {

    private String name;//项目名称

    private int type = TYPE_INCOME;//项目的类型

    private final static int TYPE_INCOME = 0;//类型为收入
    private final static int TYPE_EXPEND = 1;//类型为支出

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
