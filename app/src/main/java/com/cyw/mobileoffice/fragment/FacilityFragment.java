package com.cyw.mobileoffice.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cyw.mobileoffice.R;
import com.cyw.mobileoffice.activity.ApproveActivity;
import com.cyw.mobileoffice.activity.PublishActivity;
import com.cyw.mobileoffice.adapter.GridAdapter;
import com.cyw.mobileoffice.entity.FuncItem;
import com.cyw.mobileoffice.util.SharedHelper;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FacilityFragment extends Fragment {

    private GridView gv_Func;
    private BaseAdapter mAdapter = null;
    private ArrayList<FuncItem> mData = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_facility, container, false);
        gv_Func = (GridView) view.findViewById(R.id.gv_func);
        String  myResCodes= (String) SharedHelper.get(getContext(),"myResCodes","");
        mData = new ArrayList<FuncItem>();
        if(myResCodes!=null&&myResCodes.contains("ROLE_RES_DOC_LOOK")){
            mData.add(new FuncItem(R.mipmap.look, "查看公文"));
        }
        if(myResCodes!=null&&myResCodes.contains("ROLE_RES_DOC_PUB")){
            mData.add(new FuncItem(R.mipmap.publish, "公文发布"));
        }
        if(myResCodes!=null&&myResCodes.contains("ROLE_RES_DOC_APPROVE")){
            mData.add(new FuncItem(R.mipmap.approve, "公文审批"));
        }
//        mData.add(new FuncItem(R.mipmap.department, "部门管理"));
//        mData.add(new FuncItem(R.mipmap.position, "岗位管理"));
//        mData.add(new FuncItem(R.mipmap.employee, "员工管理"));
//        mData.add(new FuncItem(R.mipmap.role, "角色管理"));
//        mData.add(new FuncItem(R.mipmap.auth, "权限管理"));
//        mData.add(new FuncItem(R.mipmap.users, "用户管理"));
        mAdapter = new GridAdapter<FuncItem>(mData, R.layout.item_facility) {

            @Override
            public void bindView(ViewHolder holder, FuncItem obj) {
                holder.setImageResource(R.id.iv_func, obj.getiId());
                holder.setText(R.id.tv_func, obj.getiName());
            }
        };
        gv_Func.setAdapter(mAdapter);
        gv_Func.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.tv_func);
                String funStr = (String) tv.getText();
//                Toast.makeText(getContext(), funStr, Toast.LENGTH_SHORT).show();
                if (funStr.equals("查看公文")) {
                    RadioButton rb_message = (RadioButton) getActivity().findViewById(R.id.rb_message);
                    rb_message.setChecked(true);
                } else if (funStr.equals("公文发布")) {
                    Intent it = new Intent(getContext(), PublishActivity.class);
                    startActivity(it);
                } else if (funStr.equals("公文审批")) {
                    Intent it = new Intent(getContext(), ApproveActivity.class);
                    startActivity(it);
                }
            }
        });
        return view;
    }
}
