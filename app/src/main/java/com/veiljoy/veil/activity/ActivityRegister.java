package com.veiljoy.veil.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.veiljoy.veil.BaseActivity;

import com.veiljoy.veil.R;
import com.veiljoy.veil.bean.UserInfo;
import com.veiljoy.veil.im.IMUserBase;
import com.veiljoy.veil.imof.MUCJoinTask;
import com.veiljoy.veil.imof.UserAccessManager;
import com.veiljoy.veil.utils.Constants;
import com.veiljoy.veil.utils.PhotoUtils;
import com.veiljoy.veil.utils.SharePreferenceUtil;
import com.veiljoy.veil.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by zhongqihong on 15/3/31.
 */
public class ActivityRegister extends BaseActivity implements View.OnClickListener {


    private UserInfo mUserinfo = null;
    private IMUserBase.OnUserRegister mUserRegister;
    private Bitmap mAvatar;
    //生成动态数组，并且转入数据
    ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
    Integer[] icons = new Integer[]{
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
    EditText mETUserName;
    String mAccount;
    RadioGroup mRGGender;
    int mGender = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        initViews();
        initEvents();


    }

    private void init() {
        mUserinfo = new UserInfo();

        mUserRegister = new UserAccessManager(this);

    }

    private void initViews() {

        mHeaderLayout = (RelativeLayout) this.findViewById(R.id.activity_register_headerbar);

        mTVConfirm = (TextView) mHeaderLayout.findViewById(R.id.common_header_right_btn);

        mGridview = (GridView) findViewById(R.id.activity_register_grid_avatar);

        mETUserName = (EditText) findViewById(R.id.activity_register_et_name);

        mRGGender = (RadioGroup) findViewById(R.id.activity_register_radiogroup_gender);

        for (int i = 0; i < 12; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", icons[i]);//添加图像资源的ID
            map.put("ItemText", "NO." + String.valueOf(i));//按序号做ItemText
            lstImageItem.add(map);
        }

        //生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
        SimpleAdapter saImageItems = new SimpleAdapter(this, //没什么解释
                lstImageItem,//数据来源
                R.layout.icon_avatar_selection,//night_item的XML实现
                //动态数组与ImageItem对应的子项
                new String[]{"ItemImage"},

                //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[]{R.id.icon_avatar_image});
        mGridview.setAdapter(saImageItems);


    }

    private void initEvents() {
        mGridview.setOnItemClickListener(new ItemClickListener());
        mTVConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_header_right_btn:

                register();
                break;
        }
    }


    //当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件
    class ItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> arg0,//The AdapterView where the click happened
                                View arg1,//The view within the AdapterView that was clicked
                                int arg2,//The position of the view in the adapter
                                long arg3//The row id of the item that was clicked
        ) {
            //在本例中arg2=arg3
            HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);


            mAvatar = BitmapFactory.decodeResource(ActivityRegister.this.getResources(), icons[arg2]);


            Log.v("activityRegister", "mAvatar " + mAvatar == null ? "=null" : "!=null");
        }

    }

    class OnGenderCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.activity_register_gender_men:
                    mGender = 0;
                    break;
                case R.id.activity_register_gender_women:
                    mGender = 1;
                    break;
            }
        }
    }

    public void register() {

        if (validateAccount()) {


            SharePreferenceUtil.setName(mAccount);
            SharePreferenceUtil.setPasswd(Constants.USER_DEFAULT_PASSWORD);
            SharePreferenceUtil.setGender(mGender);

            RegisterTask registerTask = new RegisterTask();
            registerTask.execute(0);
        } else {

        }


    }


    class RegisterTask extends AsyncTask<Integer, Integer, Integer> {


        @Override
        protected void onPreExecute() {

            showCustomToast("正在注册...，账号："+SharePreferenceUtil.getName());

            mUserRegister.onPreRegister();

        }


        @Override
        protected Integer doInBackground(Integer... params) {



            if (mAvatar != null) {
                String fileName = PhotoUtils.savePhotoToSDCard(mAvatar, Constants.IMAGE_PATH, Bitmap.CompressFormat.PNG);
                if (fileName != null) {
                    SharePreferenceUtil.setAvatar(fileName);
                }

            }
            return mUserRegister.onRegister(SharePreferenceUtil.getName(), SharePreferenceUtil.getPasswd());


//            if(user==null)
//                return Constants.REGISTER_RESULT_ERROR;
//            else return mIMUserBase.login(user.getmName(),user.getmPassword());

        }

        @Override
        protected void onPostExecute(Integer code) {

            String status = "注册失败";
            switch (code) {
                case Constants.REGISTER_RESULT_SUCCESS: // 注册成功
                    status = "注册成功 ,账号为：" + SharePreferenceUtil.getName();
                    break;
                case Constants.REGISTER_RESULT_EXIST:// 已经存在该用户
                    status = "已经存在该用户";
                    break;
                case Constants.REGISTER_RESULT_FAIL:// 注册失败
                    status = "注册失败";
                    break;
                case Constants.SERVER_UNAVAILABLE:// 服务器没有返回结果
                    status = "服务器没有返回结果";
                    break;
            }
            showCustomToast(status);

            boolean rel = mUserRegister.onRegisterResult(code);

            if (rel) {
                showCustomToast("注册成功，账号："+SharePreferenceUtil.getName());
                new MUCJoinTask(null,ActivityRegister.this).execute("");
            }

        }
    }

    private boolean validateAccount() {
        mAccount = null;
        if (StringUtils.isNull(mETUserName)) {
            showCustomToast("请输入不见号码或登录邮箱");
            mETUserName.requestFocus();
            return false;
        }
        String account = mETUserName.getText().toString().trim();

        if (account.length() < 3) {
            showCustomToast("请输入大于3位的账号");
            return false;
        }

        if (StringUtils.matchEmail(account)) {
            mAccount = account;
            return true;
        }
        if (StringUtils.matchNumber(account)) {
            mAccount = account;
            return true;
        }
        if (StringUtils.matchAccount(account)) {
            mAccount = account;
            return true;
        }


        showCustomToast("账号格式不正确");
        mETUserName.requestFocus();
        return false;
    }


}
