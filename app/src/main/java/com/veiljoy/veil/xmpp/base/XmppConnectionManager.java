package com.veiljoy.veil.xmpp.base;

import android.util.Log;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import com.veiljoy.veil.imof.LoginConfig;
import com.veiljoy.veil.utils.SharePreferenceUtil;

import com.veiljoy.veil.utils.Constants;

/**
 * XMPP服务器连接工具类.
 */
public class XmppConnectionManager {
    private AbstractXMPPConnection connection;
    private static XMPPTCPConnectionConfiguration connectionConfig;
    private static XmppConnectionManager xmppConnectionManager;

    private XmppConnectionManager() {

    }

    public static XmppConnectionManager getInstance() {
        if (xmppConnectionManager == null) {
            xmppConnectionManager = new XmppConnectionManager();
        }
        return xmppConnectionManager;
    }

    // init
    public XMPPConnection init(LoginConfig loginConfig) {
        connectionConfig = XMPPTCPConnectionConfiguration
                .builder()
                .setServiceName(loginConfig.getXmppServiceName())
                .setHost(loginConfig.getXmppHost())
                .setPort(loginConfig.getXmppPort())
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setSendPresence(true)  // 允许登陆成功后更新在线状态
                //.setDebuggerEnabled(true) // 打开debug输出
                .build();

        // 收到好友邀请后manual表示需要经过同意,accept_all表示不经同意自动为好友
        Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
        connection = new XMPPTCPConnection(connectionConfig);
        try {
            connection.connect();
        } catch (Exception e) {
            Log.v("xmpp", "connect " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 返回一个有效的xmpp连接,如果无效则返回空.
     */
    public AbstractXMPPConnection getConnection() {
        if (connection == null) {
            init(getLoginConfig());

        }
        return connection;
    }

    /**
     * 重新创建一个连接
     */
    public AbstractXMPPConnection refresh() {
        disconnect();
        connection = null;
        return getConnection();
    }

    /**
     * 销毁xmpp连接.
     */
    public void disconnect() {
        if (connection != null) {
            connection.disconnect();
        }
    }

    public static LoginConfig getLoginConfig() {
        LoginConfig loginConfig = new LoginConfig();
        String username = SharePreferenceUtil.getName();
        String password = SharePreferenceUtil.getPasswd();
        loginConfig.setXmppHost(Constants.XMPP_HOST_IP);
        loginConfig.setXmppPort(Integer.parseInt(Constants.XMPP_HOST_PORT));
        loginConfig.setUsername(username);
        loginConfig.setPassword(password);
        loginConfig.setXmppServiceName(Constants.XMPP_SERVICE_NAME);
//		loginConfig.setAutoLogin(Boolean
//				.getBoolean(Constants.IS_AUTOLOGIN_VALUE));
//		loginConfig.setNovisible(Boolean
//				.getBoolean(Constants.IS_NOVISIBLE_VALUE));
//		loginConfig
//				.setRemember(Boolean.getBoolean(Constants.IS_REMEMBER_VALUE));
//		loginConfig.setFirstStart(Boolean
//				.getBoolean(Constants.IS_FIRSTSTART_VALUE));
        return loginConfig;
    }
}
