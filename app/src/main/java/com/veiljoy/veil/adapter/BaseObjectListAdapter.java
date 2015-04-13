package com.veiljoy.veil.adapter;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.veiljoy.veil.BaseApplication;
import com.veiljoy.veil.R;
import com.veiljoy.veil.bean.BaseInfo;

public class BaseObjectListAdapter extends BaseAdapter {

    protected BaseApplication mApplication;
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<? extends BaseInfo> mDatas = new ArrayList<BaseInfo>();

    public BaseObjectListAdapter(BaseApplication application, Context context,
                                 List<? extends BaseInfo> datas) {
        mApplication = application;
        mContext = context;
        mInflater = LayoutInflater.from(context);

        if (datas != null) {
            mDatas = datas;
        }

    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public List<? extends BaseInfo> getDatas() {
        return mDatas;
    }


}
