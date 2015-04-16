package com.veiljoy.veil.xmpp.base;

import android.util.Log;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;

import com.veiljoy.veil.imof.LoginConfig;
import com.veiljoy.veil.utils.SharePreferenceUtil;

import com.veiljoy.veil.utils.Constants;

/**
 * XMPP服务器连接工具类.
 */
public class XmppConnectionManager {
    private XMPPConnection connection;
    private static ConnectionConfiguration connectionConfig;
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
        Connection.DEBUG_ENABLED = false;
        ProviderManager pm = ProviderManager.getInstance();
        configure(pm);

        connectionConfig = new ConnectionConfiguration(
                loginConfig.getXmppHost(), loginConfig.getXmppPort(),
                loginConfig.getXmppServiceName());
        connectionConfig.setSASLAuthenticationEnabled(false);// 不使用SASL验证，设置为false
        connectionConfig
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        // 允许自动连接
        connectionConfig.setReconnectionAllowed(false);
        // 允许登陆成功后更新在线状态
        connectionConfig.setSendPresence(true);
        // 收到好友邀请后manual表示需要经过同意,accept_all表示不经同意自动为好友
        Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
        connection = new XMPPConnection(connectionConfig);
        try {
            connection.connect();
        } catch (Exception e) {
            Log.v("xmpp","connect "+e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 返回一个有效的xmpp连接,如果无效则返回空.
     */
    public XMPPConnection getConnection() {

        if (connection == null) {
            init(getLoginConfig());
            Log.v("xmpp","connection "+connection==null?"=null":"!=null");
        }
        return connection;
    }

    /**
     * 销毁xmpp连接.
     */
    public void disconnect() {
        if (connection != null) {
            connection.disconnect();
        }
    }

    public void configure(ProviderManager pm) {

        // Private Data Storage
        pm.addIQProvider("query", "jabber:iq:private",
                new PrivateDataManager.PrivateDataIQProvider());

        // Time
        try {
            pm.addIQProvider("query", "jabber:iq:time",
                    Class.forName("org.jivesoftware.smackx.packet.Time"));
        } catch (ClassNotFoundException e) {
        }

        // XHTML
        pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",
                new XHTMLExtensionProvider());

        // Roster Exchange
        pm.addExtensionProvider("x", "jabber:x:roster",
                new RosterExchangeProvider());
        // Message Events
        pm.addExtensionProvider("x", "jabber:x:event",
                new MessageEventProvider());
        // Chat State
        pm.addExtensionProvider("active",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        pm.addExtensionProvider("composing",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        pm.addExtensionProvider("paused",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        pm.addExtensionProvider("inactive",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        pm.addExtensionProvider("gone",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());

        // FileTransfer
        pm.addIQProvider("si", "http://jabber.org/protocol/si",
                new StreamInitiationProvider());

        // Group Chat Invitations
        pm.addExtensionProvider("x", "jabber:x:conference",
                new GroupChatInvitation.Provider());
        // Service Discovery # Items
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
                new DiscoverItemsProvider());
        // Service Discovery # Info
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
                new DiscoverInfoProvider());
        // Data Forms
        pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());
        // MUC User
        pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
                new MUCUserProvider());
        // MUC Admin
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
                new MUCAdminProvider());
        // MUC Owner
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
                new MUCOwnerProvider());
        // Delayed Delivery
        pm.addExtensionProvider("x", "jabber:x:delay",
                new DelayInformationProvider());
        // Version
        try {
            pm.addIQProvider("query", "jabber:iq:version",
                    Class.forName("org.jivesoftware.smackx.packet.Version"));
        } catch (ClassNotFoundException e) {
        }
        // VCard
        pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());
        // Offline Message Requests
        pm.addIQProvider("offline", "http://jabber.org/protocol/offline",
                new OfflineMessageRequest.Provider());
        // Offline Message Indicator
        pm.addExtensionProvider("offline",
                "http://jabber.org/protocol/offline",
                new OfflineMessageInfo.Provider());
        // Last Activity
        pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());
        // User Search
        pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());
        // SharedGroupsInfo
        pm.addIQProvider("sharedgroup",
                "http://www.jivesoftware.org/protocol/sharedgroup",
                new SharedGroupsInfo.Provider());
        // JEP-33: Extended Stanza Addressing
        pm.addExtensionProvider("addresses",
                "http://jabber.org/protocol/address",
                new MultipleAddressesProvider());

    }

    public static LoginConfig getLoginConfig() {


        LoginConfig loginConfig = new LoginConfig();
        String username = SharePreferenceUtil.getName();
        String password = SharePreferenceUtil.getPasswd();
        loginConfig.setXmppHost(Constants.XMPP_HOST_IP);
        loginConfig.setXmppPort(Integer.parseInt(Constants.XMPP_HOST_PORT));
        loginConfig.setUsername(username);
        loginConfig.setPassword(password);
        loginConfig.setXmppServiceName(Constants.XMPP_HOST_NAME);
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
