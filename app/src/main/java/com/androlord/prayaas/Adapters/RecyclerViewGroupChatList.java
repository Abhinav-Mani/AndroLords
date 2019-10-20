package com.androlord.prayaas.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androlord.prayaas.DataClass.GroupMessage;
import com.androlord.prayaas.NavigationFragments.GroupChat;
import com.androlord.prayaas.R;
import com.androlord.prayaas.Support.GroupChatBox;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerViewGroupChatList extends RecyclerView.Adapter<RecyclerViewGroupChatList.myHolder> {
    TextView textView;
    ImageView imageView;
    Context context;
    ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
    public RecyclerViewGroupChatList(ArrayList<HashMap<String,String>> list,Context context)
    {
        this.context=context;
        this.list=list;
    }
    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View V= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlegroupitem,parent,false);
        myHolder myHolder=new myHolder(V);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, final int position) {
        holder.textView.setText(list.get(position).get("Name"));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, GroupChatBox.class);
                intent.putExtra("Data",list.get(position));
                context.startActivity(intent);
            }
        });
        Glide.with(context).asBitmap().load(Uri.parse(list.get(position).get("Cover"))).transform(new RoundedCorners(10)).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class myHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView textView;
        ImageView imageView;
        public myHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.GroupName);
            imageView=itemView.findViewById(R.id.GroupIcon);
            linearLayout=itemView.findViewById(R.id.GroupItem);
        }
    }
}
