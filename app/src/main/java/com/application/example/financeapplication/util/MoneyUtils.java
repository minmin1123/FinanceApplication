package com.application.example.financeapplication.util;

import com.application.example.financeapplication.model.Account;

import java.util.List;

/**
 * author:minmin
 * email:775846180@qq.com
 * time:2018/05/18
 * desc:
 * version:1.0
 */

public class MoneyUtils {

    //计算某账单记录的总金额
    public static int totalMoney(List<Account> accountList) {
        int total=0;
        if(accountList!=null){
            for (Account account:accountList) {
                total+=Integer.parseInt(account.getMoney());
            }
        }
        return total;
    }
}
