package com.cyw.mobileoffice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cyw.mobileoffice.R;
import com.cyw.mobileoffice.entity.ErrorMessage;
import com.cyw.mobileoffice.util.AppURL;
import com.cyw.mobileoffice.util.SharedHelper;
import com.cyw.mobileoffice.view.AVLoadingIndicatorDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class UpdatePwdActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_bar_cancel;
    private TextView tv_bar_finish;
    private EditText et_pass_old;
    private EditText et_pass_new;
    private EditText et_pass_new2;
    private AVLoadingIndicatorDialog myDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);
        initView();
    }

    public void initView() {
        tv_bar_cancel = (TextView) findViewById(R.id.tv_bar_cancel);
        tv_bar_finish = (TextView) findViewById(R.id.tv_bar_finish);
        et_pass_old = (EditText) findViewById(R.id.et_pass_old);
        et_pass_new = (EditText) findViewById(R.id.et_pass_new);
        et_pass_new2 = (EditText) findViewById(R.id.et_pass_new2);
        tv_bar_finish.setOnClickListener(this);
        tv_bar_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_bar_cancel:
                finish();
                break;
            case R.id.tv_bar_finish:
                updatePassword();
                break;
        }
    }
    public void updatePassword(){
        String oldPass = et_pass_old.getText().toString();
        String newPass = et_pass_new.getText().toString();
        String newPass2 = et_pass_new2.getText().toString();
        if (oldPass.trim().equals("")){
            Toast.makeText(UpdatePwdActivity.this,"请输入原始密码",Toast.LENGTH_SHORT).show();
        } else if(newPass.trim().equals("")){
            Toast.makeText(UpdatePwdActivity.this,"请输入新密码，不要输入空格",Toast.LENGTH_SHORT).show();
        } else if (!newPass.equals(newPass2)){
            Toast.makeText(UpdatePwdActivity.this,"两次输入的新密码不一致",Toast.LENGTH_SHORT).show();
        }else{
            myDialog = new AVLoadingIndicatorDialog(this);
            myDialog.setMessage("Loading");
            myDialog.setCancelable(false);
            myDialog.show();
            RequestParams params = new RequestParams(AppURL.UPDATEPASSWORD);
            params.addQueryStringParameter("oldPass",oldPass);
            params.addQueryStringParameter("newPass", newPass);
            x.http().post(params, new Callback.CommonCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    try{
                        ErrorMessage msg = gson.fromJson(result, new TypeToken<ErrorMessage>() {}.getType());
                        int error = msg.getError();
                        if (error == 0) {
                            Toast.makeText(UpdatePwdActivity.this, "修改成功,请重新登录", Toast.LENGTH_SHORT).show();
                            SharedHelper.remove(UpdatePwdActivity.this,"autoLogin");
                            SharedHelper.remove(UpdatePwdActivity.this,"currentUser");
                            Intent it = new Intent(UpdatePwdActivity.this, LoginActivity.class);
                            startActivity(it);
                            UpdatePwdActivity.this.finish();
                        } else {
                            Toast.makeText(UpdatePwdActivity.this, msg.getErrorMsg(), Toast.LENGTH_SHORT).show();
                        }
                        myDialog.cancel();
                    }catch (Exception e){
                        e.printStackTrace();
                        myDialog.cancel();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Toast.makeText(UpdatePwdActivity.this, "修改密码失败", Toast.LENGTH_SHORT).show();
                    myDialog.cancel();
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    Toast.makeText(UpdatePwdActivity.this, "修改密码取消", Toast.LENGTH_SHORT).show();
                    myDialog.cancel();
                }

                @Override
                public void onFinished() {
                    myDialog.cancel();
                }
            });
        }
    }
}
