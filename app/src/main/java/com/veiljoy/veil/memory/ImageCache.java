package com.veiljoy.veil.memory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.veiljoy.veil.BaseApplication;
import com.veiljoy.veil.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhongqihong on 15/4/2.
 */
public class ImageCache {


    private static Bitmap mDefaultAvatar;
    public static Context mContext;
    public static void init(Context context){
        mContext=context;
        mDefaultAvatar = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.ic_common_header);
    }

    private static final String AVATAR_DIR = "avatar/";
    public static Map<String, SoftReference<Bitmap>> mAvatarCache = new HashMap<String, SoftReference<Bitmap>>();

    public static Bitmap getAvatar(String imageName) {
        if (mAvatarCache.containsKey(imageName)) {
            Reference<Bitmap> reference = mAvatarCache.get(imageName);
            if (reference.get() == null || reference.get().isRecycled()) {
                mAvatarCache.remove(imageName);
            } else {
                return reference.get();
            }
        }
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = mContext.getAssets().open(AVATAR_DIR + imageName);
            bitmap = BitmapFactory.decodeStream(is);
            if (bitmap == null) {
                throw new FileNotFoundException(imageName + "is not find");
            }
            mAvatarCache.put(imageName, new SoftReference<Bitmap>(bitmap));
            return bitmap;
        } catch (Exception e) {
            return mDefaultAvatar;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {

            }
        }
    }
}
