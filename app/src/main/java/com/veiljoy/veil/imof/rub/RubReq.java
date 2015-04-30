package com.veiljoy.veil.imof.rub;

import org.jivesoftware.smack.packet.IQ;

/**
 * Created by Administrator on 2015/4/29.
 */
public class RubReq extends IQ {

    public static final String ELEMENT = QUERY_ELEMENT;
    public static final String NAMESPACE = "com.veil.rub";

    public RubReq() {
        super(ELEMENT, NAMESPACE);

        this.setType(IQ.Type.get);
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(
            IQChildElementXmlStringBuilder xml) {
        xml.rightAngleBracket();
        return xml;
    }
}
