package com.cyw.mobileoffice.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyw.mobileoffice.R;


/**
 * Created by cyw on 2016/5/26.
 */
public class SettingItemLayout extends LinearLayout {
    private ImageView iv_setting;
    private TextView tv_setting;
    private ImageView iv_next;
    public SettingItemLayout(Context context){
        super(context);
    }

    public SettingItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //在构造函数中将Xml中定义的布局解析出来。
       View view =  LayoutInflater.from(context).inflate(R.layout.item_setting, this, true);
        iv_setting = (ImageView) view.findViewById(R.id.iv_setting);
        tv_setting = (TextView)view.findViewById(R.id.tv_setting);
        iv_next = (ImageView) view.findViewById(R.id.iv_next);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SettingItemLayout);
        CharSequence text = a.getText(R.styleable.SettingItemLayout_android_text);
        if(text != null) tv_setting.setText(text);
//        Log.d("Tag",tv_setting.getText().toString());
        Drawable drawable = a.getDrawable(R.styleable.SettingItemLayout_android_src);
        if(drawable!=null)iv_setting.setImageDrawable(drawable);
        boolean showNext = a.getBoolean(R.styleable.SettingItemLayout_hasNext,false);
//        Log.d("Tag",showNext?"true":"false");
        if(!showNext)iv_next.setVisibility(View.GONE);
        a.recycle();
    }
}
