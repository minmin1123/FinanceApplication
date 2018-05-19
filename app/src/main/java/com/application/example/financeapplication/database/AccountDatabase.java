package com.application.example.financeapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.application.example.financeapplication.model.Account;
import com.application.example.financeapplication.model.Item;

import java.util.ArrayList;
import java.util.List;

/**
 *   author:minmin
 *   email:775846180@qq.com
 *   time:2017/10/11
 *   desc:数据库
 *   version:1.0
 */

public class AccountDatabase {

    private AccountDatabaseHelper helper;

    private static AccountDatabase accountDatabase;

    private SQLiteDatabase sqLiteDatabase;

    private final static String DATABASE_NAME="IAcccount.db";

    private final static int VERSION = 1;

    private final static String ID = "id";
    private final static String YEAR = "year";
    private final static String MONTH = "month";
    private final static String DAY = "day";
    private final static String NAME = "name";
    private final static String MONEY = "money";
    private final static String TYPE = "type";
    private final static String TABLE_ACCOUNT = "Account";
    private final static String TABLE_ITEM = "Item";

    private AccountDatabase(Context context){
        helper = new AccountDatabaseHelper(context, DATABASE_NAME, null, VERSION);
    }

    public static AccountDatabase getInstance(Context context){
        if(accountDatabase==null){
            accountDatabase=new AccountDatabase(context);
        }
        return accountDatabase;
    }

    public static AccountDatabase getInstance(){

        return accountDatabase;
    }

    //新建一条记账记录
    public void insertAccount(Account account){
        sqLiteDatabase=helper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, account.getId());
        values.put(YEAR,account.getYear());
        values.put(MONTH,account.getMonth());
        values.put(DAY,account.getDay());
        values.put(NAME,account.getName());
        values.put(TYPE,account.getType());
        values.put(MONEY, account.getMoney());
        sqLiteDatabase.insert(TABLE_ACCOUNT, null, values);
    }

    //删除一条记账记录
    public void deleteAccount(Account account){
        sqLiteDatabase=helper.getReadableDatabase();
        String id = account.getId();
        sqLiteDatabase.delete(TABLE_ACCOUNT,ID+"=?",new String[]{id});
    }

    //查询指定日期的记账记录
    public List<Account> quaryEveryDayAccountList(String year, String month, String day){
        List<Account> accountList=new ArrayList<>();
        sqLiteDatabase=helper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.query(TABLE_ACCOUNT, null, YEAR+"=? and "+MONTH+"=? and "+DAY+"=?", new String[]{year,month,day}, null, null,ID+" asc");
        if(cursor!=null){
            while(cursor.moveToNext()){
                Account account = new Account();
                account.setId(cursor.getString(cursor.getColumnIndex(ID)));
                account.setYear(year);
                account.setMonth(month);
                account.setDay(day);
                account.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                account.setType(cursor.getInt(cursor.getColumnIndex(TYPE)));
                account.setMoney(cursor.getString(cursor.getColumnIndex(MONEY)));
                accountList.add(account);
            }
            cursor.close();
        }
        return accountList;
    }

    //查询指定日期的总收入/支出记录
    public List<Account> quaryEveryDayTotalSolutionList(String year, String month, String day,int type){
        List<Account> accountList=new ArrayList<>();
        sqLiteDatabase=helper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.query(TABLE_ACCOUNT, null, YEAR+"=? and "+MONTH+"=? and "+DAY+"=? and "+TYPE+"="+type, new String[]{year,month,day}, null, null,ID+" asc");
        if(cursor!=null){
            while(cursor.moveToNext()){
                Account account = new Account();
                account.setId(cursor.getString(cursor.getColumnIndex(ID)));
                account.setYear(year);
                account.setMonth(month);
                account.setDay(day);
                account.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                account.setType(type);
                account.setMoney(cursor.getString(cursor.getColumnIndex(MONEY)));
                accountList.add(account);
            }
            cursor.close();
        }
        return accountList;
    }

    //查询指定时期的账单记录
    public List<Account> quaryTotalSolutionList(String beginDate,String endDate,int type){
        List<Account> accountList=new ArrayList<>();
        sqLiteDatabase=helper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.query(TABLE_ACCOUNT, null, TYPE+"="+type, null, null, null,ID+" desc");
        boolean canStop=false;
        if(cursor!=null){
            while(cursor.moveToNext()){
                if(canStop){
                    break;
                }
                String nowYear=cursor.getString(cursor.getColumnIndex(YEAR));
                String nowMonth=cursor.getString(cursor.getColumnIndex(MONTH));
                String nowDay=cursor.getString(cursor.getColumnIndex(DAY));
                String nowDate=nowYear+nowMonth+nowDay;
                if(Integer.parseInt(nowDate)>=Integer.parseInt(beginDate)&&Integer.parseInt(nowDate)<=Integer.parseInt(endDate)){
                    Account account = new Account();
                    account.setId(cursor.getString(cursor.getColumnIndex(ID)));
                    account.setYear(nowYear);
                    account.setMonth(nowMonth);
                    account.setDay(nowDay);
                    account.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                    account.setType(cursor.getInt(cursor.getColumnIndex(TYPE)));
                    account.setMoney(cursor.getString(cursor.getColumnIndex(MONEY)));
                    accountList.add(account);
                }
                if(Integer.parseInt(nowDate)<Integer.parseInt(beginDate)){
                    canStop=true;
                }
            }
            cursor.close();
        }
        return accountList;
    }

    //遍历当天全部备忘录（按顺序），对is_first进行重新赋值
//    public void getEveryDayMemo(String year,String month,String day){
//        sqLiteDatabase=helper.getReadableDatabase();
//        Cursor cursor=sqLiteDatabase.query(TABLE_ACCOUNT, null, YEAR+"=? and "+MONTH+"=? and "+DAY+"=?", new String[]{year,month,day}, null, null,ID+" asc");
//        int hasFirst=0;
//        if(cursor!=null){
//            while(cursor.moveToNext()){
//                String id=cursor.getString(cursor.getColumnIndex(ID));
//                Log.i("Main","hasFirst="+hasFirst+"");
//                if(hasFirst==1){
//                    toChangeEveryDayFirstMemo(id,0);
//                }else{
//                    toChangeEveryDayFirstMemo(id,1);
//                }
//                hasFirst=1;
//            }
//            cursor.close();
//        }
//    }
//
//    public void toChangeEveryDayFirstMemo(String id,int isFirst){
//        sqLiteDatabase=helper.getReadableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(IS_REMIND, isFirst);
//        sqLiteDatabase.update(TABLE_ACCOUNT,values,ID+"=?",new String[]{id});
//    }

    //新建指定日期的备忘录列表--先将被复制备忘录改为指定日期备忘录，再插入一条被复制备忘录
//    public void insertSelectMemoList(List<Account> accountList,String year,String month,String day){
//        sqLiteDatabase=helper.getReadableDatabase();
//        for(Account account:accountList){
//            ContentValues values = new ContentValues();
//            values.put(ID,year + month + day +  account.getStart_hour() + account.getStart_minute() + account.getFinish_hour() + account.getFinish_minute() + new MyCalendar().getNow_hour()+account.getId().substring(18,22));
//            values.put(YEAR,year);
//            values.put(MONTH,month);
//            values.put(DAY,day);
//            values.put(WEEK,DateUtils.getSelectedWeek(Integer.parseInt(year),Integer.parseInt(month)-1,Integer.parseInt(day)));
//            values.put(IS_COMPLETE,0);
//            values.put(IS_REMIND,0);
//            values.put(IS_CHOSEN,0);
//            sqLiteDatabase.update(TABLE_ACCOUNT,values,ID+"=?",new String[]{account.getId()});
//            insertMemo(account);
//        }
        //对指定日期的备忘录Is_first进行重新赋值
//        getEveryDayMemo(year,month,day);
//    }

    //批量删除指定日期的备忘录
//    public void deleteSelectMemoList(List<Account> accountList){
//        for(Account account:accountList){
//            deleteMemo(account);
//            getEveryDayMemo(account.getYear(),account.getMonth(),account.getDay());
//        }
//
//    }

    //更改备忘录的完成状态--对应is_completed属性
//    public void updateMemoCompleteStatus(Account account,int isCompleted){
//        sqLiteDatabase=helper.getReadableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(IS_COMPLETE, isCompleted);
//        sqLiteDatabase.update(TABLE_ACCOUNT,values,ID+"=?",new String[]{account.getId()});
//    }

    //新建一条项目
    public void insertItem(Item item){
        sqLiteDatabase=helper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME,item.getName());
        values.put(TYPE,item.getType());
        sqLiteDatabase.insert(TABLE_ITEM, null, values);
    }

    //删除指定项目
    public void deleteItem(Item item){
        sqLiteDatabase=helper.getReadableDatabase();
        String name = item.getName();
        sqLiteDatabase.delete(TABLE_ITEM,NAME+"=?",new String[]{name});
    }

    //查询所有项目
    public List<Item> quaryAllItemList(){
        List<Item> itemList=new ArrayList<>();
        sqLiteDatabase=helper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.query(TABLE_ITEM, null, null,null,null, null,TYPE+" desc");
        if(cursor!=null){
            while(cursor.moveToNext()){
                Item item = new Item();
                item.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                item.setType(cursor.getInt(cursor.getColumnIndex(TYPE)));
                itemList.add(item);
            }
            cursor.close();
        }
        return itemList;
    }

}
