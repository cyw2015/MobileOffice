package com.cyw.mobileoffice.fragment;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import com.cyw.mobileoffice.R;
import com.cyw.mobileoffice.adapter.MessageAdapter;
import com.cyw.mobileoffice.entity.Document;
import com.cyw.mobileoffice.util.AppURL;
import com.cyw.mobileoffice.view.SpaceItemDecoration;
import com.race604.flyrefresh.FlyRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment implements FlyRefreshLayout.OnPullRefreshListener {
    private FlyRefreshLayout mFlylayout;
    private RecyclerView mListView;
    private MessageAdapter mAdapter;
    private List<Document> mDataSet = null;
    private Handler mHandler = new Handler();
    private LinearLayoutManager mLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_message, container, false);
        mDataSet = new ArrayList<>();
        initDataSet();
        mFlylayout= (FlyRefreshLayout) view.findViewById(R.id.fly_layout);
        mFlylayout.setOnPullRefreshListener(this);
        mListView=(RecyclerView)view.findViewById(R.id.rv_message);
        mLayoutManager = new LinearLayoutManager(getContext());
        mListView.setLayoutManager(mLayoutManager);
        mListView.addItemDecoration(new SpaceItemDecoration(20));//设置间距
        mAdapter = new MessageAdapter(getContext(),mDataSet);
        mListView.setAdapter(mAdapter);
        View actionButton = mFlylayout.getHeaderActionButton();
        if(actionButton!=null){
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFlylayout.startRefresh();
                }
            });
        }
        return view;
    }
    private void initDataSet() {
        RequestParams params =  new RequestParams(AppURL.GETVIEWDOCPAGE);
        params.addQueryStringParameter("sort","pubtime");
        params.addQueryStringParameter("order","desc");
        params.addQueryStringParameter("getAll","1");
        x.http().post(params,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                List<Document> Documents = new ArrayList<>();
                try {
                    JSONObject obj = new JSONObject(result);
                    String rows=obj.getString("rows");
                    if(rows.length()!=0) {
                        JSONArray arr = new JSONArray(rows);
                        for (int i = 0; i < arr.length(); i++) {
                            Document Document = new Document();
                            Document.setTitle(arr.getJSONObject(i).getString("doc_title"));
                            Document.setCreaterName(arr.getJSONObject(i).getString("creater_name"));
                            Document.setDate(arr.getJSONObject(i).getString("pubTime"));
                            Documents.add(Document);
                        }
                        mDataSet.addAll(Documents);
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
    }
    private void addDocument() {
        Document Document = new Document("李秋8","jjjjj","总裁","19999909090");
        mDataSet.add(0, Document);
        mAdapter.notifyItemInserted(0);
        mLayoutManager.scrollToPosition(0);
    }

    @Override
    public void onRefresh(FlyRefreshLayout view) {
        View child = mListView.getChildAt(0);
        if(child!=null){
            bounceAnimateView(child.findViewById(R.id.icon));
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFlylayout.onRefreshFinish();
            }
        },2000);
    }
    private void bounceAnimateView(View view){
        if(view==null){
            return;
        }
        Animator swing = ObjectAnimator.ofFloat(view, "rotationX", 0, 30, -20, 0);
        swing.setDuration(400);
        swing.setInterpolator(new AccelerateInterpolator());
        swing.start();
    }

    @Override
    public void onRefreshAnimationEnd(FlyRefreshLayout view) {
//        addDocument();
    }
}
