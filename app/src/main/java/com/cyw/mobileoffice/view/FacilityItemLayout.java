package com.cyw.mobileoffice.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyw.mobileoffice.R;

/**
 * Created by cyw on 2016/5/28.
 */
public class FacilityItemLayout extends RelativeLayout{
    private ImageView iv_func;
    private TextView tv_func;
    public FacilityItemLayout(Context context) {
        super(context);
    }
    public FacilityItemLayout(Context context, AttributeSet attrs){
        super(context, attrs);
        View view =  LayoutInflater.from(context).inflate(R.layout.item_facility, this, true);
        iv_func = (ImageView) view.findViewById(R.id.iv_func);
        tv_func = (TextView)view.findViewById(R.id.tv_func);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FacilityItemLayout);
        CharSequence text = a.getText(R.styleable.FacilityItemLayout_android_text);
        if(text != null) tv_func.setText(text);
        Log.d("Tag",tv_func.getText().toString());
        Drawable drawable = a.getDrawable(R.styleable.FacilityItemLayout_android_src);
        if(drawable!=null)iv_func.setImageDrawable(drawable);
        a.recycle();
    }
}
