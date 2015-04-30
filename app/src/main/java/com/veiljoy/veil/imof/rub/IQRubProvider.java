package com.veiljoy.veil.imof.rub;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by Administrator on 2015/4/29.
 */
public class IQRubProvider extends IQProvider<RubInfo> {
    @Override
    public RubInfo parse(XmlPullParser parser, int initialDepth)
            throws XmlPullParserException, IOException, SmackException {
        RubInfo info = new RubInfo();

        String room = info.getRoom();
        boolean create = info.isCreate();

        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    String startTag = parser.getName();
                    switch (startTag) {
                        case "room":
                            room = parser.nextText();
                            break;
                        case "create":
                            create = Boolean.parseBoolean(parser.nextText());
                            break;
                    }
                    break;
                case XmlPullParser.END_TAG:
                    String endTag = parser.getName();
                    switch (endTag) {
                        case "query":
                            done = true;
                            break;
                    }
                    break;
            }
        }

        info.setCreate(create);
        info.setRoom(room);
        System.out.println("IQRubProvider rub info: " + info.toString());
        return info;
    }
}
