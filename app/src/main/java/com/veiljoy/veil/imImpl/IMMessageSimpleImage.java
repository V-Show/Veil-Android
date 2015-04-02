package com.veiljoy.veil.imImpl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.veiljoy.veil.R;
import com.veiljoy.veil.im.IMMessage;
import com.veiljoy.veil.im.IMMessageItem;
import com.veiljoy.veil.utils.PhotoUtils;

/**
 * Created by zhongqihong on 15/4/2.
 */
public class IMMessageSimpleImage extends IMMessageItem {

    protected Bitmap mBitmap;
    protected ImageView mIvImage;

    public IMMessageSimpleImage(IMMessage msg,Context context){

        super(msg,context);

    }

    @Override
    protected void onInitViews() {
        View view = mInflater.inflate(R.layout.message_simple_image, null);
        mLayoutMessageContainer.addView(view);
        mIvImage = (ImageView) view.findViewById(R.id.message_iv_msg_simple_image);
    }

    @Override
    protected void onFillMessage() {
        mBitmap= BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_chat_avatar_10);

        //mBitmap = PhotoUtils.getBitmapFromFile(mMsg.getmContent());
        mIvImage.setImageBitmap(mBitmap);

    }
}
