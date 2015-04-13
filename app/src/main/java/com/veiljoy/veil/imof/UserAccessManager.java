package com.veiljoy.veil.imof;

import android.content.Context;
import android.util.Log;

import com.veiljoy.veil.im.IMUserBase;
import com.veiljoy.veil.utils.SharePreferenceUtil;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.packet.XMPPError;

import com.veiljoy.veil.utils.Constants;

/**
 * Created by zhongqihong on 15/4/11.
 */
public class UserAccessManager implements IMUserBase.OnUserRegister, IMUserBase.OnUserLogin {


    private Context context;
    private LoginConfig loginConfig;

    public UserAccessManager(Context context) {

        this.context = context;
    }


    @Override
    public void onPreRegister() {

    }

    @Override
    public int onRegister(String name, String psw) {
        String username = SharePreferenceUtil.getName();
        String password = SharePreferenceUtil.getPasswd();

        IQ result = null;
        try {
            XMPPConnection connection = XmppConnectionManager.getInstance()
                    .getConnection();
            if (!connection.isConnected())
                connection.connect();
            Registration reg = new Registration();
            reg.setType(IQ.Type.SET);
            reg.setUsername(username);
            reg.setPassword(password);

            reg.setTo(connection.getServiceName());

            PacketFilter filter = new AndFilter(new PacketIDFilter(
                    reg.getPacketID()), new PacketTypeFilter(IQ.class));
            PacketCollector collector = XmppConnectionManager.getInstance()
                    .getConnection().createPacketCollector(filter);
            XmppConnectionManager.getInstance().getConnection().sendPacket(reg);
            result = (IQ) collector.nextResult(SmackConfiguration
                    .getPacketReplyTimeout());
            // Stop queuing results
            collector.cancel();// 停止请求results（是否成功的结果）

            if (result == null) {
                return Constants.SERVER_UNAVAILABLE;
            } else if (result.getType() == IQ.Type.ERROR) {
                if (result.getError().toString().equalsIgnoreCase("conflict(409)")) {
                    return Constants.REGISTER_RESULT_EXIST;
                } else {
                    return Constants.REGISTER_RESULT_FAIL;
                }
            } else if (result.getType() == IQ.Type.RESULT) {

                return Constants.REGISTER_RESULT_SUCCESS;

            }

        } catch (Exception xee) {
            xee.printStackTrace();
            return Constants.SERVER_UNAVAILABLE;
        }

        return Constants.REGISTER_RESULT_FAIL;
    }

    @Override
    public boolean onRegisterResult(int code) {


        if (code == Constants.REGISTER_RESULT_SUCCESS)
            return true;
        else
            return false;
    }

    @Override
    public void preLogin() {

    }

    @Override
    public int onLogin(String name, String psw) {


        String username = name;
        String password = psw;

        Log.v("login", "trying to login with name :" + name + " ,psw:" + psw);
        try {
            XMPPConnection connection = XmppConnectionManager.getInstance()
                    .getConnection();

            Log.v("login", "connection " + connection == null ? "null" : "!=null");
            if (!connection.isConnected())
                connection.connect();
            Log.v("login", "login ...");
            connection.login(username, password);
            Log.v("login", "login finished...");
            // OfflineMsgManager.getInstance(activitySupport).dealOfflineMsg(connection);
            connection.sendPacket(new Presence(Presence.Type.available));

                /*
                *

                if (loginConfig.isNovisible()) {// 隐身登录
                    Presence presence = new Presence(Presence.Type.unavailable);
                    Collection<RosterEntry> rosters = connection.getRoster()
                            .getEntries();
                    for (RosterEntry rosterEntry : rosters) {
                        presence.setTo(rosterEntry.getUser());
                        connection.sendPacket(presence);
                    }
                }
                loginConfig.setUsername(username);
                if (loginConfig.isRemember()) {
                    loginConfig.setPassword(password);
                } else {
                    loginConfig.setPassword("");
                }
                loginConfig.setOnline(true);

                */
            return Constants.LOGIN_SUCCESS;
        } catch (Exception xee) {

            Log.v("login", "login exception: " + xee.getMessage());
            if (xee instanceof XMPPException) {
                XMPPException xe = (XMPPException) xee;
                final XMPPError error = xe.getXMPPError();
                int errorCode = 0;
                if (error != null) {
                    errorCode = error.getCode();
                }
                if (errorCode == 401) {
                    return Constants.LOGIN_ERROR_ACCOUNT_PASS;
                } else if (errorCode == 403) {
                    return Constants.LOGIN_ERROR_ACCOUNT_PASS;
                } else {
                    return Constants.SERVER_UNAVAILABLE;
                }
            } else {
                return Constants.LOGIN_ERROR;
            }
        }

    }

    @Override
    public boolean onLoginResult(int code) {

        if(code==Constants.LOGIN_SUCCESS)
            return true;
        else
            return false;
    }
}
