package com.veiljoy.veil.imof;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.veiljoy.veil.R;
import com.veiljoy.veil.im.IMUserBase;
import com.veiljoy.veil.utils.SharePreferenceUtil;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.RosterEntry;
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

import java.util.Collection;

/**
 * Created by zhongqihong on 15/4/11.
 */
public class UserAccessManager implements IMUserBase.OnUserRegister,IMUserBase.OnUserLogin {



    private Context context;
    private LoginConfig loginConfig;
    public  UserAccessManager(Context context){

        this.context=context;
    }



    @Override
    public void onPreRegister() {

    }

    @Override
    public int onRegister(String name, String psw) {
        String username = SharePreferenceUtil.getName();
        String password = SharePreferenceUtil.getPasswd();

        IQ result =null;
        try {
            XMPPConnection connection = XmppConnectionManager.getInstance()
                    .getConnection();
            if(!connection.isConnected())
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
                    return Constants.REGISTER_RESULT_USER_EXIST;
                } else {
                    return Constants.REGISTER_RESULT_ERROR;
                }
            } else if (result.getType() == IQ.Type.RESULT) {

                return Constants.REGISTER_RESULT_OK;

            }

        } catch (Exception xee) {
//            if (xee instanceof XMPPException) {
//                XMPPException xe = (XMPPException) xee;
//                final XMPPError error = xe.getXMPPError();
//                int errorCode = 0;
//                if (error != null) {
//                    errorCode = error.getCode();
//                }
//                if (errorCode == 401) {
//                    return Constants.LOGIN_ERROR_ACCOUNT_PASS;
//                } else if (errorCode == 403) {
//                    return Constants.LOGIN_ERROR_ACCOUNT_PASS;
//                } else {
//                    return Constants.SERVER_UNAVAILABLE;
//                }
//            } else {
//                return Constants.LOGIN_ERROR;
//            }
        }

        return Constants.REGISTER_RESULT_ERROR;
    }

    @Override
    public boolean onRegisterResult(int code) {

        String status = "注册失败";
        switch (code) {
            case Constants.REGISTER_RESULT_OK: // 注册成功
                status = "注册成功 ,账号为："+ SharePreferenceUtil.getName();
                return true;
            case Constants.REGISTER_RESULT_USER_EXIST:// 已经存在该用户
                status = "已经存在该用户";
                break;
            case Constants.REGISTER_RESULT_ERROR:// 注册失败
                status = "注册失败";
                break;
            case Constants.SERVER_UNAVAILABLE:// 服务器没有返回结果
                status = "服务器没有返回结果";
                break;
        }
        Log.v("register", "register code"+status);

        return false;
    }

    @Override
    public void preLogin() {

    }

    @Override
    public int onLogin(String name, String psw) {


            String username = name;
            String password = psw;
            try {
                XMPPConnection connection = XmppConnectionManager.getInstance()
                        .getConnection();
                connection.connect();
                connection.login(username, password);
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
                return Constants.LOGIN_SECCESS;
            } catch (Exception xee) {
                if (xee instanceof XMPPException) {
                    XMPPException xe = (XMPPException) xee;
                    final XMPPError error = xe.getXMPPError();
                    int errorCode = 0;
                    if (error != null) {
                        errorCode = error.getCode();
                    }
                    if (errorCode == 401) {
                        return Constants.LOGIN_ERROR_ACCOUNT_PASS;
                    }else if (errorCode == 403) {
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
    public void onLoginResult(int code) {

        switch (code) {

            case Constants.LOGIN_SECCESS: // 登录成功
                Toast.makeText(context, this.context.getString( R.string.login_success ), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent();
////			if (loginConfig.isFirstStart()) {//第一次启动
////				intent.setClass(context, GuideViewActivity.class);
////				loginConfig.setFirstStart(false);
////			} else
////			{
//                intent.setClass(context, MainTabActivity.class);
//                intent.putExtra(Constants.MSGKEY, list);
////			}
//                activitySupport.saveLoginConfig(loginConfig);//保存设置
//                activitySupport.startService();
//                context.startActivity(intent);
//                ((LoginActivity)this.context).finish();
//
//                Intent mIntentFilter = new Intent();
//                mIntentFilter.setAction("www.veiljoy.veil.welcome.exit");
//                context.sendBroadcast(mIntentFilter);

                //JPushInterface.setAliasAndTags(activitySupport.getEimApplication(), null, (Set<String>) getTagSet(), mTagsCallback);

                Log.v("UserAccessManager","login success");
                break;
            case Constants.LOGIN_ERROR_ACCOUNT_PASS:// 账户或者密码错误
                Toast.makeText(
                        context,
                        context.getResources().getString(
                                R.string.message_invalid_username_password),
                        Toast.LENGTH_SHORT).show();
                Log.v("UserAccessManager","login LOGIN_ERROR_ACCOUNT_PASS");
                break;
            case Constants.SERVER_UNAVAILABLE:// 服务器连接失败
                Toast.makeText(
                        context,
                        context.getResources().getString(
                                R.string.message_server_unavailable),
                        Toast.LENGTH_SHORT).show();
                Log.v("UserAccessManager","login SERVER_UNAVAILABLE");
                break;
            case Constants.LOGIN_ERROR:// 未知异常
                Toast.makeText(
                        context,
                        context.getResources().getString(
                                R.string.unrecoverable_error), Toast.LENGTH_SHORT)
                        .show();
                Log.v("UserAccessManager","login LOGIN_ERROR");
                break;
        }

    }
}
