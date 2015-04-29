package com.veiljoy.veil.imof;

import android.content.Context;
import android.util.Log;

import com.veiljoy.veil.im.IMUserBase;
import com.veiljoy.veil.utils.AppStates;
import com.veiljoy.veil.utils.SharePreferenceUtil;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.iqregister.packet.Registration;

import com.veiljoy.veil.utils.Constants;
import com.veiljoy.veil.xmpp.base.XmppConnectionManager;

import java.io.IOException;

/**
 * Created by zhongqihong on 15/4/11.
 */
public class UserAccessManager implements IMUserBase.OnUserRegister, IMUserBase.OnUserLogin {

    @Override
    public void onPreRegister() {
    }

    @Override
    public int onRegister(String name, String psw) {
        int code = Constants.REGISTER_RESULT_SUCCESS;
        String username = SharePreferenceUtil.getName();
        String password = SharePreferenceUtil.getPasswd();

        try {
            AbstractXMPPConnection connection = XmppConnectionManager.getInstance()
                    .getConnection();
            if (!connection.isConnected()) {
                connection.connect();
            }
            AccountManager accountManager = AccountManager.getInstance(connection);
            // allow sensitive operations like account creation or password changes
            // over an insecure (e.g. unencrypted) connections.
            accountManager.sensitiveOperationOverInsecureConnection(true);
            accountManager.createAccount(username, password);
        } catch (XMPPException e) {
            e.printStackTrace();
            code = Constants.REGISTER_RESULT_FAIL;
            if (e instanceof XMPPException.XMPPErrorException) {
                XMPPException.XMPPErrorException xe = (XMPPException.XMPPErrorException) e;
                if (xe.getXMPPError().getCondition().toString().equals("conflict")) {
                    code = Constants.REGISTER_RESULT_EXIST;
                }
            }
        } catch (SmackException e) {
            e.printStackTrace();
            code = Constants.REGISTER_RESULT_FAIL;
        } catch (IOException e) {
            e.printStackTrace();
            code = Constants.REGISTER_RESULT_FAIL;
        }

        // login after create account
        if (code == Constants.REGISTER_RESULT_SUCCESS) {
            // make a new connection for login
            AbstractXMPPConnection connection = XmppConnectionManager.getInstance().refresh();
            try {
                connection.login(username, password);
            } catch (XMPPException e) {
                code = Constants.LOGIN_ERROR;
                e.printStackTrace();
            } catch (SmackException e) {
                code = Constants.LOGIN_ERROR;
                e.printStackTrace();
            } catch (IOException e) {
                code = Constants.LOGIN_ERROR;
                e.printStackTrace();
            }

            if (code == Constants.REGISTER_RESULT_SUCCESS) {
                AppStates.setAlreadyLogined(true);

                MUCHelper.init(connection);
            }
        }

        return code;
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

        try {
            MUCHelper.login(name, psw);
            return Constants.LOGIN_SUCCESS;
        } catch (Exception xee) {
            Log.v("login", "login exception: " + xee.getMessage());
            if (xee instanceof XMPPException.XMPPErrorException) {
                XMPPException.XMPPErrorException xe = (XMPPException.XMPPErrorException) xee;
                final XMPPError error = xe.getXMPPError();
                XMPPError.Condition errCondition = null;
                if (error != null) {
                    errCondition = error.getCondition();
                }
                if (errCondition.toString().equals("not-authorized")) {
                    return Constants.LOGIN_ERROR_ACCOUNT_PASS;
                } else if (errCondition.toString().equals("forbidden")) {
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
        if (code == Constants.LOGIN_SUCCESS)
            return true;
        else
            return false;
    }
}
