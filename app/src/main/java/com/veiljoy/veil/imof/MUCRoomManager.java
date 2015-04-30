package com.veiljoy.veil.imof;

import android.app.Activity;
import android.util.Log;

import com.veiljoy.veil.android.BaseApplication;
import com.veiljoy.veil.im.IMRoom;
import com.veiljoy.veil.imof.rub.IQRubProvider;
import com.veiljoy.veil.imof.rub.RubInfo;
import com.veiljoy.veil.imof.rub.RubReq;
import com.veiljoy.veil.utils.AppStates;
import com.veiljoy.veil.utils.Constants;
import com.veiljoy.veil.utils.SharePreferenceUtil;
import com.veiljoy.veil.xmpp.base.XmppConnectionManager;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by zhongqihong on 15/4/29.
 */
public class MUCRoomManager implements IMRoom {

    Activity mActivity;
    public static MUCRoomManager instance = null;

    private MUCRoomManager(Activity a) {
        mActivity = a;

        ProviderManager.addIQProvider("query", "com.veil.rub",
                new IQRubProvider());
    }

    public static MUCRoomManager getInstance(Activity a) {
        if (instance == null) {
            synchronized (MUCRoomManager.class) {
                if (instance == null) {
                    instance = new MUCRoomManager(a);
                }
            }
        }

        return instance;
    }

    @Override
    public String getRoomName() {
        return SharePreferenceUtil.getRoom();
    }

    @Override
    public List<String> getRoomMembers() {
        return null;
    }

    /*
    * 进入一个特定的房间
    * */
    public boolean enterRoom(String roomName) {
        MultiUserChat muc = MUCHelper.JoinRoom(roomName);
        if (muc != null) {
            AppStates.setMultiUserChat(muc);
            return true;
        } else return false;
    }

    /*
    * 进任何一个有效的房间,如果服务器没有找到合适的房间，则重新创建一个新的房间
    * */
    public String getAnyRoom(String defRoomName) {
        MultiUserChat muc = null;
        Map<String, String> room = MUCHelper.getAnyRoom();
        if (room == null || room.size() == 0) {
            muc = MUCHelper.createRoom(defRoomName);
        } else {
            Collection roomJids = room.values();
            Iterator<String> ite = roomJids.iterator();
            while (ite.hasNext()) {
                String roomJid = ite.next();
                //获取服务器任意房间成功
                if (roomJid != null) {
                    return roomJid;
                }
            }
        }
        //重新创建房间成功
        if (muc != null) {
            return defRoomName;
        }

        return null;
    }

    public RubInfo getRubRoom() {
        RubInfo info = null;
        RubReq req = new RubReq();
        try {
            info = XmppConnectionManager.getInstance().getConnection().createPacketCollectorAndSend(
                    new StanzaTypeFilter(RubInfo.class), req)
                    .nextResultOrThrow();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        return info;
    }

    public MultiUserChat enterRubRoom() {
        Log.v("suyu", "enter rub room");
        MultiUserChat muc = null;
        RubInfo info = getRubRoom();
        if (info.isCreate()) {
            muc = MUCHelper.createRoom(info.getRoom());
        } else {
            muc = MUCHelper.JoinRoom(info.getRoom());
        }
        if (muc != null) {
            AppStates.setMultiUserChat(muc);
        }
        return muc;
    }

    public MultiUserChat enterRoom() {
        Log.v("MUCRoomManager", "enter room");
        MUCHelper.login(SharePreferenceUtil.getName(), SharePreferenceUtil.getPasswd());

        MultiUserChat muc = AppStates.getMultiUserChat();
        //判断是否已经在房间里面了，不需要重新进入
        if (muc == null) {
            Log.v("MUCRoomManager", "muc" + muc == null ? "=null" : "!=null");
            String roomJid = getAnyRoom(Constants.DEFAULT_ROOM_JID);
            if (roomJid != null) {
                muc = MUCHelper.JoinRoom(roomJid);
                Log.v("MUCRoomManager", "roomJid" + roomJid == null ? "=null" : "!=null");
                if (muc != null) {
                    ((BaseApplication) mActivity.getApplication()).enter();
                    AppStates.setMultiUserChat(muc);
                    SharePreferenceUtil.setRoom(roomJid);
                }
            }
        }
        return muc;
    }
}
