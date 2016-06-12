package com.cyw.mobileoffice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cyw.mobileoffice.R;
import com.cyw.mobileoffice.entity.Document;
import com.cyw.mobileoffice.entity.ErrorMessage;
import com.cyw.mobileoffice.util.AppURL;
import com.cyw.mobileoffice.view.AVLoadingIndicatorDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class ApproveAddActivity extends AppCompatActivity {
    private TextView tv_code;
    private TextView tv_title;
    private RadioGroup rg_appr_state;
    private EditText et_appr_advice;
    private AVLoadingIndicatorDialog myDialog;
    Document doc ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_add);
        Intent intent = getIntent();
        doc = (Document) intent.getSerializableExtra("doc");
        initView();
    }
    private  void initView(){
        tv_code  = (TextView) findViewById(R.id.tv_code);
        tv_title = (TextView) findViewById(R.id.tv_title);
        rg_appr_state  = (RadioGroup) findViewById(R.id.rg_appr_state);
        et_appr_advice = (EditText) findViewById(R.id.et_appr_advice);
        tv_code.setText(doc.getDocCode());
        tv_title.setText(doc.getTitle());
    }

    public void submitAppr(View v){
        String apprAdvice = et_appr_advice.getText().toString();
        String isPass = "1";
        switch (rg_appr_state.getCheckedRadioButtonId()){
            case R.id.rb_appr_ok:
                isPass="1";
                break;
            case R.id.rb_appr_no:
                isPass="0";
                break;
        }
        myDialog = new AVLoadingIndicatorDialog(this);
        myDialog.setMessage("Loading");
        myDialog.setCancelable(false);
        myDialog.show();
        RequestParams params = new RequestParams(AppURL.UPDATEDOCAPPR);
        params.addQueryStringParameter("docCode", doc.getDocCode());
        params.addQueryStringParameter("isPass", isPass);
        params.addQueryStringParameter("apprAdvice", apprAdvice);
        x.http().post(params,  new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                try{
                    ErrorMessage msg = gson.fromJson(result, new TypeToken<ErrorMessage>() {}.getType());
                    int error = msg.getError();
                    if (error == 0) {
                        Toast.makeText(ApproveAddActivity.this, "审批成功", Toast.LENGTH_SHORT).show();
                        ApproveAddActivity.this.finish();
                    } else {
                        Toast.makeText(ApproveAddActivity.this, msg.getErrorMsg(), Toast.LENGTH_SHORT).show();
                    }
                    myDialog.cancel();
                }catch (Exception e){
                    e.printStackTrace();
                    myDialog.cancel();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(ApproveAddActivity.this,"审批出错",Toast.LENGTH_SHORT).show();
                myDialog.cancel();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(ApproveAddActivity.this,"审批取消",Toast.LENGTH_SHORT).show();
                myDialog.cancel();
            }

            @Override
            public void onFinished() {
                myDialog.cancel();
            }
        });
//        Toast.makeText(ApproveAddActivity.this,"提交",Toast.LENGTH_SHORT).show();
    }
}
