package com.application.example.financeapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.application.example.financeapplication.R;
import com.application.example.financeapplication.adapter.AccountListAdapter;
import com.application.example.financeapplication.database.AccountDatabase;
import com.application.example.financeapplication.model.Account;
import com.application.example.financeapplication.model.Item;
import com.application.example.financeapplication.swipemenulistview.SwipeMenu;
import com.application.example.financeapplication.swipemenulistview.SwipeMenuCreator;
import com.application.example.financeapplication.swipemenulistview.SwipeMenuItem;
import com.application.example.financeapplication.swipemenulistview.SwipeMenuListView;
import com.application.example.financeapplication.util.DateUtils;
import com.application.example.financeapplication.util.MyCalendar;
import com.application.example.financeapplication.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * author:minmin
 * email:775846180@qq.com
 * time:2018/05/16
 * desc:
 * version:1.0
 */

public class CheckActivity extends Activity implements View.OnClickListener {

    private TextView mDateTv;
    private SwipeMenuListView mListLv;
    private ImageView mBackIv;
    private Button mAddBt;
    private EditText mMoneyEt;
    private Spinner mNameSp;

    private String mSelectedYear = MyCalendar.getNow_year();
    private String mSelectedMonth = MyCalendar.getNow_month();
    private String mSelectedDay = MyCalendar.getNow_day();
    private String searchYear = mSelectedYear;
    private String searchMonth = mSelectedMonth;
    private String searchDay = mSelectedDay;

    private AlertDialog.Builder mSearchSelectDayMemoBuilder;
    private AccountDatabase mAccountDatabase = AccountDatabase.getInstance();
    private AccountListAdapter mAccountListAdapter;
    private ArrayAdapter mNameListAdapter;

    private List<Account> mAccountList = new ArrayList<>();
    private List<Item> mItemList = new ArrayList<>();
    private List<String> mNameList = new ArrayList<>();

    private int seletedItemPosition=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        initList();
        initView();
    }

    public void initList() {
        mAccountList = mAccountDatabase.quaryEveryDayAccountList(mSelectedYear, mSelectedMonth, mSelectedDay);
        mItemList = mAccountDatabase.quaryAllItemList();
        for(Item item:mItemList){
            String type=item.getType()==0?"收入":"支出";
            mNameList.add(item.getName()+"【"+type+"】");
        }
    }

    public void initView() {
        mDateTv = findViewById(R.id.dateTv);
        mListLv = findViewById(R.id.listLv);
        mBackIv = findViewById(R.id.backIv);
        mAddBt = findViewById(R.id.addBt);
        mMoneyEt = findViewById(R.id.moneyEt);
        mNameSp = findViewById(R.id.nameSp);

        mDateTv.setText("今天-" + mSelectedYear + "年" + mSelectedMonth + "月" + mSelectedDay + "日");
        mAccountListAdapter = new AccountListAdapter(this, R.layout.item_account, mAccountList);
        mListLv.setAdapter(mAccountListAdapter);
        mNameListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,mNameList );
        mNameListAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mNameSp.setAdapter(mNameListAdapter);

        mDateTv.setOnClickListener(this);
        mBackIv.setOnClickListener(this);
        mAddBt.setOnClickListener(this);
        mNameSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                seletedItemPosition=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //创建侧滑删除菜单
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem item = new SwipeMenuItem(getApplicationContext());
                item.setBackground(R.color.red);
                item.setWidth(Utils.dp2px(CheckActivity.this, 60));
                item.setIcon(R.drawable.delete);
                menu.addMenuItem(item);

            }
        };
        mListLv.setMenuCreator(creator);
        mListLv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                final Account deleteAccount = mAccountList.get(position);
                switch (index) {
                    case 0:
                        //点击了删除键
                        mAccountDatabase.deleteAccount(deleteAccount);
                        updateView();
                        Snackbar.make(mListLv, R.string.delete_success, Snackbar.LENGTH_SHORT)
                                .setAction(R.string.undo, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mAccountDatabase.insertAccount(deleteAccount);
                                        updateView();
                                    }
                                }).show();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dateTv:
                showDatePickerDialog();
                break;
            case R.id.backIv:
                finish();
                break;
            case R.id.addBt:
                if(checkAccountOk()){
                    saveAccount();
                }
                break;
            default:
                break;
        }
    }

    //弹出日历选择器
    public void showDatePickerDialog() {
        mSearchSelectDayMemoBuilder = new AlertDialog.Builder(this);
        mSearchSelectDayMemoBuilder.setTitle(R.string.quary_date);
        mSearchSelectDayMemoBuilder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mSelectedMonth = searchMonth;
                mSelectedYear = searchYear;
                mSelectedDay = searchDay;
                if (MyCalendar.getNow_year().equals(mSelectedYear) && MyCalendar.getNow_month().equals(mSelectedMonth) && MyCalendar.getNow_day().equals(mSelectedDay)) {
                    mDateTv.setText("今天-" + mSelectedYear + "年" + mSelectedMonth + "月" + mSelectedDay + "日");
                } else if (DateUtils.isTomorrow(MyCalendar.getNow_year(), MyCalendar.getNow_month(), MyCalendar.getNow_day(), mSelectedYear, mSelectedMonth, mSelectedDay)) {
                    mDateTv.setText("明天-" + mSelectedYear + "年" + mSelectedMonth + "月" + mSelectedDay + "日");
                } else {
                    mDateTv.setText(mSelectedYear + "年" + mSelectedMonth + "月" + mSelectedDay + "日");
                }
                Toast.makeText(CheckActivity.this, R.string.quary_success, Toast.LENGTH_SHORT).show();
                updateView();
            }
        });
        mSearchSelectDayMemoBuilder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        View view = LayoutInflater.from(this).inflate(R.layout.edit_dialog, null);
        DatePicker dialogDp = view.findViewById(R.id.dialogDp);
        dialogDp.init(Integer.parseInt(searchYear), Integer.parseInt(searchMonth) - 1, Integer.parseInt(searchDay), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                searchYear = String.valueOf(year);
                searchMonth = DateUtils.toNormalTime(month + 1);
                searchDay = DateUtils.toNormalTime(day);

            }
        });
        mSearchSelectDayMemoBuilder.setView(view);
        mSearchSelectDayMemoBuilder.create().show();
    }

    //更新当前显示的视图
    public void updateView() {
        mAccountList.clear();
        mAccountList.addAll(mAccountDatabase.quaryEveryDayAccountList(mSelectedYear, mSelectedMonth, mSelectedDay)) ;
        mAccountListAdapter.notifyDataSetChanged();
    }

    //用户确认保存，插入数据库操作
    public void saveAccount() {
        Account account = new Account();
        account.setId(mSelectedYear + mSelectedMonth + mSelectedDay + MyCalendar.getNow_hour() + MyCalendar.getNow_minute() + MyCalendar.getNow_second());
        account.setYear(mSelectedYear);
        account.setMonth(mSelectedMonth);
        account.setDay(mSelectedDay);
        Item item = mItemList.get(seletedItemPosition);
        account.setType(item.getType());
        account.setName(item.getName());
        account.setMoney(mMoneyEt.getText().toString().trim());
        mAccountDatabase.insertAccount(account);
        Toast.makeText(this, R.string.save_success, Toast.LENGTH_SHORT).show();
        updateView();
        mMoneyEt.setText("");
    }

    public boolean checkAccountOk(){
        String money=mMoneyEt.getText().toString();
        if(TextUtils.isEmpty(money)){
            Toast.makeText(this, R.string.none_money, Toast.LENGTH_SHORT).show();
            return false;
        }
        try{
            Integer.parseInt(money);
        }catch(NumberFormatException e){
            Toast.makeText(this, R.string.error_format, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        setResult(1, intent);
        super.finish();
    }
}
