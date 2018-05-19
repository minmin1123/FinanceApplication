package com.application.example.financeapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

public class MainActivity extends Activity implements View.OnClickListener {

    private NavigationView mViewNv;
    private ImageView mAddAnyIv;
    private ImageView mManageIv;
    private TextView mIncomeTv;
    private TextView mExpendTv;
    private TextView mProfitTv;
    private FrameLayout mItemFl1;
    private FrameLayout mItemFl2;
    private FrameLayout mItemFl3;
    private ImageView mLeftIv;
    private ImageView mRightIv;
    private TextView mDateTv;

    private String mSelectedYear = MyCalendar.getNow_year();
    private String mSelectedMonth = MyCalendar.getNow_month();
    private String mSelectedDay = MyCalendar.getNow_day();
    private String searchYear = mSelectedYear;
    private String searchMonth = mSelectedMonth;
    private String searchDay = mSelectedDay;

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
    private final static String LEFT = "left";
    private final static String RIGHT = "right";
    private final static String YEAR = "year";
    private final static String MONTH = "month";
    private final static String DAY = "day";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initList();
        initView();
    }

    //数据初始化
    public void initList() {
        mIncomeList = mAccountDatabase.quaryEveryDayTotalSolutionList(mSelectedYear, mSelectedMonth, mSelectedDay, TYPE_INCOME);
        mExpendList = mAccountDatabase.quaryEveryDayTotalSolutionList(mSelectedYear, mSelectedMonth, mSelectedDay, TYPE_EXPEND);
        totalIncome = MoneyUtils.totalMoney(mIncomeList);
        totalExpend = MoneyUtils.totalMoney(mExpendList);
        totalProfit = totalIncome + totalExpend;
    }

    //页面初始化
    public void initView() {

        mAddAnyIv = findViewById(R.id.addAnyIv);
        mManageIv = findViewById(R.id.manageIv);
        mViewNv = findViewById(R.id.viewNv);
        mIncomeTv = findViewById(R.id.incomeTv);
        mExpendTv = findViewById(R.id.expendTv);
        mProfitTv = findViewById(R.id.profitTv);
        mItemFl1 = findViewById(R.id.itemFl1);
        mItemFl2 = findViewById(R.id.itemFl2);
        mItemFl3 = findViewById(R.id.itemFl3);
        mLeftIv = findViewById(R.id.leftIv);
        mRightIv = findViewById(R.id.rightIv);
        mDateTv = findViewById(R.id.dateTv);

        mIncomeTv.setText(totalIncome + "");
        mExpendTv.setText(totalExpend + "");
        mProfitTv.setText(totalProfit + "");
        mDateTv.setText(mSelectedYear + "-" + mSelectedMonth + "-" + mSelectedDay);

        mAddAnyIv.setOnClickListener(this);
        mManageIv.setOnClickListener(this);
        mItemFl1.setOnClickListener(this);
        mItemFl2.setOnClickListener(this);
        mItemFl3.setOnClickListener(this);
        mLeftIv.setOnClickListener(this);
        mRightIv.setOnClickListener(this);
        mDateTv.setOnClickListener(this);

        mViewNv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //NavigationView菜单项选中事件
                switch (item.getItemId()) {
                    case R.id.count:
                        Intent toCountIntent = new Intent(MainActivity.this, CountActivity.class);
                        startActivity(toCountIntent);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        //获取滑动菜单NavigationView的头布局
        View headView = mViewNv.getHeaderView(0);
        ImageView mHeadIv = headView.findViewById(R.id.headIv);
        mHeadIv.setBackgroundResource(R.drawable.head);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addAnyIv:
                Intent toCheckIntent = new Intent(MainActivity.this, CheckActivity.class);
                startActivityForResult(toCheckIntent,1);
                break;
            case R.id.manageIv:
                Intent toManageIntent = new Intent(MainActivity.this, ManageActivity.class);
                startActivity(toManageIntent);
                break;
            case R.id.itemFl1:
                Intent toItemSolutionIntent1 = new Intent(MainActivity.this, DayItemSolutionActivity.class);
                toItemSolutionIntent1.putExtra(TYPE, TYPE_INCOME);
                toItemSolutionIntent1.putExtra(YEAR, mSelectedYear);
                toItemSolutionIntent1.putExtra(MONTH, mSelectedMonth);
                toItemSolutionIntent1.putExtra(DAY, mSelectedDay);
                startActivityForResult(toItemSolutionIntent1,2);
                break;
            case R.id.itemFl2:
                Intent toItemSolutionIntent2 = new Intent(MainActivity.this, DayItemSolutionActivity.class);
                toItemSolutionIntent2.putExtra(TYPE, TYPE_EXPEND);
                toItemSolutionIntent2.putExtra(YEAR, mSelectedYear);
                toItemSolutionIntent2.putExtra(MONTH, mSelectedMonth);
                toItemSolutionIntent2.putExtra(DAY, mSelectedDay);
                startActivityForResult(toItemSolutionIntent2,3);
                break;
            case R.id.leftIv:
                checkOntherDayAccount(LEFT);
                break;
            case R.id.rightIv:
                checkOntherDayAccount(RIGHT);
                break;
            case R.id.dateTv:
                showDatePickerDialog();
                break;
            default:
                break;
        }
    }

    @Override//返回更新
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateView();
    }

    //更新当前显示的视图
    public void updateView() {

        mIncomeList = mAccountDatabase.quaryEveryDayTotalSolutionList(mSelectedYear, mSelectedMonth, mSelectedDay, TYPE_INCOME);
        mExpendList = mAccountDatabase.quaryEveryDayTotalSolutionList(mSelectedYear, mSelectedMonth, mSelectedDay, TYPE_EXPEND);
        totalIncome = MoneyUtils.totalMoney(mIncomeList);
        totalExpend = MoneyUtils.totalMoney(mExpendList);
        totalProfit = totalIncome + totalExpend;
        mIncomeTv.setText(totalIncome + "");
        mExpendTv.setText(totalExpend + "");
        mProfitTv.setText(totalProfit + "");
    }

    //查看不同日期的账单情况
    public void checkOntherDayAccount(String flag) {
        List<String> dateList = new ArrayList<>();
        if (flag.equals(LEFT)) {
            dateList = DateUtils.theDayBefore(mSelectedYear, mSelectedMonth, mSelectedDay);
        } else if (flag.equals(RIGHT)) {
            dateList = DateUtils.theDayAfter(mSelectedYear, mSelectedMonth, mSelectedDay);
        }
        mSelectedYear = dateList.get(0);
        mSelectedMonth = DateUtils.toNormalTime(Integer.parseInt(dateList.get(1)));
        mSelectedDay = DateUtils.toNormalTime(Integer.parseInt(dateList.get(2)));
        mDateTv.setText(mSelectedYear + "-" + mSelectedMonth + "-" + mSelectedDay);
        updateView();
    }

    //弹出日历选择器
    public void showDatePickerDialog() {
        mSearchSelectDayMemoBuilder = new AlertDialog.Builder(this);
        mSearchSelectDayMemoBuilder.setTitle(R.string.search_date);
        mSearchSelectDayMemoBuilder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mSelectedMonth = searchMonth;
                mSelectedYear = searchYear;
                mSelectedDay = searchDay;
                mDateTv.setText(mSelectedYear + "-" + mSelectedMonth + "-" + mSelectedDay);
                Toast.makeText(MainActivity.this, R.string.quary_success, Toast.LENGTH_SHORT).show();
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

}
