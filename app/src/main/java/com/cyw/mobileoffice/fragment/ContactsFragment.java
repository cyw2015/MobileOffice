package com.cyw.mobileoffice.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cyw.mobileoffice.R;
import com.cyw.mobileoffice.activity.ContactDetailActivity;
import com.cyw.mobileoffice.adapter.ContactAdapter;
import com.cyw.mobileoffice.entity.Contact;
import com.cyw.mobileoffice.listener.OnRecylerItemClickLitener;
import com.cyw.mobileoffice.util.AppURL;
import com.cyw.mobileoffice.view.SpaceItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment {

    private RecyclerView rc_contacts;
    private List<Contact> mDatas;
    private ContactAdapter recycleAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_contacts, container, false);
        rc_contacts = (RecyclerView) view.findViewById(R.id.rc_contacts);
        mDatas = new ArrayList<>();
        initData();
        recycleAdapter = new ContactAdapter(getContext(), mDatas);
        recycleAdapter.setOnItemClickLitener(new OnRecylerItemClickLitener(){

            @Override
            public void onItemClick(View view, int position) {
                Intent it =  new Intent(getContext(),ContactDetailActivity.class);
                Contact contact= mDatas.get(position);
                it.putExtra("contact", contact);
                startActivity(it);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        rc_contacts.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rc_contacts.setLayoutManager(layoutManager);
        rc_contacts.setAdapter(recycleAdapter);
        rc_contacts.addItemDecoration(new SpaceItemDecoration(5));
        rc_contacts.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    private void initData() {
        RequestParams params =  new RequestParams(AppURL.GETCONTACTS);
        x.http().get(params,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                List<Contact> contacts = new ArrayList<>();
                try {
                    JSONArray arr = new JSONArray(result);
                    for(int i = 0;i<arr.length();i++){
                        Contact contact = new Contact();
                        contact.setCode(arr.getJSONObject(i).getString("emp_code"));
                        contact.setName(arr.getJSONObject(i).getString("emp_name"));//名称
                        contact.setSex(arr.getJSONObject(i).getString("emp_sex"));//性别
                        contact.setDeptCode(arr.getJSONObject(i).getString("dept_code"));
                        contact.setDepartment(arr.getJSONObject(i).getString("dept_name"));//部门
                        contact.setPostCode(arr.getJSONObject(i).getString("posi_code"));
                        contact.setPosition(arr.getJSONObject(i).getString("posi_name"));//岗位
                        contact.setBirthDay(arr.getJSONObject(i).getString("birthday"));//出生年月
                        contact.setEmail(arr.getJSONObject(i).getString("email"));//邮箱
                        contact.setAddress(arr.getJSONObject(i).getString("address"));//地址
                        contact.setPhone(arr.getJSONObject(i).getString("telphone"));//电话
                        contact.setUrl(arr.getJSONObject(i).getString("emp_image"));//url
                        contacts.add(contact);
                    }
                    mDatas.addAll(contacts);
                    recycleAdapter.notifyDataSetChanged();
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
}
