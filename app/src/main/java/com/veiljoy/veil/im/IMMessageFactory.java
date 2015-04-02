package com.veiljoy.veil.im;

import android.content.Context;

import com.veiljoy.veil.imImpl.IMMessageVoice;
import com.veiljoy.veil.imImpl.IMMessageSimpleImage;

/**
 * Created by zhongqihong on 15/4/2.
 */
public class IMMessageFactory {

    public static IMMessageFactory instance=null;



    private IMMessageFactory(){

    }

    public static  IMMessageFactory getInstance(){

        if(instance==null){
            synchronized (IMMessageFactory.class)
            {
                if(instance==null)
                    instance= new IMMessageFactory();
            }
        }
        return  instance;
    }


    public IMMessageItem getMessageItem(IMMessage msg,Context context){

        IMMessageItem message=null;
        String uri=msg.getmUri();


        switch(IMMessage.Scheme.ofUri(uri)){
            case MAP:
            case FILE:
                break;
            case IMAGE:
                message=new IMMessageSimpleImage(msg,context);
                break;
            case VOICE:
               message=new IMMessageVoice(msg,context);
                break;
            case UNKNOWN: throw  new RuntimeException("Unknown type of uri: "+uri);
        }


        message.init(msg.getmMessageType());



        return message;

    }


}
