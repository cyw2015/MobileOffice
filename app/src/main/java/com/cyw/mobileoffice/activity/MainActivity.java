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
/*
    RequestParams params = new RequestParams("http://cyw.ngrok.cc/login");
    params.addQueryStringParameter("username","admin");
    params.addQueryStringParameter("password","admin");
    params.addQueryStringParameter("f","android");
    x.http().post(params,new Callback.CommonCallback<String>(){

        @Override
        public void onSuccess(String result) {
            Toast.makeText(x.app(),result,Toast.LENGTH_LONG).show();
            getImageUrl();
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            Toast.makeText(x.app(),ex.getMessage(),Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancelled(Callback.CancelledException cex) {
            Toast.makeText(x.app(),"cancelled",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFinished() {

        }
    });
    private void getImageUrl(){
        RequestParams params = new RequestParams("http://cyw.ngrok.cc/person/getUserEmpImage.do");
        params.addQueryStringParameter("user","160401000");
        x.http().post(params,new Callback.CommonCallback<String>(){
            @Override
            public void onSuccess(String result) {
                Toast.makeText(x.app(),result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject obj = new JSONObject(result);
                    String url =obj.getString("errorMsg");
                    setImage(url);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(),ex.getMessage(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(),"cancelled",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {

            }
        });

    }

    private void setImage(String url){
        x.image().bind(iv,"http://cyw.ngrok.cc/imageEmp/"+url);
    }
    */
}
