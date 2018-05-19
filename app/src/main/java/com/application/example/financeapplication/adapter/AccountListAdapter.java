package com.application.example.financeapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.example.financeapplication.R;
import com.application.example.financeapplication.model.Account;

import java.util.List;

/**
 *   author:minmin
 *   email:775846180@qq.com
 *   time:2017/01/15
 *   desc:纪念日的Adapter
 *   version:1.0
 */

public class AccountListAdapter extends BaseAdapter {

    private int resourceId;

    private ViewHolder holder;

    private Context mConext;

    private List<Account> mAccountList;

    private final static int TYPE_INCOME = 0;
    private final static int TYPE_EXPEND = 1;

    public AccountListAdapter(Context context, int resource, List<Account> objects) {
        super();
        mConext = context;
        resourceId = resource;
        mAccountList = objects;
    }

    @Override
    public int getCount() {
        return mAccountList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAccountList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (mAccountList.get(position).getType()==0) {
            return TYPE_INCOME;
        } else{
            return TYPE_EXPEND;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Account account = (Account) getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(mConext).inflate(resourceId, null);
            holder = new ViewHolder();
            holder.mNameTv = view.findViewById(R.id.nameTv);
            holder.mMoneyTv = view.findViewById(R.id.moneyTv);
            holder.bg = view.findViewById(R.id.bg);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        String type=account.getType()==0?"收入":"支出";
        holder.mNameTv.setText(account.getName()+"【"+type+"】");
        holder.mMoneyTv.setText(account.getMoney());
        
        switch (getItemViewType(position)) {
            case TYPE_EXPEND:
                holder.bg.setBackgroundResource(R.drawable.item_expend);
                break;
            default:
                break;
        }

        return view;
    }

    class ViewHolder {
        TextView mNameTv;
        TextView mMoneyTv;
        LinearLayout bg;
    }
}