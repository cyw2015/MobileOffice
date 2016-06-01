package com.cyw.mobileoffice.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyw.mobileoffice.R;
import com.cyw.mobileoffice.entity.Contact;
import com.cyw.mobileoffice.listener.OnRecylerItemClickLitener;
import com.cyw.mobileoffice.util.AppURL;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

/**
 * Created by cyw on 2016/5/30.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {
    private List<Contact> mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    private OnRecylerItemClickLitener mOnRecylerItemClickLitener;
    public ContactAdapter(Context context, List<Contact> datas) {
        this.mContext = context;
        this.mDatas = datas;
        inflater = LayoutInflater.from(mContext);
    }
    public void setOnItemClickLitener(OnRecylerItemClickLitener mOnRecylerItemClickLitener)
    {
        this.mOnRecylerItemClickLitener = mOnRecylerItemClickLitener;
    }
    @Override
    public ContactAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_contacts, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ContactAdapter.MyViewHolder holder, int position) {
    //绑定数据
        ImageOptions options = new ImageOptions.Builder()
                // 是否忽略GIF格式的图片
                .setIgnoreGif(false)
                .setSize(DensityUtil.dip2px(85),DensityUtil.dip2px(85))
                .setRadius(DensityUtil.dip2px(85))
                // 图片缩放模式
                .setCrop(true)
                // 下载中显示的图片
                .setLoadingDrawableId(R.mipmap.emp)
                // 下载失败显示的图片
                .setFailureDrawableId(R.mipmap.emp)
                // 得到ImageOptions对象
                .build();
        String url =AppURL.IMAGEPATH;
        if(mDatas.get(position).getUrl()!=null &&!mDatas.get(position).getUrl().equals("")){
            url +=mDatas.get(position).getUrl();
        }else{
            url +="empDefault.jpg";
        }
        x.image().bind(holder.iv_contact, url,options);/*绑定员工头像*/
        holder.tv_name.setText(mDatas.get(position).getName());
        holder.tv_phone.setText(mDatas.get(position).getPhone());
        holder.tv_position.setText(mDatas.get(position).getPosition());
        holder.tv_depart.setText(mDatas.get(position).getDepartment());
        // 如果设置了回调，则设置点击事件
        if (mOnRecylerItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnRecylerItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnRecylerItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_contact;
        TextView tv_name;
        TextView tv_position;
        TextView tv_depart;
        TextView tv_phone;
        View itemView;//暴露视图
        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            iv_contact = (ImageView) itemView.findViewById(R.id.iv_contact);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_position = (TextView) itemView.findViewById(R.id.tv_position);
            tv_depart = (TextView)itemView.findViewById(R.id.tv_depart);
            tv_phone = (TextView) itemView.findViewById(R.id.tv_phone);
        }
    }
}
