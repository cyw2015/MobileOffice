package com.cyw.mobileoffice.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cyw.mobileoffice.R;
import com.cyw.mobileoffice.adapter.GridAdapter;
import com.cyw.mobileoffice.entity.Document;
import com.cyw.mobileoffice.util.AppURL;
import com.cyw.mobileoffice.view.AVLoadingIndicatorDialog;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

public class ApproveActivity extends AppCompatActivity {
    private PullToRefreshListView pullToRefresh;
    private ArrayList<Document> data = new ArrayList<>();
    GridAdapter<Document> adapter = null;
    private AVLoadingIndicatorDialog myDialog;
    private int page = 1;
    private String rowsStr = "20";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve);
        pullToRefresh = (PullToRefreshListView) findViewById(R.id.pullToRefresh);
        data = new ArrayList<>();
        //初始数据
        page = 1;
        initData();
        //绑定数据
        adapter = new GridAdapter<Document>(data, R.layout.item_publish_list) {
            @Override
            public void bindView(ViewHolder holder, Document obj) {
                holder.setText(R.id.tv_pub_title, obj.getTitle());
                String state = obj.getState();//审批状态
                String str = null;
                if (state != null && !state.equals("")) {
                    if (state.equals("1")) {
                        str = "待审批";
                    } else if (state.equals("2")) {
                        str = "审批通过";
                    } else if (state.equals("-1")) {
                        str = "审批未通过";
                    }
                    holder.setText(R.id.tv_appr, str);
                }
                holder.setText(R.id.tv_create_date, obj.getEditTime());
                holder.setText(R.id.tv_pub_creater,obj.getCreaterName());
            }
        };

        initPullTORefresh();
    }

    //初始化下拉上拉组件
    private void initPullTORefresh() {
        pullToRefresh.setAdapter(adapter);
        pullToRefresh.setMode(PullToRefreshBase.Mode.BOTH);
        ILoadingLayout startLabels = pullToRefresh.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout endLabels = pullToRefresh.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        //刷新事件
        pullToRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                adapter.clear();//清空数据
                page = 1;
                initData();
                new FinishRefresh().execute();
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                initData();//添加数据
                new FinishRefresh().execute();
            }
        });

        pullToRefresh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(ApproveActivity.this, PublishDetailActivity.class);
                it.putExtra("doc", data.get(position - 1));
                startActivity(it);
            }
        });
    }

    public void initData(){
        myDialog = new AVLoadingIndicatorDialog(this);
        myDialog.setMessage("Loading");
        myDialog.setCancelable(false);
        myDialog.show();
        RequestParams params = new RequestParams(AppURL.APPROVALDATA);
        params.addQueryStringParameter("page", String.valueOf(page));
        params.addQueryStringParameter("rows", rowsStr);
        params.addQueryStringParameter("sort", "apprState");
        params.addQueryStringParameter("order", "asc");
        x.http().post(params,  new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                ArrayList<Document> documents = new ArrayList<>();
                try {
                    JSONObject obj = new JSONObject(result);
                    String rows = obj.getString("rows");
                    if (rows.length() != 0) {
                        page++;
                        JSONArray arr = new JSONArray(rows);
                        for (int i = 0; i < arr.length(); i++) {
                            Document document = new Document();
                            document.setDocCode(arr.getJSONObject(i).getString("docCode"));
                            document.setTitle(arr.getJSONObject(i).getString("docTitle"));
                            document.setEditTime(arr.getJSONObject(i).getString("editTime"));
                            document.setRecipients(arr.getJSONObject(i).getString("createrName"));
                            String str = arr.getJSONObject(i).getString("apprState");
                            if(str.equals("0")){
                                document.setState("1");
                            }else if(str.equals("1")){
                                document.setState("2");
                            }else if(str.equals("-1")){
                                document.setState("-1");
                            }
                            //审批状态
                            documents.add(document);
                        }
                        data.addAll(documents);
                        adapter.notifyDataSetChanged();
                        myDialog.cancel();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    myDialog.cancel();
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

    private class FinishRefresh extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            pullToRefresh.onRefreshComplete();
        }
    }
}
