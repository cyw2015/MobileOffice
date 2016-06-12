package com.cyw.mobileoffice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import com.cyw.mobileoffice.R;

public class MainActivity extends AppCompatActivity {
    private ImageView iv ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent it = new Intent(this,LoginActivity.class);
        startActivity(it);
        finish();
    }
}
