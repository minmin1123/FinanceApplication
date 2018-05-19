package com.application.example.financeapplication;

import android.app.Application;

import com.application.example.financeapplication.database.AccountDatabase;

/**
 * author:minmin
 * email:775846180@qq.com
 * time:2018/03/03
 * desc:
 * version:1.0
 */

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    public void init(){
        AccountDatabase.getInstance(this);
    }
}
