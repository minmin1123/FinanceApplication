package com.application.example.financeapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.application.example.financeapplication.R;
import com.application.example.financeapplication.database.AccountDatabase;
import com.application.example.financeapplication.model.Account;
import com.application.example.financeapplication.util.DateUtils;
import com.application.example.financeapplication.util.MoneyUtils;
import com.application.example.financeapplication.util.MyCalendar;

import java.util.ArrayList;
import java.util.List;

/**
 * author:minmin
 * email:775846180@qq.com
 * time:2018/05/16
 * desc:
 * version:1.0
 */

public class CountActivity extends Activity implements View.OnClickListener {


    private LinearLayout mBeginDateLl;
    private LinearLayout mEndDateLl;
    private TextView mBeginDateTv;
    private TextView mEndDateTv;
    private Button mQuaryBt;
    private ImageView mBackIv;
    private TextView mIncomeTv;
    private TextView mExpendTv;
    private TextView mProfitTv;
    private FrameLayout mItemFl1;
    private FrameLayout mItemFl2;
    private FrameLayout mItemFl3;

    private String mSelectedBeginYear = MyCalendar.getNow_year();
    private String mSelectedBeginMonth = MyCalendar.getNow_month();
    private String mSelectedBeginDay = MyCalendar.getNow_day();
    private String searchBeginYear = mSelectedBeginYear;
    private String searchBeginMonth = mSelectedBeginMonth;
    private String searchBeginDay = mSelectedBeginDay;

    private String mSelectedEndYear = MyCalendar.getNow_year();
    private String mSelectedEndMonth = MyCalendar.getNow_month();
    private String mSelectedEndDay = MyCalendar.getNow_day();
    private String searchEndYear = mSelectedEndYear;
    private String searchEndMonth = mSelectedEndMonth;
    private String searchEndDay = mSelectedEndDay;

    private AlertDialog.Builder mSearchSelectDayMemoBuilder;
    private AccountDatabase mAccountDatabase = AccountDatabase.getInstance();

    private List<Account> mIncomeList = new ArrayList<>();
    private List<Account> mExpendList = new ArrayList<>();

    private int totalIncome = 0;
    private int totalExpend = 0;
    private int totalProfit = 0;

    private final static int TYPE_INCOME = 0;
    private final static int TYPE_EXPEND = 1;

    private final static String TYPE = "type";
    private final static String BEGIN_DATE = "begin";
    private final static String END_DATE = "end";
    private final static String BEGIN_DATE_TEXT = "beginText";
    private final static String END_DATE_TEXT = "endText";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);

        initList();
        initView();
    }

    //数据初始化
    public void initList() {
        mIncomeList = mAccountDatabase.quaryEveryDayTotalSolutionList(mSelectedBeginYear, mSelectedBeginMonth, mSelectedBeginDay, TYPE_INCOME);
        mExpendList = mAccountDatabase.quaryEveryDayTotalSolutionList(mSelectedBeginYear, mSelectedBeginMonth, mSelectedBeginDay, TYPE_EXPEND);
        totalIncome = MoneyUtils.totalMoney(mIncomeList);
        totalExpend = MoneyUtils.totalMoney(mExpendList);
        totalProfit = totalIncome + totalExpend;
    }

    //页面初始化
    public void initView() {

        mBeginDateLl = findViewById(R.id.beginDateLl);
        mEndDateLl = findViewById(R.id.endDateLl);
        mBeginDateTv = findViewById(R.id.beginDateTv);
        mEndDateTv = findViewById(R.id.endDateTv);
        mQuaryBt = findViewById(R.id.quaryBt);
        mBackIv = findViewById(R.id.backIv);
        mIncomeTv = findViewById(R.id.incomeTv);
        mExpendTv = findViewById(R.id.expendTv);
        mProfitTv = findViewById(R.id.profitTv);
        mItemFl1 = findViewById(R.id.itemFl1);
        mItemFl2 = findViewById(R.id.itemFl2);
        mItemFl3 = findViewById(R.id.itemFl3);

        mBeginDateTv.setText(mSelectedBeginYear + "-" + mSelectedBeginMonth + "-" + mSelectedBeginDay);
        mEndDateTv.setText(mSelectedEndYear + "-" + mSelectedEndMonth + "-" + mSelectedEndDay);
        mIncomeTv.setText(totalIncome + "");
        mExpendTv.setText(totalExpend + "");
        mProfitTv.setText(totalProfit + "");

        mBackIv.setOnClickListener(this);
        mQuaryBt.setOnClickListener(this);
        mItemFl1.setOnClickListener(this);
        mItemFl2.setOnClickListener(this);
        mItemFl3.setOnClickListener(this);
        mBeginDateLl.setOnClickListener(this);
        mEndDateLl.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.beginDateLl:
                showDatePickerDialog(BEGIN_DATE);
                break;
            case R.id.endDateLl:
                showDatePickerDialog(END_DATE);
                break;
            case R.id.itemFl1:
                Intent toMItemSolutionIntent1 = new Intent(CountActivity.this, MonthItemSolutionActivity.class);
                toMItemSolutionIntent1.putExtra(TYPE, TYPE_INCOME);
                toMItemSolutionIntent1.putExtra(BEGIN_DATE, mSelectedBeginYear + mSelectedBeginMonth + mSelectedBeginDay);
                toMItemSolutionIntent1.putExtra(END_DATE, mSelectedEndYear + mSelectedEndMonth + mSelectedEndDay);
                toMItemSolutionIntent1.putExtra(BEGIN_DATE_TEXT, mBeginDateTv.getText().toString());
                toMItemSolutionIntent1.putExtra(END_DATE_TEXT, mEndDateTv.getText().toString());
                startActivity(toMItemSolutionIntent1);
                break;
            case R.id.itemFl2:
                Intent toMItemSolutionIntent2 = new Intent(CountActivity.this, MonthItemSolutionActivity.class);
                toMItemSolutionIntent2.putExtra(TYPE, TYPE_EXPEND);
                toMItemSolutionIntent2.putExtra(BEGIN_DATE, mSelectedBeginYear + mSelectedBeginMonth + mSelectedBeginDay);
                toMItemSolutionIntent2.putExtra(END_DATE, mSelectedEndYear + mSelectedEndMonth + mSelectedEndDay);
                toMItemSolutionIntent2.putExtra(BEGIN_DATE_TEXT, mBeginDateTv.getText().toString());
                toMItemSolutionIntent2.putExtra(END_DATE_TEXT, mEndDateTv.getText().toString());
                startActivity(toMItemSolutionIntent2);
                break;
            case R.id.quaryBt:
                if (Integer.parseInt(mSelectedBeginYear + mSelectedBeginMonth + mSelectedBeginDay) > Integer.parseInt(mSelectedEndYear + mSelectedEndMonth + mSelectedEndDay)) {
                    Toast.makeText(this, R.string.error_date, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.quary_success, Toast.LENGTH_SHORT).show();
                    updateView();
                }
                break;
            case R.id.backIv:
                finish();
                break;
            default:
                break;
        }
    }

    //更新当前显示的视图
    public void updateView() {
        mIncomeList = mAccountDatabase.quaryTotalSolutionList(mSelectedBeginYear + mSelectedBeginMonth + mSelectedBeginDay, mSelectedEndYear + mSelectedEndMonth + mSelectedEndDay, TYPE_INCOME);
        mExpendList = mAccountDatabase.quaryTotalSolutionList(mSelectedBeginYear + mSelectedBeginMonth + mSelectedBeginDay, mSelectedEndYear + mSelectedEndMonth + mSelectedEndDay, TYPE_EXPEND);
        totalIncome = MoneyUtils.totalMoney(mIncomeList);
        totalExpend = MoneyUtils.totalMoney(mExpendList);
        totalProfit = totalIncome + totalExpend;
        mIncomeTv.setText(totalIncome + "");
        mExpendTv.setText(totalExpend + "");
        mProfitTv.setText(totalProfit + "");
    }

    //弹出日历选择器
    public void showDatePickerDialog(final String type) {
        mSearchSelectDayMemoBuilder = new AlertDialog.Builder(this);
        mSearchSelectDayMemoBuilder.setTitle(type.equals(BEGIN_DATE) ? R.string.search_bigin_date : R.string.search_end_date);
        mSearchSelectDayMemoBuilder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (type.equals(BEGIN_DATE)) {
                    mSelectedBeginMonth = searchBeginMonth;
                    mSelectedBeginYear = searchBeginYear;
                    mSelectedBeginDay = searchBeginDay;
                    mBeginDateTv.setText(mSelectedBeginYear + "-" + mSelectedBeginMonth + "-" + mSelectedBeginDay);
                } else {
                    mSelectedEndMonth = searchEndMonth;
                    mSelectedEndYear = searchEndYear;
                    mSelectedEndDay = searchEndDay;
                    mEndDateTv.setText(mSelectedEndYear + "-" + mSelectedEndMonth + "-" + mSelectedEndDay);
                }
            }
        });
        mSearchSelectDayMemoBuilder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        View view = LayoutInflater.from(this).inflate(R.layout.edit_dialog, null);
        DatePicker dialogDp = view.findViewById(R.id.dialogDp);
        dialogDp.init(Integer.parseInt(type.equals(BEGIN_DATE) ? searchBeginYear : searchEndYear), Integer.parseInt(type.equals(BEGIN_DATE) ? searchBeginMonth : searchEndMonth) - 1, Integer.parseInt(type.equals(BEGIN_DATE) ? searchBeginDay : searchEndDay), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                if (type.equals(BEGIN_DATE)) {
                    searchBeginYear = String.valueOf(year);
                    searchBeginMonth = DateUtils.toNormalTime(month + 1);
                    searchBeginDay = DateUtils.toNormalTime(day);
                } else {
                    searchEndYear = String.valueOf(year);
                    searchEndMonth = DateUtils.toNormalTime(month + 1);
                    searchEndDay = DateUtils.toNormalTime(day);
                }
            }
        });
        mSearchSelectDayMemoBuilder.setView(view);
        mSearchSelectDayMemoBuilder.create().show();
    }
}
