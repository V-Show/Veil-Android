package com.veiljoy.veil.imImpl;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.veiljoy.veil.BaseApplication;
import com.veiljoy.veil.R;
import com.veiljoy.veil.activity.ActivityChat;
import com.veiljoy.veil.im.IMMessage;
import com.veiljoy.veil.memory.ImageCache;
import com.veiljoy.veil.utils.DateUtils;

/**
 * Created by zhongqihong on 15/4/2.
 */

public abstract class IMMessageItem {

    protected Context mContext;
    protected View mRootView;

    /**
     * TimeStampContainer
     */
    private RelativeLayout mLayoutTimeStampContainer;
    private TextView mTVTimeStampTime;
    private TextView mTVTimeStampDistance;

    /**
     * LeftContainer
     */
    private RelativeLayout mLayoutLeftContainer;
    private LinearLayout mLayoutStatus;
    private TextView mTVStatus;

    /**
     * MessageContainer
     */
    protected LinearLayout mLayoutMessageContainer;

    /**
     * RightContainer
     */
    private LinearLayout mLayoutRightContainer;
    private ImageView mIvPhotoView;

    protected LayoutInflater mInflater;
    protected IMMessage mMsg;

    protected int mBackground;


    public IMMessageItem(IMMessage msg, Context context) {
        mMsg = msg;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void init(int source) {

        if (source == IMMessage.RECV) {

            mRootView = mInflater.inflate(R.layout.message_recv_template, null);
            mBackground = R.drawable.bg_message_box_receive;

        } else if (source == IMMessage.SEND) {
            mRootView = mInflater.inflate(R.layout.message_send_template, null);
            mBackground = R.drawable.bg_message_box_send;

        }
        if (mRootView != null) {
            initViews(mRootView);
        }

    }

    protected void initViews(View view) {

        mLayoutTimeStampContainer = (RelativeLayout) view
                .findViewById(R.id.message_layout_timecontainer);
        mTVTimeStampTime = (TextView) view
                .findViewById(R.id.message_timestamp_htv_time);
        mTVTimeStampDistance = (TextView) view
                .findViewById(R.id.message_timestamp_htv_distance);

        mLayoutLeftContainer = (RelativeLayout) view
                .findViewById(R.id.message_layout_leftcontainer);
        mLayoutStatus = (LinearLayout) view
                .findViewById(R.id.message_layout_status);
        mTVStatus = (TextView) view.findViewById(R.id.message_htv_status);

        mLayoutMessageContainer = (LinearLayout) view
                .findViewById(R.id.message_layout_messagecontainer);
        mLayoutMessageContainer.setBackgroundResource(mBackground);

        mLayoutRightContainer = (LinearLayout) view
                .findViewById(R.id.message_layout_rightcontainer);
        mIvPhotoView = (ImageView) view.findViewById(R.id.message_iv_userphoto);
        onInitViews();
    }

    public void fillContent() {
        fillTimeStamp();
        fillStatus();
        fillMessage();
        fillPhotoView();
    }

    protected void fillMessage() {
        onFillMessage();
    }

    protected void fillTimeStamp() {
        mLayoutTimeStampContainer.setVisibility(View.VISIBLE);
        if (mMsg.getmLTime() != 0) {
            mTVTimeStampTime.setText(DateUtils.formatDate(mContext,
                    mMsg.getmLTime()));
        }
        if (mMsg.getmDistance() != null) {
            mTVTimeStampDistance.setText(mMsg.getmDistance());
        } else {
            mTVTimeStampDistance.setText("900 m");
        }
    }

    protected void fillStatus() {
        mLayoutLeftContainer.setVisibility(View.VISIBLE);
        mLayoutStatus
                .setBackgroundResource(R.drawable.bg_message_status_sended);
        mTVStatus.setText("送达");
    }

    protected void fillPhotoView() {
        mLayoutRightContainer.setVisibility(View.VISIBLE);
        mIvPhotoView.setImageBitmap(ImageCache.getAvatar(mMsg.getmAvatar()));
    }

    protected void refreshAdapter() {
        ((ActivityChat) mContext).refreshAdapter();
    }

    public View getRootView() {
        return mRootView;
    }

    protected abstract void onInitViews();

    protected abstract void onFillMessage();

}
