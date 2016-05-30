package com.cyw.mobileoffice.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.cyw.mobileoffice.R;
import com.cyw.mobileoffice.adapter.GridAdapter;
import com.cyw.mobileoffice.entity.FuncItem;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FacilityFragment extends Fragment {

    private GridView gv_Func;
    private BaseAdapter mAdapter = null;
    private ArrayList<FuncItem>mData = null;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_facility, container, false);
        gv_Func = (GridView) view.findViewById(R.id.gv_func);
        mData= new ArrayList<FuncItem>();
        mData.add(new FuncItem(R.mipmap.look,"查看公文"));
        mData.add(new FuncItem(R.mipmap.publish,"公文发布"));
        mData.add(new FuncItem(R.mipmap.approve,"公文审批"));
        mData.add(new FuncItem(R.mipmap.department,"部门管理"));
        mData.add(new FuncItem(R.mipmap.position,"岗位管理"));
        mData.add(new FuncItem(R.mipmap.employee,"员工管理"));
        mData.add(new FuncItem(R.mipmap.role,"角色管理"));
        mData.add(new FuncItem(R.mipmap.auth,"权限管理"));
        mData.add(new FuncItem(R.mipmap.users,"用户管理"));
        mAdapter = new GridAdapter<FuncItem>(mData,R.layout.item_facility) {

            @Override
            public void bindView(ViewHolder holder, FuncItem obj) {
                holder.setImageResource(R.id.iv_func,obj.getiId());
                holder.setText(R.id.tv_func,obj.getiName());
            }
        };
        gv_Func.setAdapter(mAdapter);
        gv_Func.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),"你点击了.."+position+"项",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
