package com.cyw.mobileoffice.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cyw.mobileoffice.R;
import com.cyw.mobileoffice.entity.ErrorMessage;
import com.cyw.mobileoffice.util.AppURL;
import com.cyw.mobileoffice.util.SharedHelper;
import com.cyw.mobileoffice.view.AVLoadingIndicatorDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Calendar;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private AVLoadingIndicatorDialog myDialog;
    private TextView tv_bar_cancel;
    private TextView tv_bar_finish;
    private TextView tv_code;
    private EditText et_name;
    private TextView tv_dept;
    private TextView tv_posi;
    private RadioGroup rg_sex;
    private EditText et_phone;
    private EditText et_email;
    private TextView tv_birth;
    private EditText et_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        String user = (String) SharedHelper.get(this, "currentUser", "");
        initDate(user);
    }

    private void initView() {
        tv_code = (TextView) findViewById(R.id.tv_code);
        et_name = (EditText) findViewById(R.id.et_name);
        tv_dept = (TextView) findViewById(R.id.tv_dept);
        tv_posi = (TextView) findViewById(R.id.tv_posi);
        rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_email = (EditText) findViewById(R.id.et_email);
        tv_birth = (TextView) findViewById(R.id.tv_birth);
        et_address = (EditText) findViewById(R.id.et_address);
        tv_bar_cancel = (TextView) findViewById(R.id.tv_bar_cancel);
        tv_bar_finish = (TextView) findViewById(R.id.tv_bar_finish);
        tv_birth.setOnClickListener(this);
        tv_bar_cancel.setOnClickListener(this);
        tv_bar_finish.setOnClickListener(this);
    }

    private void initDate(String user) {
        myDialog = new AVLoadingIndicatorDialog(this);
        myDialog.setMessage("Loading");
        myDialog.setCancelable(false);
        myDialog.show();
        RequestParams params = new RequestParams(AppURL.GETPERSONDETAIL);
        params.addQueryStringParameter("empCode", user);
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                if (result != null && !result.equals("")) {
                    try {
                        JSONObject obj = new JSONObject(result);
                        if (obj.has("error")) {
                            Toast.makeText(SettingActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                        } else {
                            tv_code.setText(obj.getString("emp_code"));
                            et_name.setText(obj.getString("emp_name"));
                            tv_dept.setText(obj.getString("dept_name"));
                            tv_posi.setText(obj.getString("posi_name"));
//                            et_sex.setText(obj.getString("emp_sex"));
                            if (obj.getString("emp_sex").equals("1")) {
                                rg_sex.check(R.id.rb_boy);
                            } else {
                                rg_sex.check(R.id.rb_girl);
                            }
                            et_phone.setText(obj.getString("telphone"));
                            et_email.setText(obj.getString("email"));
                            tv_birth.setText(obj.getString("birthday"));
                            et_address.setText(obj.getString("address"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        myDialog.cancel();
                    }
                } else {
                    Toast.makeText(SettingActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                }
                myDialog.cancel();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                myDialog.cancel();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                myDialog.cancel();
            }

            @Override
            public void onFinished() {
                myDialog.cancel();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_birth:
                Calendar c = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(SettingActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker dp, int year, int mounth, int day) {
                                tv_birth.setText(year + "-" + (mounth + 1) + "-" + day);
                            }
                        },
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH));
                dpd.show();
                break;
            case R.id.tv_bar_cancel:
                finish();
                break;
            case R.id.tv_bar_finish:
                updatePerson();
                break;
        }
    }

    public void updatePerson() {
        String code = tv_code.getText().toString();
        String sex = "1";
        if (rg_sex.getCheckedRadioButtonId() == R.id.rb_boy) {
            sex = "1";
        } else {
            sex = "2";
        }
        String phone = et_phone.getText().toString();
        String email = et_email.getText().toString();
        String birth = tv_birth.getText().toString();
        String address = et_address.getText().toString();
        String name = et_name.getText().toString();
        if (name.trim().equals("")) {
            Toast.makeText(SettingActivity.this, "请输入正确的姓名", Toast.LENGTH_SHORT).show();
        }
        myDialog = new AVLoadingIndicatorDialog(this);
        myDialog.setMessage("Loading");
        myDialog.setCancelable(false);
        myDialog.show();
        RequestParams params = new RequestParams(AppURL.UPDATEPERSON);
        params.addQueryStringParameter("empCode", code);
        params.addQueryStringParameter("empName", name);
        params.addQueryStringParameter("empSex", sex);
        params.addQueryStringParameter("telphone", phone);
        params.addQueryStringParameter("birthday", birth);
        params.addQueryStringParameter("email", email);
        params.addQueryStringParameter("address", address);
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
//                Toast.makeText(SettingActivity.this,result,Toast.LENGTH_SHORT).show();
                Gson gson = new Gson();
                try {
                    ErrorMessage msg = gson.fromJson(result, new TypeToken<ErrorMessage>() {
                    }.getType());
                    int error = msg.getError();
                    if (error == 0) {
                        Toast.makeText(SettingActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                        myDialog.cancel();
                        finish();
                    }else{
                        Toast.makeText(SettingActivity.this,msg.getErrorMsg(),Toast.LENGTH_SHORT).show();
                        myDialog.cancel();
                    }
                } catch (Exception e) {
                    myDialog.cancel();
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(SettingActivity.this,"修改出错",Toast.LENGTH_SHORT).show();
                myDialog.cancel();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(SettingActivity.this,"修改取消",Toast.LENGTH_SHORT).show();
                myDialog.cancel();
            }

            @Override
            public void onFinished() {
                myDialog.cancel();
            }
        });
    }
}
