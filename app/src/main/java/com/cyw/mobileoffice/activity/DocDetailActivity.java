package com.cyw.mobileoffice.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cyw.mobileoffice.R;
import com.cyw.mobileoffice.entity.Document;
import com.cyw.mobileoffice.util.AppURL;
import com.cyw.mobileoffice.view.AVLoadingIndicatorDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

public class DocDetailActivity extends AppCompatActivity {
    private static final String BASE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator;
    private TextView tv_detail_title;//标题
    private TextView tv_detail_date;//发文时间
    private TextView tv_creater;//发文人
    private TextView tv_rec;//收文人
    private TextView tv_att;//附件
    private TextView tv_content;//内容
    private Document doc;
    private AVLoadingIndicatorDialog myDialog;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_detail);
        Intent it = getIntent();
        String docCode = it.getStringExtra("docCode");
        initView();
        myDialog=new AVLoadingIndicatorDialog(this);
        myDialog.setMessage("Loading");
        myDialog.setCancelable(false);
        myDialog.show();
        initDate(docCode);
        tv_att.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(doc.getAttachmentPath().equals(""))return;
                progressDialog = new ProgressDialog(DocDetailActivity.this);
                RequestParams params=new RequestParams(AppURL.GETDOCUMENTURL);
                //设置断点续传
                params.setSaveFilePath(BASE_PATH+doc.getAttachment());
                params.setAutoResume(false);
                params.addQueryStringParameter("attachment",doc.getAttachment());
                params.addQueryStringParameter("attachmentPath",doc.getAttachmentPath());
                x.http().get(params,new Callback.ProgressCallback<File>(){

                    @Override
                    public void onSuccess(File result) {
                        Toast.makeText(DocDetailActivity.this, "下载成功"+BASE_PATH+doc.getAttachment(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        ex.printStackTrace();
                        Toast.makeText(DocDetailActivity.this, "下载失败，请检查网络和SD卡", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }

                    @Override
                    public void onWaiting() {

                    }

                    @Override
                    public void onStarted() {

                    }

                    @Override
                    public void onLoading(long total, long current, boolean isDownloading) {
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setMessage("亲，努力下载中。。。");
                        progressDialog.show();
                        progressDialog.setMax((int) total);
                        progressDialog.setProgress((int) current);
                    }
                });
            }
        });
    }
    private void initView(){
        tv_detail_title = (TextView) findViewById(R.id.tv_detail_title);
        tv_detail_date = (TextView)findViewById(R.id.tv_detail_date);
        tv_creater =(TextView)findViewById(R.id.tv_creater);
        tv_rec = (TextView)findViewById(R.id.tv_rec);
        tv_att = (TextView)findViewById(R.id.tv_att);
        tv_content=(TextView)findViewById(R.id.tv_content);
    }
    private void initDate(String docCode){
        RequestParams params =  new RequestParams(AppURL.GETDOCDETAIL);
        params.addQueryStringParameter("docCode",docCode);
        x.http().post(params,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                if(!result.equals("")){
                    try {
                        JSONObject obj = new JSONObject(result);
                        doc = new Document();
                        doc.setTitle(obj.getString("doc_title"));
                        doc.setCreaterName(obj.getString("creater_name"));
                        doc.setDate(obj.getString("pubTime"));
                        doc.setRecipientsCode(obj.getString("recipientsCode"));
                        doc.setRecipients(obj.getString("recipients"));
                        doc.setContent(obj.getString("content"));
                        doc.setAttachment(obj.getString("attachment"));
                        doc.setAttachmentPath(obj.getString("attachmentPath"));
                        tv_detail_title.setText(doc.getTitle());
                        tv_detail_date.setText(doc.getDate());
                        tv_creater.setText(doc.getCreaterName());
                        tv_rec.setText(doc.getRecipients());
                        tv_att.setText(doc.getAttachment());
                        tv_content.setText(doc.getContent());
                        myDialog.cancel();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        myDialog.cancel();
                    }
                }
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

}
