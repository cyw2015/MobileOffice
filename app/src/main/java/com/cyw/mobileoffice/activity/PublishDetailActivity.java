package com.cyw.mobileoffice.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublishDetailActivity extends AppCompatActivity {
    private static final String BASE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator;
    private TextView tv_detail_title;//标题
    private TextView tv_detail_date;//发文时间
    private TextView tv_creater;//发文人
    private TextView tv_rec;//收文人
    private TextView tv_att;//附件
    private TextView tv_content;//内容
    private ListView lv_appr;//审批
    private Document doc;
    private AVLoadingIndicatorDialog myDialog;
    private ProgressDialog progressDialog;
    private String[] texts = new String[]{"审批状态:","审批时间:","审批人:","审批意见:"};
    private String[] apprs = new String[4];
    List<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDialog=new AVLoadingIndicatorDialog(this);
        myDialog.setMessage("Loading");
        myDialog.setCancelable(false);
        myDialog.show();
        setContentView(R.layout.activity_publish_detail);
        initView();
        Intent it = getIntent();
        doc = (Document) it.getSerializableExtra("doc");
        tv_detail_title.setText(doc.getTitle());//标题
        tv_detail_date.setText(doc.getEditTime());//创建日期
        tv_rec.setText(doc.getRecipients());//收件人  //可能没有
        doc.getState();
        initView();
        initDate(doc.getDocCode());
    }

    private void initView(){
        tv_detail_title = (TextView) findViewById(R.id.tv_detail_title);//标题
        tv_detail_date = (TextView)findViewById(R.id.tv_detail_date);//创建日期
        tv_creater =(TextView)findViewById(R.id.tv_creater);//创建人
        tv_rec = (TextView)findViewById(R.id.tv_rec);//收件人
        tv_att = (TextView)findViewById(R.id.tv_att);//附件
        tv_content=(TextView)findViewById(R.id.tv_content);//内容
        lv_appr = (ListView) findViewById(R.id.lv_appr);//审批
        tv_att.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(doc.getAttachmentPath().equals(""))return;
                progressDialog = new ProgressDialog(PublishDetailActivity.this);
                RequestParams params=new RequestParams(AppURL.GETDOCUMENTURL);
                //设置断点续传
                params.setSaveFilePath(BASE_PATH+doc.getAttachment());
                params.setAutoResume(false);
                params.addQueryStringParameter("attachment",doc.getAttachment());
                params.addQueryStringParameter("attachmentPath",doc.getAttachmentPath());
                x.http().get(params,new Callback.ProgressCallback<File>(){

                    @Override
                    public void onSuccess(File result) {
                        Toast.makeText(PublishDetailActivity.this, "下载成功"+BASE_PATH+doc.getAttachment(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        ex.printStackTrace();
                        Toast.makeText(PublishDetailActivity.this, "下载失败，请检查网络和SD卡", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFinished() {
                        progressDialog.dismiss();
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

    private void initDate(String docCode){
        String state = doc.getState();
        String str = null;
        if(state!=null&&!state.equals("")){
            if(state.equals("0")){
                str= "未送审";
            }else if(state.equals("1")){
                str= "待审批";
            }else if(state.equals("2")){
                str= "审批通过";
            }else if(state.equals("-1")){
                str= "审批未通过";
            }
            apprs[0]=str;
        }
        RequestParams params =  new RequestParams(AppURL.GETDOCDETAILDATA);
        params.addQueryStringParameter("docCode", docCode);
        x.http().post(params,new Callback.CommonCallback<String>(){
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    doc.setCreaterName(obj.getString("createrName"));
                    tv_creater.setText(doc.getCreaterName());
                    doc.setContent(obj.getString("content"));
                    tv_content.setText(doc.getContent());
                    if(obj.has("attachment")){
                        doc.setAttachment(obj.getString("attachment"));
                        tv_att.setText(doc.getAttachment());
                    }
                    if(obj.has("attachmentPath")){
                        doc.setAttachmentPath(obj.getString("attachmentPath"));
                    }

                    if(obj.has("apprDate")){
                        doc.setApprDate(obj.getString("apprDate"));
                        apprs[1]=doc.getApprDate();
                    }
                    if(obj.has("apprName")){
                        doc.setApprName(obj.getString("apprName"));
                        apprs[2]=doc.getApprName();
                    }
                    if(obj.has("apprAdvice")){
                        doc.setApprAdvice(obj.getString("apprAdvice"));
                        apprs[3]=doc.getApprAdvice();
                    }
                    if(obj.has("recipients")){
                        doc.setRecipients(obj.getString("recipients"));
                        tv_rec.setText(doc.getRecipients());
                    }
                    if(obj.has("recipientsCode")){
                        doc.setRecipientsCode(obj.getString("recipientsCode"));
                    }
                    for (int i = 0; i < texts.length; i++) {
                        Map<String, Object> showitem = new HashMap<String, Object>();
                        showitem.put("text", texts[i]);
                        showitem.put("detail", apprs[i]);
                        listitem.add(showitem);
                    }
                    SimpleAdapter myAdapter = new SimpleAdapter(getApplicationContext(), listitem,
                            R.layout.item_appr_list, new String[]{"text", "detail"},
                            new int[]{R.id.tv_list_text, R.id.tv_list_detail});
                    lv_appr.setAdapter(myAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
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
}
