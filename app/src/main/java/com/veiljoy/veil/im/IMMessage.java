package com.veiljoy.veil.im;

import android.os.Parcel;
import android.os.Parcelable;

import com.veiljoy.veil.bean.BaseInfo;
import com.veiljoy.veil.utils.Constants;
import com.veiljoy.veil.utils.DateUtils;

import java.util.Date;

/**
 * Created by zhongqihong on 15/3/31.
 */
public class IMMessage extends BaseInfo  implements Parcelable, Comparable<IMMessage>{


    private String mTo;
    private String mFrom;
    private String mContent;
    private int mMessageType;
    private String mTime;
    private String mDistance;
    private long mLTime;
    //消息类型
    private String mUri;
    private String mAvatar;
    public static final int SUCCESS = 0;
    public static final int ERROR = 1;
    public static final int RECV = 0;
    public static final int SEND = 1;
    public static final String KEY_TIME = "immessage.time";
    public static final String IMMESSAGE_KEY = "immessage.key";
    @Override
    public int compareTo(IMMessage oth) {
        if (null == this.getmTime() || null == oth.getmTime()) {
			return 0;
		}
		String format = null;
		String time1 = "";
		String time2 = "";
		if (this.getmTime().length() == oth.getmTime().length()
				&& this.getmTime().length() == 23) {
			time1 = this.getmTime();
			time2 = oth.getmTime();
			format = Constants.MS_FORMART;
		} else {
			time1 = this.getmTime().substring(0, 19);
			time2 = oth.getmTime().substring(0, 19);
		}
		Date da1 = DateUtils.str2Date(time1, format);
		Date da2 = DateUtils.str2Date(time2, format);
		if (da1.before(da2)) {
			return -1;
		}
		if (da2.before(da1)) {
			return 1;
		}

        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUri);
        dest.writeString(mContent);
        dest.writeString(mTime);
        dest.writeString(mFrom);
        if(mMessageType==SEND)
            dest.writeInt(0);
        else
            dest.writeInt(1);

    }
    public static final Parcelable.Creator<IMMessage> CREATOR = new Parcelable.Creator<IMMessage>() {

        @Override
        public IMMessage createFromParcel(Parcel source) {
            IMMessage message = new IMMessage();
            message.setmUri(source.readString());
            message.setmContent(source.readString());
            message.setmTime(source.readString());
            message.setmFrom(source.readString());
            int msg_type=source.readInt();
            if(msg_type==0){
                message.setmMessageType(SEND);
            }
            else{
                message.setmMessageType(RECV);
            }
            return message;
        }

        @Override
        public IMMessage[] newArray(int size) {
            return new IMMessage[size];
        }

    };


    public enum Scheme {

        TEXT("text"),MAP("map"), FILE("file"), IMAGE("image"), VOICE("voice"), UNKNOWN("");


        private String scheme;
        private String uriPrefix;

        Scheme(String scheme) {
            this.scheme = scheme;
            uriPrefix = scheme + "://";
        }


        /**
         * Defines scheme of incoming URI
         *
         * @param uri URI for scheme detection
         * @return Scheme of incoming URI
         */
        public static Scheme ofUri(String uri) {
            if (uri != null) {
                for (Scheme s : values()) {
                    if (s.belongsTo(uri)) {
                        return s;
                    }
                }
            }
            return UNKNOWN;
        }

        private boolean belongsTo(String uri) {
            return uri.startsWith(uriPrefix);
        }

        /**
         * Appends scheme to incoming path
         */
        public String wrap(String path) {
            return uriPrefix + path;
        }

        /**
         * Removed scheme part ("scheme://") from incoming URI
         */
        public String crop(String uri) {
            if (!belongsTo(uri)) {
                throw new IllegalArgumentException(String.format("URI [%1$s] doesn't have expected scheme [%2$s]", uri, scheme));
            }
            return uri.substring(uriPrefix.length());
        }

        public String getScheme(){

            return this.scheme;
        }
    }

    public String getmTo() {
        return mTo;
    }

    public void setmTo(String mTo) {
        this.mTo = mTo;
    }

    public String getmFrom() {
        return mFrom;
    }

    public void setmFrom(String mFrom) {
        this.mFrom = mFrom;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public int getmMessageType() {
        return mMessageType;
    }

    public void setmMessageType(int mMessageType) {
        this.mMessageType = mMessageType;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmDistance() {
        return mDistance;
    }

    public void setmDistance(String mDistance) {
        this.mDistance = mDistance;
    }

    public static int getSuccess() {
        return SUCCESS;
    }

    public static int getError() {
        return ERROR;
    }

    public static int getRecv() {
        return RECV;
    }

    public static int getSend() {
        return SEND;
    }

    public long getmLTime() {
        return mLTime;
    }

    public void setmLTime(long mLTime) {
        this.mLTime = mLTime;
    }

    public String getmAvatar() {
        return mAvatar;
    }

    public void setmAvatar(String mAvatar) {
        this.mAvatar = mAvatar;
    }

    public String getmUri() {
        return mUri;
    }

    public void setmUri(String mUri) {
        this.mUri = mUri;
    }




}
