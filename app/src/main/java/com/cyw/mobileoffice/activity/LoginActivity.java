package com.cyw.mobileoffice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cyw.mobileoffice.R;
import com.cyw.mobileoffice.util.AppURL;
import com.cyw.mobileoffice.util.SharedHelper;
import com.cyw.mobileoffice.view.AVLoadingIndicatorDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;


public class LoginActivity extends AppCompatActivity {

    private ImageView ivEmp;
    private EditText username;
    private EditText password;
    private CheckBox remember;
    private CheckBox autoLogin;
    private AVLoadingIndicatorDialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 取消标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        initView();
    }

    /**
     * 初始化绑定
     */
    private void initView() {
        ivEmp = (ImageView) findViewById(R.id.empImage);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        remember = (CheckBox) findViewById(R.id.remember);
        autoLogin = (CheckBox) findViewById(R.id.autoLogin);
        String userStr = (String) SharedHelper.get(this, "username", "");
        String passStr = (String) SharedHelper.get(this, "password", "");
        username.setText(userStr);
        password.setText(passStr);
        remember.setChecked( (boolean)SharedHelper.get(this, "remember", false));
        autoLogin.setChecked((boolean) SharedHelper.get(this, "autoLogin", false));
        String imgUrl = (String) SharedHelper.get(this,"currentUrl","");
        ImageOptions options = new ImageOptions.Builder()
                // 是否忽略GIF格式的图片
                .setIgnoreGif(false)
                .setSize(DensityUtil.dip2px(104),DensityUtil.dip2px(104))
                .setRadius(DensityUtil.dip2px(104))
                // 图片缩放模式
                .setCrop(true)
                // 下载中显示的图片
                .setLoadingDrawableId(R.mipmap.emp)
                // 下载失败显示的图片
                .setFailureDrawableId(R.mipmap.emp)
                // 得到ImageOptions对象
                .build();
        x.image().bind(ivEmp,imgUrl, options);
    }

    /**
     * 点击登录
     *
     * @param view
     */
    public void loginSys(View view) {
        String code = username.getText().toString();
        String pass = password.getText().toString();
        if (code.trim().equals("")) {
            Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
        } else if (pass.equals("")) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
        } else {
            if (remember.isChecked()) {
                SharedHelper.put(this, "username", code);
                SharedHelper.put(this, "password", pass);
                SharedHelper.put(this, "remember", remember.isChecked());
                SharedHelper.put(this, "autoLogin", autoLogin.isChecked());
            } else {
                SharedHelper.remove(this, "username");
                SharedHelper.remove(this, "password");
                SharedHelper.remove(this, "remember");
                SharedHelper.remove(this, "autoLogin");
            }
            myDialog=new AVLoadingIndicatorDialog(this);
            myDialog.setMessage("Loading");
            myDialog.setCancelable(false);
            myDialog.show();
            doLogin(code,pass);
        }
    }

    public void doLogin(final String username, String password) {
        RequestParams params =  new RequestParams(AppURL.LOGINURL);
        params.addQueryStringParameter("username",username);
        params.addQueryStringParameter("password",password);
        params.addQueryStringParameter("f","android");
        x.http().post(params,new Callback.CommonCallback<String>(){
            @Override
            public void onSuccess(String result) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(result);
                    String error= obj.getString("error");
                    if(error!=null&&error.equals("0")){
                        SharedHelper.put(x.app(),"currentUser",username);
                        Intent it = new Intent(LoginActivity.this,OfficeActivity.class);
                        startActivity(it);
                        finish();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(LoginActivity.this, "登录失败，请检查用户名和密码", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                myDialog.cancel();
                Toast.makeText(LoginActivity.this,"登录失败,请检查网络",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(LoginActivity.this,"登录取消",Toast.LENGTH_SHORT).show();
                myDialog.cancel();
            }
            @Override
            public void onFinished() {
//                Toast.makeText(LoginActivity.this,"完成",Toast.LENGTH_SHORT).show();
                myDialog.cancel();
            }
        });
    }
    /**
     * 复选框勾选
     *
     * @param v
     */
    public void onCheckView(View v) {
        switch (v.getId()) {
            case R.id.autoLogin:
                if (autoLogin.isChecked()) {
                    remember.setChecked(true);
                }
                break;
            case R.id.remember:
                if (!remember.isChecked()) {
                    autoLogin.setChecked(false);
                }
                break;
        }
    }
}
