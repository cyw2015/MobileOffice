package com.cyw.mobileoffice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyw.mobileoffice.R;
import com.cyw.mobileoffice.entity.Contact;
import com.cyw.mobileoffice.util.AppURL;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * Created by cyw on 2016/6/1.
 */
public class ContactDetailActivity extends AppCompatActivity {

    private ImageView iv_contact_detail;
    private TextView tv_name_detail;
    private TextView tv_depart_name;
    private TextView tv_posi_name;
    private TextView tv_phone_detail;
    private TextView tv_email;
    private TextView tv_address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        Intent it = getIntent();
        Contact contact  = (Contact) it.getSerializableExtra("contact");
        iv_contact_detail = (ImageView) findViewById(R.id.iv_contact_detail);
        tv_name_detail = (TextView) findViewById(R.id.tv_name_detail);
        tv_depart_name = (TextView) findViewById(R.id.tv_depart_name);
        tv_posi_name = (TextView) findViewById(R.id.tv_posi_name);
        tv_phone_detail = (TextView) findViewById(R.id.tv_phone_detail);
        tv_email =  (TextView)findViewById(R.id.tv_email);
        tv_address = (TextView) findViewById(R.id.tv_address);

        //绑定数据
        ImageOptions options = new ImageOptions.Builder()
                // 是否忽略GIF格式的图片
                .setIgnoreGif(false)
                .setSize(DensityUtil.dip2px(85),DensityUtil.dip2px(85))
                .setRadius(DensityUtil.dip2px(85))
                // 图片缩放模式
                .setCrop(true)
                // 下载中显示的图片
                .setLoadingDrawableId(R.mipmap.emp)
                // 下载失败显示的图片
                .setFailureDrawableId(R.mipmap.emp)
                // 得到ImageOptions对象
                .build();
        String url = AppURL.IMAGEPATH;
        if(contact.getUrl()!=null &&!contact.getUrl().equals("")){
            url +=contact.getUrl();
        }else{
            url +="empDefault.jpg";
        }
        x.image().bind(iv_contact_detail, url,options);
        tv_name_detail.setText(contact.getName());
        tv_depart_name.setText(contact.getDepartment());
        tv_posi_name.setText(contact.getPosition());
        tv_phone_detail.setText(contact.getPhone());
        tv_email.setText(contact.getEmail());
        tv_address.setText(contact.getAddress());
    }
}
