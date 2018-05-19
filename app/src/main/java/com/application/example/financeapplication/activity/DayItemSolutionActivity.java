package com.application.example.financeapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.example.financeapplication.R;
import com.application.example.financeapplication.adapter.AccountListAdapter;
import com.application.example.financeapplication.database.AccountDatabase;
import com.application.example.financeapplication.model.Account;
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
 * time:2018/05/18
 * desc:
 * version:1.0
 */

public class DayItemSolutionActivity extends Activity implements View.OnClickListener {

    private TextView mDateTv;
    private SwipeMenuListView mListLv;
    private ImageView mBackIv;

    private String mSelectedYear = MyCalendar.getNow_year();
    private String mSelectedMonth = MyCalendar.getNow_month();
    private String mSelectedDay = MyCalendar.getNow_day();
    private String searchYear = mSelectedYear;
    private String searchMonth = mSelectedMonth;
    private String searchDay = mSelectedDay;

    private AlertDialog.Builder mSearchSelectDayMemoBuilder;
    private AccountDatabase mAccountDatabase = AccountDatabase.getInstance();
    private AccountListAdapter mAccountListAdapter;

    private List<Account> mAccountList = new ArrayList<>();

    private final static String TYPE = "type";
    private final static String YEAR = "year";
    private final static String MONTH = "month";
    private final static String DAY = "day";
    private static int TYPE_INTENT = -1;
    private final static int TYPE_INCOME = 0;
    private final static int TYPE_EXPEND = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_item_solution);
        Intent toItemSolutionIntent = getIntent();
        TYPE_INTENT = toItemSolutionIntent.getIntExtra(TYPE, -1) == TYPE_INCOME ? TYPE_INCOME : TYPE_EXPEND;
        mSelectedYear = toItemSolutionIntent.getStringExtra(YEAR);
        mSelectedMonth = toItemSolutionIntent.getStringExtra(MONTH);
        mSelectedDay = toItemSolutionIntent.getStringExtra(DAY);
        initList();
        initView();
    }

    public void initList() {
        mAccountList = mAccountDatabase.quaryEveryDayTotalSolutionList(mSelectedYear, mSelectedMonth, mSelectedDay, TYPE_INTENT);
    }

    public void initView() {
        mDateTv = findViewById(R.id.dateTv);
        mListLv = findViewById(R.id.listLv);
        mBackIv = findViewById(R.id.backIv);

        if (MyCalendar.getNow_year().equals(mSelectedYear) && MyCalendar.getNow_month().equals(mSelectedMonth) && MyCalendar.getNow_day().equals(mSelectedDay)) {
            mDateTv.setText("今天-" + mSelectedYear + "年" + mSelectedMonth + "月" + mSelectedDay + "日");
        } else if (DateUtils.isTomorrow(MyCalendar.getNow_year(), MyCalendar.getNow_month(), MyCalendar.getNow_day(), mSelectedYear, mSelectedMonth, mSelectedDay)) {
            mDateTv.setText("明天-" + mSelectedYear + "年" + mSelectedMonth + "月" + mSelectedDay + "日");
        } else {
            mDateTv.setText(mSelectedYear + "年" + mSelectedMonth + "月" + mSelectedDay + "日");
        }
        mAccountListAdapter = new AccountListAdapter(this, R.layout.item_account, mAccountList);
        mListLv.setAdapter(mAccountListAdapter);

        mDateTv.setOnClickListener(this);
        mBackIv.setOnClickListener(this);

        //创建侧滑删除菜单
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem item = new SwipeMenuItem(getApplicationContext());
                item.setBackground(R.color.red);
                item.setWidth(Utils.dp2px(DayItemSolutionActivity.this, 60));
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
        mAccountList.addAll(mAccountDatabase.quaryEveryDayTotalSolutionList(mSelectedYear, mSelectedMonth, mSelectedDay, TYPE_INTENT));
        mAccountListAdapter.notifyDataSetChanged();
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        setResult(2, intent);
        super.finish();
    }
}
