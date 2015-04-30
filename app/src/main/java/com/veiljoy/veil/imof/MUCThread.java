package com.veiljoy.veil.imof;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.veiljoy.veil.bean.UserInfo;
import com.veiljoy.veil.utils.Constants;
import com.veiljoy.veil.xmpp.base.XmppConnectionManager;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by suyu on 2015/4/27.
 * 这个thread是用于取得房间成员信息的
 */
public class MUCThread extends Thread {
    MultiUserChat muc;
    Handler handler;
    Handler callerHandler;
    Map<String, UserInfo> userInfoList = new HashMap<String, UserInfo>();

    public static int UPDATE_ALL = 0;
    public static int ADD_ONE = 1;
    public static int REMOVE_ONE = 2;

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
                if (msg.what == UPDATE_ALL) {
                    userInfoList.clear();

                    java.util.Iterator<java.lang.String> it = muc.getOccupants().iterator();
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
                        } catch (SmackException | XMPPException.XMPPErrorException e) {
                            e.printStackTrace();
                        }
                        userInfoList.put(user.getmName(), user);
                    }

                    // notify caller
                    Message message = handler.obtainMessage();
                    message.obj = userInfoList;
                    callerHandler.sendMessage(message);
                } else if (msg.what == ADD_ONE) {
                    // add the one to userInfoList
                    String name = (String) msg.obj;
                    // turn participant jid to node
                    name = name.substring(name.indexOf("/") + 1);
                    if (!userInfoList.containsKey(name)) {
                        UserInfo user = new UserInfo();
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
                            byte[] avatar = vcard.getAvatar();
                            user.setmAvatar(avatar);
                        } catch (XMPPException | SmackException.NotConnectedException | SmackException.NoResponseException e) {
                            e.printStackTrace();
                        }
                        userInfoList.put(user.getmName(), user);

                        // notify caller
                        Message message = handler.obtainMessage();
                        message.obj = userInfoList;
                        callerHandler.sendMessage(message);
                    }
                } else if (msg.what == REMOVE_ONE) {
                    // add the one to userInfoList
                    String name = (String) msg.obj;
                    // turn participant jid to node
                    name = name.substring(name.indexOf("/") + 1);
                    userInfoList.remove(name);

                    // notify caller
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
