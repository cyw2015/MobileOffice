package com.cyw.mobileoffice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cyw.mobileoffice.R;
import com.cyw.mobileoffice.adapter.TabFragmentPagerAdapter;
import com.cyw.mobileoffice.entity.ErrorMessage;
import com.cyw.mobileoffice.util.AppURL;
import com.cyw.mobileoffice.util.SharedHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;


public class OfficeActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    public static final int PAGE_ONE=0;
    public static final int PAGE_TWO=1;
    public static final int PAGE_THREE=2;
    public static final int PAGE_FOUR=3;

    private TextView txt_topbar;
    private RadioGroup rg_tab_bar;
    private RadioButton rb_contacts;
    private RadioButton rb_message;
    private RadioButton rb_facility;
    private RadioButton rb_setting;
    private ViewPager vpager;
    private TabFragmentPagerAdapter tAdapter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office);
        tAdapter =new TabFragmentPagerAdapter(getSupportFragmentManager());
        bindViews();
        rb_contacts.setChecked(true);
        //获取用户权限资源信息
        getResCode();
    }

    //获取权限信息
    private void getResCode(){
        String user = (String) SharedHelper.get(OfficeActivity.this,"currentUser","");
        if(user!=null){
            RequestParams params =  new RequestParams(AppURL.GETRESCODE);
            params.addQueryStringParameter("userCode",user);
            x.http().post(params,new Callback.CommonCallback<String>(){

                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    try {
                        ErrorMessage msg = gson.fromJson(result, new TypeToken<ErrorMessage>() {
                        }.getType());
                        int error = msg.getError();
                        if (error == 0) {
                            SharedHelper.put(OfficeActivity.this,"myResCodes",msg.getErrorMsg());
//                          Toast.makeText(OfficeActivity.this,msg.getErrorMsg(),Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(OfficeActivity.this,msg.getErrorMsg(),Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        }
    }

    /**
     * 绑定视图
     */
    private void   bindViews(){
        txt_topbar = (TextView) findViewById(R.id.txt_topbar);
        rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rb_contacts = (RadioButton) findViewById(R.id.rb_contacts);
        rb_message = (RadioButton) findViewById(R.id.rb_message);
        rb_facility = (RadioButton) findViewById(R.id.rb_facility);
        rb_setting = (RadioButton) findViewById(R.id.rb_setting);
        rg_tab_bar.setOnCheckedChangeListener(this);
        vpager = (ViewPager)findViewById(R.id.vpager);
        vpager.setAdapter(tAdapter);
        vpager.setCurrentItem(0);
        vpager.addOnPageChangeListener(this);
    }

    /**
     * 底部导航栏勾选
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_contacts:
                txt_topbar.setText("通讯录");
                vpager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.rb_message:
                txt_topbar.setText("公文列表");
                vpager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.rb_facility:
                txt_topbar.setText("功能");
                vpager.setCurrentItem(PAGE_THREE);
                break;
            case R.id.rb_setting:
                txt_topbar.setText("设置");
                vpager.setCurrentItem(PAGE_FOUR);
                break;
        }
    }

    //重写ViewPager页面切换
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if(state==2){
            switch (vpager.getCurrentItem()){
                case PAGE_ONE:
                    rb_contacts.setChecked(true);
                    break;
                case PAGE_TWO:
                    rb_message.setChecked(true);
                    break;
                case PAGE_THREE:
                    rb_facility.setChecked(true);
                    break;
                case PAGE_FOUR:
                    rb_setting.setChecked(true);
                    break;
            }
        }
    }
    public void logout(View v){
        //清除当前用户
        //清除Autologin
        SharedHelper.remove(this,"autoLogin");
        SharedHelper.remove(this,"currentUser");
        Intent it = new Intent(this, LoginActivity.class);
        startActivity(it);
        finish();
    }

    public void personSetting(View v){
        Intent intent =  new Intent(OfficeActivity.this,SettingActivity.class);
        startActivity(intent);
//        Toast.makeText(OfficeActivity.this,"设置",Toast.LENGTH_SHORT).show();
    }
    public void updatePassword(View v){
        Intent intent  = new Intent(OfficeActivity.this,UpdatePwdActivity.class);
        startActivity(intent);
    }

}
