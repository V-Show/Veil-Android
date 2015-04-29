package com.veiljoy.veil.init;

import android.os.AsyncTask;

import com.veiljoy.veil.imof.MUCHelper;
import com.veiljoy.veil.xmpp.base.XmppConnectionManager;

import org.jivesoftware.smack.XMPPConnection;

/**
 * Created by zhongqihong on 15/4/16.
 */
public class InitializationTask  extends AsyncTask<Void,Integer,Integer> {


    private final int INIT_CODE_FAIL=-1;
    private final int INIT_CODE_SUCCESS=0;
    private final int INIT_CODE_NETWORK_FAIL=1;
    private final int INIT_CODE_XMPP_FAIL=2;
    private final int INIT_CODE_SERVER_FAIL=3;
    private final int INIT_CODE_APPSTATE_FAIL=4;
    private final int INIT_CODE_ACCOUNT_FAIL=5;

    InitializationListener listener;

    public InitializationTask(InitializationListener l){
        this.listener=l;
    }


    public static interface   InitializationListener{

        void onResult(int code);
        int inBackground(int code);
    }



    private boolean initNetwork(){

        return true;

    }

    private boolean initServer(){
        return true;
    }

    private boolean initXmpp(){

        final XMPPConnection connection = XmppConnectionManager.getInstance().getConnection();
        MUCHelper.init(connection);

        return true;
    }

    private boolean initAppState(){

        return true;
    }

    private boolean initAccount(){
        return true;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... params) {

        publishProgress(10);
        if(!initNetwork()){

            return INIT_CODE_NETWORK_FAIL;
        }
        publishProgress(20);

        if(!initAppState()){
            return INIT_CODE_APPSTATE_FAIL;
        }
        publishProgress(40);
        if(!initXmpp()){
            return INIT_CODE_XMPP_FAIL;
        }
        publishProgress(60);
        if(!initServer()){
            return INIT_CODE_SERVER_FAIL;
        }
        publishProgress(80);
        if(!initAccount()){
            return INIT_CODE_ACCOUNT_FAIL;
        }
        publishProgress(100);


        return listener.inBackground(INIT_CODE_SUCCESS);


    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Integer code) {
        super.onPostExecute(code);

        listener.onResult(code);
    }

}
