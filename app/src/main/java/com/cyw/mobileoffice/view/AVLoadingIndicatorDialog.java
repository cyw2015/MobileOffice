package com.cyw.mobileoffice.view;


import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cyw.mobileoffice.R;

/**
 * Created by cyw on 2016/5/27.
 */
public class AVLoadingIndicatorDialog extends AlertDialog {

    private TextView mMessageView;

    public AVLoadingIndicatorDialog(Context context) {
        super(context);
        View view= LayoutInflater.from(getContext()).inflate(R.layout.progress_avld,null);
        mMessageView= (TextView) view.findViewById(R.id.pi_message);
        setView(view);
    }


    @Override
    public void setMessage(CharSequence message) {
        Log.d("mess",message.toString());
        mMessageView.setText(message);
    }
}