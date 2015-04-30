package com.veiljoy.veil.imof;

import android.os.AsyncTask;
import android.util.Log;

import com.veiljoy.veil.android.BaseActivity;
import com.veiljoy.veil.activity.ActivityChat;
import com.veiljoy.veil.android.BaseApplication;
import com.veiljoy.veil.utils.AppStates;
import com.veiljoy.veil.utils.Constants;

import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhongqihong on 15/4/15.
 */
public class MUCJoinTask extends AsyncTask<String, Integer, MultiUserChat> {
    String roomName = null;
    String roomJid = null;
    OnJoinMUCListener mOnJoinMUCListener;
    BaseActivity mActivity;
    final String TAG = this.getClass().toString();

    public interface OnJoinMUCListener {
        void onResult(MultiUserChat muc);
    }

    public MUCJoinTask(OnJoinMUCListener l, BaseActivity activity) {
        mActivity = activity;
        this.mOnJoinMUCListener = l;
    }

    @Override
    protected void onPreExecute() {
        //  mActivity.showCustomToast("请求服务器房间号...");
    }

    @Override
    protected MultiUserChat doInBackground(String... params) {
//        boolean flag = false;
//
//        MUCHelper.createRoom(Constants.DEFAULT_ROOM_JID);
//
//        List<List<Map<String, String>>> childs = new ArrayList<List<Map<String, String>>>();
//        MUCHelper.fetchHostRoom(childs);
//        for (int i = 0; i < childs.size(); i++) {
//            if (!flag) {
//                //获得上层房间组
//                List<Map<String, String>> roomGroup = childs.get(i);
//                for (int j = 0; j < roomGroup.size(); j++) {
//                    Map<String, String> room = roomGroup.get(j);
//                    if (room.containsKey(Constants.MAP_ROOM_NAME_KEY)) {
//                        if (room.get(Constants.MAP_ROOM_NAME_KEY) != null) {
//                            roomName = room.get(Constants.MAP_ROOM_NAME_KEY);
//                        }
//                    }
//
//                    if (room.containsKey(Constants.MAP_ROOM_JID_KEY)) {
//                        if (room.get(Constants.MAP_ROOM_JID_KEY) != null) {
//                            roomJid = room.get(Constants.MAP_ROOM_JID_KEY);
//                        }
//                    }
//
//                    if (roomName != null && roomJid != null) {
//                        flag = true;
//                        break;
//                    }
//                }
//            } else {
//                break;
//            }
//        }
//
//        MultiUserChat muc=MUCHelper.JoinRoom(roomJid);
//
//        if(muc!=null){
//            ((BaseApplication) mActivity.getApplication()).enter();
//        }
//        return muc;
        return MUCRoomManager.getInstance(mActivity).enterRoom();
    }

    @Override
    protected void onPostExecute(MultiUserChat muc) {
        if (mOnJoinMUCListener != null)
            mOnJoinMUCListener.onResult(muc);
        else {
            if (muc != null) {
                mActivity.startActivity(ActivityChat.class, null);
                mActivity.finish();
            } else {
                mActivity.showCustomToast("获得房间异常");
            }
        }
    }
}
