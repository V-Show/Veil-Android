package com.veiljoy.veil.imof;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.veiljoy.veil.bean.UserInfo;
import com.veiljoy.veil.utils.Constants;
import com.veiljoy.veil.xmpp.base.XmppConnectionManager;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.VCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suyu on 2015/4/27.
 * 这个thread是用于取得房间成员信息的
 */
public class MUCThread extends Thread {
    MultiUserChat muc;
    Handler handler;
    Handler callerHandler;
    List<UserInfo> userInfoList = new ArrayList<UserInfo>();

    public MUCThread(MultiUserChat muc, Handler callerHandler) {
        super();
        this.muc = muc;
        this.callerHandler = callerHandler;
    }

    @Override
    public void run() {
        Looper.prepare();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // process incoming messages here
                if (msg.obj.equals("#all")) {
                    userInfoList.clear();

                    java.util.Iterator<java.lang.String> it = muc.getOccupants();
                    while (it.hasNext()) {
                        String name = it.next();
                        UserInfo user = new UserInfo();
                        Log.v("suyu", "name@room: " + name);
                        name = name.substring(name.indexOf("/") + 1);
                        user.setmName(name);
                        // get vcard
                        try {
                            name = name + "@veil";
                            VCard vcard = new VCard();
                            vcard.load(XmppConnectionManager.getInstance()
                                    .getConnection(), name);
                            Log.v("suyu", "name: " + name);
                            String gender = vcard.getField(Constants.USER_CARD_FILED_GENDER);
                            if (gender != null) {
                                user.setmGender(Integer.parseInt(gender));
                            }
                            Log.v("suyu", "gender: " + gender);
                            user.setmAvatar(vcard.getAvatar());
                        } catch (XMPPException e) {
                            e.printStackTrace();
                        }
                        userInfoList.add(user);
                    }

                    // get all participants
                    Message message = handler.obtainMessage();
                    message.obj = userInfoList;
                    callerHandler.sendMessage(message);
                }
            }
        };

        Looper.loop();
    }

    public Handler getHandler() {
        return handler;
    }
}
