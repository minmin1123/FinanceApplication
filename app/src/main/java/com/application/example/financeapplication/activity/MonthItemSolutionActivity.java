package com.application.example.financeapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.example.financeapplication.R;
import com.application.example.financeapplication.adapter.MonthAccountListAdapter;
import com.application.example.financeapplication.database.AccountDatabase;
import com.application.example.financeapplication.model.Account;
import com.application.example.financeapplication.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

/**
 * author:minmin
 * email:775846180@qq.com
 * time:2018/05/18
 * desc:
 * version:1.0
 */

public class MonthItemSolutionActivity extends Activity{

    private TextView mBeginDateTv;
    private TextView mEndDateTv;
    private SwipeMenuListView mListLv;
    private ImageView mBackIv;

    private final static String TYPE = "type";
    private final static String BEGIN_DATE = "begin";
    private final static String END_DATE = "end";
    private final static String BEGIN_DATE_TEXT = "beginText";
    private final static String END_DATE_TEXT = "endText";

    private AccountDatabase mAccountDatabase = AccountDatabase.getInstance();
    private MonthAccountListAdapter mMonthAccountListAdapter;
    private List<Account> mAccountList = new ArrayList<>();
    private Intent toItemSolutionIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_item_solution);
        toItemSolutionIntent=getIntent();
        initList();
        initView();

    }

    public void initList() {
        mAccountList = mAccountDatabase.quaryTotalSolutionList(toItemSolutionIntent.getStringExtra(BEGIN_DATE),toItemSolutionIntent.getStringExtra(END_DATE),toItemSolutionIntent.getIntExtra(TYPE,-1));
        toItemSolutionIntent = getIntent();
    }

    public void initView() {
        mListLv = findViewById(R.id.listLv);
        mBackIv = findViewById(R.id.backIv);
        mBeginDateTv = findViewById(R.id.beginDateTv);
        mEndDateTv = findViewById(R.id.endDateTv);

        mBeginDateTv.setText(toItemSolutionIntent.getStringExtra(BEGIN_DATE_TEXT));
        mEndDateTv.setText(toItemSolutionIntent.getStringExtra(END_DATE_TEXT));
        mMonthAccountListAdapter = new MonthAccountListAdapter(this, R.layout.item_month_account , mAccountList);
        mListLv.setAdapter(mMonthAccountListAdapter);

        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
