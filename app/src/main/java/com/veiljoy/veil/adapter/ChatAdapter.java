package com.veiljoy.veil.adapter;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.veiljoy.veil.BaseApplication;
import com.veiljoy.veil.R;
import com.veiljoy.veil.bean.BaseInfo;
import com.veiljoy.veil.im.IMMessage;
import com.veiljoy.veil.im.IMMessageFactory;
import com.veiljoy.veil.im.IMMessageItem;

public class ChatAdapter extends BaseObjectListAdapter {

	public ChatAdapter(BaseApplication application, Context context,
			List<? extends BaseInfo> datas) {
		super(application, context, datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {


        View view=null;
        IMMessage msg=(IMMessage)getItem(position);

		if(msg!=null){

            IMMessageItem msgItem= IMMessageFactory.getInstance().getMessageItem(msg,mContext);
            msgItem.fillContent();
            view=msgItem.getRootView();
        }

		return view;
	}
	public void refreshList(List<BaseInfo> items) {
		this.mDatas = items;
		this.notifyDataSetChanged();
	}
}
