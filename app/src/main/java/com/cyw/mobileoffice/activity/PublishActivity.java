package com.cyw.mobileoffice.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cyw.mobileoffice.R;
import com.cyw.mobileoffice.adapter.GridAdapter;
import com.cyw.mobileoffice.entity.Document;
import com.cyw.mobileoffice.entity.ErrorMessage;
import com.cyw.mobileoffice.util.AppURL;
import com.cyw.mobileoffice.view.AVLoadingIndicatorDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

public class PublishActivity extends AppCompatActivity {
    private PullToRefreshListView pullToRefresh;
    private ArrayList<Document> data = new ArrayList<>();
    GridAdapter<Document> adapter = null;
    private AVLoadingIndicatorDialog myDialog;
    private int page = 1;
    private String rowsStr = "20";
    private AdapterView.AdapterContextMenuInfo selectMenuInfo;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        pullToRefresh = (PullToRefreshListView) findViewById(R.id.pullToRefresh);
        data = new ArrayList<>();
        page = 1;
        //初始数据
        initData();
        //绑定数据
        adapter = new GridAdapter<Document>(data, R.layout.item_publish_list) {
            @Override
            public void bindView(ViewHolder holder, Document obj) {
                holder.setText(R.id.tv_pub_title, obj.getTitle());
                String state = obj.getState();
                String str = null;
                if (state != null && !state.equals("")) {
                    if (state.equals("0")) {
                        str = "未送审";
                    } else if (state.equals("1")) {
                        str = "待审批";
                    } else if (state.equals("2")) {
                        str = "审批通过";
                    } else if (state.equals("-1")) {
                        str = "审批未通过";
                    }
                    holder.setText(R.id.tv_appr, str);
                }
                holder.setText(R.id.tv_create_date, obj.getEditTime());
            }
        };

        pullToRefresh.setAdapter(adapter);
        pullToRefresh.setMode(PullToRefreshBase.Mode.BOTH);
        init();
        pullToRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                adapter.clear();
                page = 1;
                initData();
                new FinishRefresh().execute();
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                initData();
                new FinishRefresh().execute();
//                adapter.notifyDataSetChanged();
            }
        });
        pullToRefresh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(PublishActivity.this, PublishDetailActivity.class);
                it.putExtra("doc", data.get(position - 1));
                startActivity(it);
            }
        });
        pullToRefresh.getRefreshableView().setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                MenuInflater menuInflater = getMenuInflater();
                menuInflater.inflate(R.menu.menu_pub_act, menu);
                menu.setHeaderTitle("选择操作");
                selectMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
            }
        });
    }

    //初始化下拉上拉组件
    private void init() {
        ILoadingLayout startLabels = pullToRefresh.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout endLabels = pullToRefresh.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
    }

    //初始数据
    private void initData() {
        myDialog = new AVLoadingIndicatorDialog(this);
        myDialog.setMessage("Loading");
        myDialog.setCancelable(false);
        myDialog.show();
        RequestParams params = new RequestParams(AppURL.GETPUBDOCURL);
        params.addQueryStringParameter("page", String.valueOf(page));
        params.addQueryStringParameter("rows", rowsStr);
        params.addQueryStringParameter("sort", "editTime");
        params.addQueryStringParameter("order", "desc");
        params.addQueryStringParameter("isContent", "yes");
        x.http().post(params, new Callback.CommonCallback<String>() {
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
                            document.setDocCode(arr.getJSONObject(i).getString("doc_code"));
                            document.setTitle(arr.getJSONObject(i).getString("doc_title"));
                            document.setEditTime(arr.getJSONObject(i).getString("editTime"));
                            document.setRecipients(arr.getJSONObject(i).getString("recipients"));
                            document.setRecipientsCode(arr.getJSONObject(i).getString("recipientsCode"));
                            document.setState(arr.getJSONObject(i).getString("state"));
                            document.setAttachment(arr.getJSONObject(i).getString("attachment"));
                            document.setAttachmentPath(arr.getJSONObject(i).getString("attachmentPath"));
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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (info == null) {
            info = selectMenuInfo;
        }
        switch (item.getItemId()) {
            case R.id.menu_edit_pub:
                editPub(info.position);
                return true;
            case R.id.menu_send_appr:
                sendAppr(info.position);
                return true;
            case R.id.menu_delete_pub:
                deletePub(info.position);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    //送审
    private void sendAppr(int position) {
        final Document doc = data.get(position - 1);
        if (doc.getState().equals("0")) {
            alert = null;
            builder = new AlertDialog.Builder(PublishActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);

            alert = builder.setIcon(R.mipmap.info)
                    .setTitle("操作提示：")
                    .setMessage("是否确定送审选定公文?\n" + doc.getTitle() + "\n")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            Toast.makeText(PublishActivity.this, "你点击了取消按钮~", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myDialog = new AVLoadingIndicatorDialog(PublishActivity.this);
                            myDialog.setMessage("Loading");
                            myDialog.setCancelable(false);
                            myDialog.show();
                            RequestParams params = new RequestParams(AppURL.SendAppr);
                            params.addQueryStringParameter("docCode", doc.getDocCode());
                            x.http().post(params, new Callback.CommonCallback<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    Gson gson = new Gson();
                                    try {
                                        ErrorMessage msg = gson.fromJson(result, new TypeToken<ErrorMessage>() {
                                        }.getType());
                                        int error = msg.getError();
                                        if (error == 0) {
                                            Toast.makeText(PublishActivity.this, "送审成功,请刷新列表", Toast.LENGTH_SHORT).show();
                                            myDialog.cancel();
                                            //initData();
                                        } else {
                                            Toast.makeText(PublishActivity.this, msg.getErrorMsg(), Toast.LENGTH_SHORT).show();
                                            myDialog.cancel();
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(PublishActivity.this, "送审出错", Toast.LENGTH_SHORT).show();
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
                    }).create();             //创建AlertDialog对象
            alert.show();                    //显示对话框
        } else {
            Toast.makeText(PublishActivity.this, "只能送审未送审的公文", Toast.LENGTH_SHORT).show();
        }
    }

    //删除
    private void deletePub(final int position) {
        final Document doc = data.get(position - 1);
        if (doc.getState().equals("0")) {
            alert = null;
            builder = new AlertDialog.Builder(PublishActivity.this);
            alert = builder.setIcon(R.mipmap.info)
                    .setTitle("操作提示：")
                    .setMessage("是否确定删除选定公文?\n" + doc.getTitle() + "\n")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myDialog = new AVLoadingIndicatorDialog(PublishActivity.this);
                            myDialog.setMessage("Loading");
                            myDialog.setCancelable(false);
                            myDialog.show();
                            RequestParams params = new RequestParams(AppURL.DELETEPUBDOC);
                            params.addQueryStringParameter("ids", doc.getDocCode());
                            params.addQueryStringParameter("attachmentPaths",doc.getAttachmentPath());
                            x.http().post(params,new  Callback.CommonCallback<String>() {
                                @Override
                                public void onSuccess(String result){
                                    Gson gson = new Gson();
                                    try {
                                        ErrorMessage msg = gson.fromJson(result, new TypeToken<ErrorMessage>() {
                                        }.getType());
                                        int error = msg.getError();
                                        if (error == 0) {
                                            Toast.makeText(PublishActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                            myDialog.cancel();
//                                            initData();
                                            adapter.remove(position-1);
                                        } else {
                                            Toast.makeText(PublishActivity.this, msg.getErrorMsg(), Toast.LENGTH_SHORT).show();
                                            myDialog.cancel();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(PublishActivity.this, "删除出错", Toast.LENGTH_SHORT).show();
                                        myDialog.cancel();
                                    }
                                }

                                @Override
                                public void onError(Throwable ex, boolean isOnCallback) {
                                    Toast.makeText(PublishActivity.this,"出错",Toast.LENGTH_SHORT).show();
                                    myDialog.cancel();
                                }

                                @Override
                                public void onCancelled(CancelledException cex) {
                                    Toast.makeText(PublishActivity.this,"取消",Toast.LENGTH_SHORT).show();
                                    myDialog.cancel();
                                }

                                @Override
                                public void onFinished() {
                                    myDialog.cancel();
                                }
                            });
                        }
                    }).create();
            alert.show(); //显示对话框
        } else {
            Toast.makeText(PublishActivity.this, "只能删除未送审的公文", Toast.LENGTH_SHORT).show();
        }
    }

    //修改
    private void editPub(int position) {
        Document doc = data.get(position - 1);
        if (doc.getState().equals("0")) {

        } else {
            Toast.makeText(PublishActivity.this, "只能修改未送审的公文", Toast.LENGTH_SHORT).show();
        }
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