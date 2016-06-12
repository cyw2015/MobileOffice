package com.cyw.mobileoffice.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cyw.mobileoffice.R;
import com.cyw.mobileoffice.entity.Contact;
import com.cyw.mobileoffice.entity.Document;
import com.cyw.mobileoffice.entity.ErrorMessage;
import com.cyw.mobileoffice.util.AppURL;
import com.cyw.mobileoffice.util.FileUtils;
import com.cyw.mobileoffice.view.AVLoadingIndicatorDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EditDocActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_bar_cancel;
    private TextView tv_bar_finish;
    private EditText et_doc_code;
    private EditText et_doc_title;
    private EditText et_doc_content;
    private TextView tv_doc_rec;
    private TextView tv_doc_attm;
    private List<Contact> mDatas;
    private AVLoadingIndicatorDialog myDialog;
    private String[] nItems;
    private boolean[] checkList;
    private ArrayList<Integer> multiChoiceID = new ArrayList<Integer>();
    private String recipients="";
    Document doc ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_doc);
        Intent intent= getIntent();
        doc = (Document) intent.getSerializableExtra("doc");
        initView();
        mDatas = new ArrayList<>();
        initData();
    }

    public void initView() {
        tv_bar_cancel = (TextView) findViewById(R.id.tv_bar_cancel);
        tv_bar_finish = (TextView) findViewById(R.id.tv_bar_finish);
        et_doc_code = (EditText) findViewById(R.id.et_doc_code);
        et_doc_title = (EditText) findViewById(R.id.et_doc_title);
        et_doc_content = (EditText) findViewById(R.id.et_doc_content);
        tv_doc_rec = (TextView) findViewById(R.id.tv_doc_rec);
        tv_doc_attm = (TextView) findViewById(R.id.tv_doc_attm);
        tv_bar_cancel.setOnClickListener(this);
        tv_bar_finish.setOnClickListener(this);
        et_doc_code.setText(doc.getDocCode());
        et_doc_title.setText(doc.getTitle());
        tv_doc_rec.setText(doc.getRecipients()+",");
        recipients = doc.getRecipientsCode()+",";
    }

    private void initData() {
        myDialog = new AVLoadingIndicatorDialog(this);
        myDialog.setMessage("Loading");
        myDialog.setCancelable(false);
        myDialog.show();
        RequestParams params1 =  new RequestParams(AppURL.GETDOCDETAIL);
        params1.addQueryStringParameter("docCode",doc.getDocCode());
        x.http().post(params1,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                if(!result.equals("")){
                    try {
                        JSONObject obj = new JSONObject(result);
                        doc = new Document();
                        doc.setContent(obj.getString("content"));
                        doc.setAttachment(obj.getString("attachment"));
                        doc.setAttachmentPath(obj.getString("attachmentPath"));
                        et_doc_content.setText(doc.getContent());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

        RequestParams params = new RequestParams(AppURL.GETCONTACTS);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                List<Contact> contacts = new ArrayList<>();
                try {
                    JSONArray arr = new JSONArray(result);
                    for (int i = 0; i < arr.length(); i++) {
                        Contact contact = new Contact();
                        contact.setCode(arr.getJSONObject(i).getString("emp_code"));
                        contact.setName(arr.getJSONObject(i).getString("emp_name"));//名称
                        contact.setSex(arr.getJSONObject(i).getString("emp_sex"));//性别
                        contact.setDeptCode(arr.getJSONObject(i).getString("dept_code"));
                        contact.setDepartment(arr.getJSONObject(i).getString("dept_name"));//部门
                        contact.setPostCode(arr.getJSONObject(i).getString("posi_code"));
                        contact.setPosition(arr.getJSONObject(i).getString("posi_name"));//岗位
                        contact.setBirthDay(arr.getJSONObject(i).getString("birthday"));//出生年月
                        contact.setEmail(arr.getJSONObject(i).getString("email"));//邮箱
                        contact.setAddress(arr.getJSONObject(i).getString("address"));//地址
                        contact.setPhone(arr.getJSONObject(i).getString("telphone"));//电话
                        contact.setUrl(arr.getJSONObject(i).getString("emp_image"));//url
                        contacts.add(contact);
                    }
                    mDatas.addAll(contacts);
                    nItems = new String[mDatas.size()];
                    checkList = new boolean[mDatas.size()];
                    for(int i = 0 ;i<mDatas.size();i++){
                        nItems[i]= mDatas.get(i).getCode()+" "+mDatas.get(i).getName()+" "+mDatas.get(i).getDepartment();
                        checkList[i]=false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    myDialog.cancel();
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

    public void getAttm(View v) {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    public void getRec(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditDocActivity.this);
        builder.setIcon(R.mipmap.info);
        builder.setTitle("多项选择");
        //  设置多选项
        builder.setMultiChoiceItems(nItems,
                checkList,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1, boolean arg2) {
                        if (arg2) {
                            multiChoiceID.add(arg1);
                        } else {
                            multiChoiceID.remove(arg1);
                        }
                    }
                });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                String str = "";
                recipients="";
                int size = multiChoiceID.size();
                for (int i = 0; i < size; i++) {
                    str+=mDatas.get(multiChoiceID.get(i)).getName()+",";
                    recipients+=mDatas.get(multiChoiceID.get(i)).getCode()+",";
                }
                tv_doc_rec.setText(str);
//                Toast toast = Toast.makeText(getApplicationContext(), "你选择了" + str, Toast.LENGTH_LONG);
//                toast.show();
            }
        });
        //  设置取消按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                multiChoiceID.clear();
                for (int i =0 ;i<checkList.length;i++)
                    checkList[i]=false;
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    String path = FileUtils.getPath(this, uri);
                    tv_doc_attm.setText(path);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_bar_cancel:
                finish();
                break;
            case R.id.tv_bar_finish:
                editDoc();
                break;
        }
    }
    public void editDoc(){

        String docCode =et_doc_code.getText().toString();

        String  docTitle = et_doc_title.getText().toString();
        if(docTitle.isEmpty()){
            Toast.makeText(EditDocActivity.this,"公文标题不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(recipients.isEmpty()){
            Toast.makeText(EditDocActivity.this,"收文人不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        String docContent = et_doc_content.getText().toString();
        if(docContent.isEmpty()){
            Toast.makeText(EditDocActivity.this,"公文内容不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        String attachment =  tv_doc_attm.getText().toString();
        myDialog = new AVLoadingIndicatorDialog(this);
        myDialog.setMessage("Loading");
        myDialog.setCancelable(false);
        myDialog.show();
        RequestParams params = new RequestParams(AppURL.UPDATEPUBDOC);
        params.addBodyParameter("docCode",docCode);
        params.addBodyParameter("docTitle",docTitle);
        params.addBodyParameter("recipients",recipients);
        params.addBodyParameter("type","android");
        params.addBodyParameter("docContent",docContent);
        if(!attachment.isEmpty())
            params.addBodyParameter("attachment", new File(attachment));
        params.addBodyParameter("oldAttachmentPath",doc.getAttachmentPath());
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                try {
                    ErrorMessage msg = gson.fromJson(result, new TypeToken<ErrorMessage>() {
                    }.getType());
                    int error = msg.getError();
                    if (error == 0) {
                        Toast.makeText(EditDocActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        myDialog.cancel();
                        finish();
                    } else {
                        Toast.makeText(EditDocActivity.this, msg.getErrorMsg(), Toast.LENGTH_SHORT).show();
                        myDialog.cancel();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(EditDocActivity.this, "修改出错", Toast.LENGTH_SHORT).show();
                    myDialog.cancel();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(EditDocActivity.this, "修改出错", Toast.LENGTH_SHORT).show();
                myDialog.cancel();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(EditDocActivity.this, "修改取消", Toast.LENGTH_SHORT).show();
                myDialog.cancel();
            }

            @Override
            public void onFinished() {
                myDialog.cancel();
            }
        });
    }
}
