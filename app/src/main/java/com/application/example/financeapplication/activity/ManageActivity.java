package com.application.example.financeapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.application.example.financeapplication.R;
import com.application.example.financeapplication.adapter.ItemListAdapter;
import com.application.example.financeapplication.database.AccountDatabase;
import com.application.example.financeapplication.model.Item;
import com.application.example.financeapplication.swipemenulistview.SwipeMenu;
import com.application.example.financeapplication.swipemenulistview.SwipeMenuCreator;
import com.application.example.financeapplication.swipemenulistview.SwipeMenuItem;
import com.application.example.financeapplication.swipemenulistview.SwipeMenuListView;
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

public class ManageActivity extends Activity implements View.OnClickListener{
    
    private SwipeMenuListView mListLv;
    private ImageView mBackIv;
    private Button mAddBt;
    private EditText mNameEt;
    private RadioGroup mTypeRg;
    
    private AccountDatabase mAccountDatabase = AccountDatabase.getInstance();
    private ItemListAdapter mItemListAdapter;

    private List<Item> mItemList = new ArrayList<>();


    private final static int TYPE_INCOME = 0;
    private final static int TYPE_EXPEND = 1;
    private static int TYPE_INTENT = TYPE_INCOME;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        
        initList();
        initView();
    }

    public void initList() {
        mItemList = mAccountDatabase.quaryAllItemList();
    }

    public void initView() {
        mListLv = findViewById(R.id.listLv);
        mBackIv = findViewById(R.id.backIv);
        mAddBt = findViewById(R.id.addBt);
        mNameEt = findViewById(R.id.nameEt);
        mTypeRg = findViewById(R.id.typeRg);

        mItemListAdapter = new ItemListAdapter(this, R.layout.item_item, mItemList);
        mListLv.setAdapter(mItemListAdapter);
        
        mBackIv.setOnClickListener(this);
        mAddBt.setOnClickListener(this);

        //创建侧滑删除菜单
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem item = new SwipeMenuItem(getApplicationContext());
                item.setBackground(R.color.red);
                item.setWidth(Utils.dp2px(ManageActivity.this, 60));
                item.setIcon(R.drawable.delete);
                menu.addMenuItem(item);

            }
        };
        mListLv.setMenuCreator(creator);
        mListLv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                final Item deleteItem = mItemList.get(position);
                switch (index) {
                    case 0:
                        //点击了删除键
                        mAccountDatabase.deleteItem(deleteItem);
                        updateView();
                        Snackbar.make(mListLv, R.string.delete_success, Snackbar.LENGTH_SHORT)
                                .setAction(R.string.undo, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mAccountDatabase.insertItem(deleteItem);
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

        mTypeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch(i){
                    case R.id.incomeRb:
                        TYPE_INTENT=TYPE_INCOME;
                        break;
                    case R.id.expendRb:
                        TYPE_INTENT=TYPE_EXPEND;
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backIv:
                finish();
                break;
            case R.id.addBt:
                if(checkItemOk()){
                    saveItem();
                }
                break;
            default:
                break;
        }
    }
    
    //更新当前显示的视图
    public void updateView() {
        mItemList.clear();
        mItemList.addAll(mAccountDatabase.quaryAllItemList());
        mItemListAdapter.notifyDataSetChanged();
    }

    //用户确认保存，插入数据库操作
    public void saveItem() {
        Item item = new Item();
        item.setType(TYPE_INTENT);
        item.setName(mNameEt.getText().toString().trim());
        mAccountDatabase.insertItem(item);
        Toast.makeText(this, R.string.save_success, Toast.LENGTH_SHORT).show();
        updateView();
        mNameEt.setText("");
    }

    public boolean checkItemOk(){
        String money=mNameEt.getText().toString();
        if(TextUtils.isEmpty(money)){
            Toast.makeText(this, R.string.none_name, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
