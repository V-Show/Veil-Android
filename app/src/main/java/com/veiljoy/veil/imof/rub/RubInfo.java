package com.veiljoy.veil.imof.rub;

import org.jivesoftware.smack.packet.IQ;

/**
 * Created by Administrator on 2015/4/29.
 */
public class RubInfo extends IQ {

    public static final String ELEMENT = QUERY_ELEMENT;
    public static final String NAMESPACE = "com.veil.rub";

    String room;
    boolean create;

    protected RubInfo() {
        super(ELEMENT, NAMESPACE);

        room = "hall";
        create = false;
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(
            IQChildElementXmlStringBuilder xml) {
        xml.rightAngleBracket();
        xml.element("room", room);
        xml.element("create", Boolean.toString(create));
        return xml;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public boolean isCreate() {
        return create;
    }

    public void setCreate(boolean create) {
        this.create = create;
    }
}
