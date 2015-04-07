package com.veiljoy.veil.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.INotificationSideChannel;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.veiljoy.veil.BaseActivity;

import com.veiljoy.veil.R;
import com.veiljoy.veil.bean.AvatarInfo;
import com.veiljoy.veil.bean.UserInfo;
import com.veiljoy.veil.im.IMUserBase;
import com.veiljoy.veil.utils.Constants;
import com.veiljoy.veil.utils.PhotoUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zhongqihong on 15/3/31.
 */
public class ActivityRegister extends BaseActivity implements View.OnClickListener{


    private IMUserBase mIMUserBase;
    private UserInfo mUserinfo;

    private Bitmap mAvatar;
    //生成动态数组，并且转入数据
    ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
    Integer[] icons= new Integer[]{
         R.mipmap.ic_chat_avatar_0,
            R.mipmap.ic_chat_avatar_1,
            R.mipmap.ic_chat_avatar_2,
            R.mipmap.ic_chat_avatar_3,
            R.mipmap.ic_chat_avatar_4,
            R.mipmap.ic_chat_avatar_5,
            R.mipmap.ic_chat_avatar_6,
            R.mipmap.ic_chat_avatar_7,
            R.mipmap.ic_chat_avatar_8,
            R.mipmap.ic_chat_avatar_9,
            R.mipmap.ic_chat_avatar_10,
            R.mipmap.ic_chat_avatar_11,
    };
    GridView mGridview;
    TextView mTVConfirm;
    RelativeLayout mHeaderLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        initViews();
        initEvents();


    }

    private void init(){
        mUserinfo=new UserInfo();

    }

    private void initViews(){

        mHeaderLayout=(RelativeLayout)this.findViewById(R.id.activity_register_headerbar);

        mTVConfirm= (TextView)mHeaderLayout.findViewById(R.id.common_header_right_btn);

        mGridview = (GridView) findViewById(R.id.activity_register_grid_avatar);

        for(int i=0;i<12;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", icons[i]);//添加图像资源的ID
            map.put("ItemText", "NO."+String.valueOf(i));//按序号做ItemText
            lstImageItem.add(map);
        }

        //生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
        SimpleAdapter saImageItems = new SimpleAdapter(this, //没什么解释
                lstImageItem,//数据来源
                R.layout.icon_avatar_selection,//night_item的XML实现
                //动态数组与ImageItem对应的子项
                new String[] {"ItemImage"},

                //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[] {R.id.icon_avatar_image});
        mGridview.setAdapter(saImageItems);


    }

    private void initEvents(){
        mGridview.setOnItemClickListener(new ItemClickListener());
        mTVConfirm.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.common_header_right_btn:
                //startActivity(ActivityChat.class,null);

                register();
                break;
        }
    }


    //当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件
    class  ItemClickListener implements AdapterView.OnItemClickListener
    {
        public void onItemClick(AdapterView<?> arg0,//The AdapterView where the click happened
                                View arg1,//The view within the AdapterView that was clicked
                                int arg2,//The position of the view in the adapter
                                long arg3//The row id of the item that was clicked
        ) {
            //在本例中arg2=arg3
            HashMap<String, Object> item=(HashMap<String, Object>) arg0.getItemAtPosition(arg2);


            mAvatar= BitmapFactory.decodeResource(ActivityRegister.this.getResources(), icons[arg2]);



            Log.v("activityRegister","mAvatar "+mAvatar==null?"=null":"!=null");
        }

    }

    public void register(){

       mUserinfo.setmName("");
        mUserinfo.setmPassword("");
        mUserinfo.setmGender(0);
        mUserinfo.setmAvatar("001");


       RegisterTask registerTask = new RegisterTask();
       registerTask.execute(mUserinfo);

    }


    class RegisterTask extends AsyncTask<UserInfo,Integer,Integer>{




        @Override
        protected void onPreExecute() {

            Toast.makeText(ActivityRegister.this,"signing up...",Toast.LENGTH_LONG);

        }


        @Override
        protected Integer doInBackground(UserInfo... params) {

            UserInfo user=params[0];

            if(mAvatar!=null)
               mUserinfo.setmAvatar(PhotoUtils.savePhotoToSDCard(mAvatar,Constants.IMAGE_PATH,Bitmap.CompressFormat.PNG));

//            if(user==null)
//                return Constants.REGISTER_RESULT_ERROR;
//            else return mIMUserBase.login(user.getmName(),user.getmPassword());
            return Constants.REGISTER_RESULT_OK;
        }

        @Override
        protected void onPostExecute(Integer result){
            switch(result){
                case Constants.REGISTER_RESULT_OK:
                    Bundle bundle =new Bundle();
                    bundle.putString(Constants.USER_AVATAR_FILE_PATH_KEY,mUserinfo.getmAvatar());
                    startActivity(ActivityChat.class,bundle);
                    break;
                case Constants.REGISTER_RESULT_ERROR:
                    break;
                case Constants.REGISTER_RESULT_USER_EXIST:
                    break;

            }

            Toast.makeText(ActivityRegister.this,"result code:"+result,Toast.LENGTH_LONG);
        }
    }

}
