package com.veiljoy.veil.imof;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.veiljoy.veil.im.IMChatBase;
import com.veiljoy.veil.im.IMMessage;
import com.veiljoy.veil.utils.AppStates;
import com.veiljoy.veil.utils.Constants;
import com.veiljoy.veil.utils.FormatTools;
import com.veiljoy.veil.utils.SharePreferenceUtil;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jivesoftware.smackx.vcardtemp.provider.VCardProvider;

import java.io.ByteArrayInputStream;

/**
 * Created by zhongqihong on 15/4/11.
 */
public class IMOFChatImpl implements IMChatBase {

    @Override
    public void send(IMMessage msg) {
    }

    @Override
    public void wrap() {
    }

    @Override
    public void receive() {
    }

    /**
     * 获取用户头像信息
     *
     * @param connection
     * @param user
     * @return
     */
    public static Drawable getUserImage(XMPPConnection connection, String user) {
        ByteArrayInputStream bais = null;
        try {
            VCard vcard = new VCard();
            // 加入这句代码，解决No VCard for
            ProviderManager.addIQProvider("vCard", "vcard-temp",
                    new VCardProvider());

            vcard.load(connection, user + "@" + connection.getServiceName());

            if (vcard == null || vcard.getAvatar() == null)
                return null;
            bais = new ByteArrayInputStream(vcard.getAvatar());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bais == null)
            return null;
        return FormatTools.getInstance().InputStream2Drawable(bais);
    }

    /**
     * @deprecated
     */
    @java.lang.Deprecated
    public static void setUserVCard(XMPPConnection connection)
            throws SmackException.NotConnectedException, SmackException.NoResponseException, XMPPException.XMPPErrorException {
        VCard vCard = new VCard();
        vCard.load(connection);
        //vCard.setEmailHome("lulu@sina.com");
        vCard.setOrganization("Conference");
        vCard.setNickName(SharePreferenceUtil.getName());
        vCard.setField(Constants.USER_CARD_FILED_GENDER, SharePreferenceUtil.getGender() + "");
        vCard.setPhoneWork(Constants.USER_CARD_FILED_PHONE, "110");
        vCard.setField(Constants.USER_CARD_FILED_DESC, "info about user");
        vCard.setAvatar(FormatTools.Bitmap2Bytes(AppStates.getUserAvatar()));
        vCard.save(connection);
        Log.v("ChatImpl", "添加成功");
    }

    public static Bitmap getUserAvatar(XMPPConnection connection) {
        String userName = connection.getUser();
        Bitmap bitmap = null;
        try {
            Log.v("ChatImpl", "获取用户头像信息: " + userName);
            VCard vcard = new VCard();
            vcard.load(connection, connection.getUser());

            if (vcard == null || vcard.getAvatar() == null) {
                Log.v("ChatImpl", "vcard loaded fail");
                return null;
            }
            bitmap = BitmapFactory.decodeByteArray(vcard.getAvatar(), 0, vcard.getAvatar().length);
            Log.v("ChatImpl", "get user avatar success..");
        } catch (Exception e) {
            Log.v("ChatImpl", "get user avatar failed.." + e.getMessage());
        }
        return bitmap;
    }

}
