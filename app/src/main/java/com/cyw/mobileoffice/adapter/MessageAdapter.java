package com.cyw.mobileoffice.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cyw.mobileoffice.R;
import com.cyw.mobileoffice.entity.Document;

import java.util.List;

/**
 * Created by cyw on 2016/5/30.
 */
public class MessageAdapter  extends RecyclerView.Adapter<MessageAdapter.ItemViewHolder>{
    private LayoutInflater mInflater;
    private List<Document> mDataSet;
    private Context mContext;
    public MessageAdapter(Context context,List<Document>mDataSet){
        this.mDataSet=mDataSet;
        this.mContext=context;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public MessageAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =mInflater.inflate(R.layout.item_message,parent,false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MessageAdapter.ItemViewHolder holder, int position) {
        final Document data = mDataSet.get(position);
        holder.tv_date.setText(data.getDate());
        holder.tv_title.setText(data.getTitle());
        holder.tv_creater.setText(data.getCreaterName());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tv_title;
        TextView tv_date;
        TextView tv_creater;
        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_date =(TextView)itemView.findViewById(R.id.tv_date);
            tv_creater = (TextView)itemView.findViewById(R.id.tv_creater);
        }
    }
}

