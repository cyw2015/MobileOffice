package com.cyw.mobileoffice.fragment;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cyw.mobileoffice.R;
import com.cyw.mobileoffice.util.AppURL;
import com.cyw.mobileoffice.util.SharedHelper;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * A simple {@link Fragment} subclass.
 */
public class MySettingFragment extends Fragment {
    private ImageView ci_emp;

    public MySettingFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_mysetting, container, false);
        ci_emp = (ImageView) view.findViewById(R.id.ci_emp);
        getImageUrl();
        return view;
    }

    private void getImageUrl() {
        RequestParams params = new RequestParams(AppURL.GETEMPIMAGEURL);
        String user = (String) SharedHelper.get(getContext(), "currentUser", "");
        if (user == null || user.equals("")) {
            return;
        } else {
            params.addQueryStringParameter("user", user);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
//                    Toast.makeText(x.app(), result, Toast.LENGTH_LONG).show();
                    try {
                        JSONObject obj = new JSONObject(result);
                        String url = obj.getString("errorMsg");
                        setImage(url);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
//                    Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(CancelledException cex) {
//                    Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFinished() {

                }
            });
        }

    }
    private void setImage(String url){
        ImageOptions options = new ImageOptions.Builder()
                // 是否忽略GIF格式的图片
                .setIgnoreGif(false)
                .setSize(DensityUtil.dip2px(104),DensityUtil.dip2px(104))
                .setRadius(DensityUtil.dip2px(104))
                // 图片缩放模式
                .setCrop(true)
                // 下载中显示的图片
                .setLoadingDrawableId(R.mipmap.emp)
                // 下载失败显示的图片
                .setFailureDrawableId(R.mipmap.emp)
                // 得到ImageOptions对象
                .build();
        // 加载图片
        url = AppURL.IMAGEPATH+url;
        SharedHelper.put(getContext(),"currentUrl",url);
        x.image().bind(ci_emp,url, options, new Callback.CommonCallback<Drawable>() {
            @Override
            public void onSuccess(Drawable arg0) {
                LogUtil.e("下载成功");
            }
            @Override
            public void onFinished() {
                LogUtil.e("下载完成");
            }
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                LogUtil.e("下载出错，" + arg0.getMessage());
            }
            @Override
            public void onCancelled(CancelledException arg0) {
                LogUtil.e("下载取消");
            }
        });
    }
}
