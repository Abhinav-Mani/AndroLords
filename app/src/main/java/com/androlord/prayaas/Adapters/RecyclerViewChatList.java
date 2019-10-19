package com.androlord.prayaas.Adapters;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androlord.prayaas.R;
import com.androlord.prayaas.Support.ChatBox;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewChatList extends RecyclerView.Adapter<RecyclerViewChatList.ChatListViewHolder>{
    @NonNull
    ArrayList<Map<String,String>> contacts=new ArrayList<Map<String, String>>();
    Context context;
    public RecyclerViewChatList(ArrayList<Map<String,String>> contacts, Context context)
    {
        this.contacts=contacts;
        this.context=context;
    }
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.singlecontact,viewGroup,false);
        ChatListViewHolder chatListViewHolder=new ChatListViewHolder(v);
        return chatListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder chatListViewHolder, int i) {
        Log.d("logno", "onBindViewHolder: "+i+" "+contacts.get(i).get("Title"));
        final HashMap<String,String> data=new HashMap<String,String>();
        data.put("Title",contacts.get(i).get("Title"));
        data.put("Cover",contacts.get(i).get("Cover"));
        data.put("User",contacts.get(i).get("User"));
        data.put("Source","1");
        chatListViewHolder.title.setText(contacts.get(i).get("Title"));
        Log.e("ak47", "onBindViewHolder: "+contacts.get(i).get("Cover") );
        Glide.with(context).asBitmap().load(contacts.get(i).get("Cover")).transform(new RoundedCorners(4)).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(chatListViewHolder.cover);
        chatListViewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ChatBox.class);
                intent.putExtra("Data",data);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class ChatListViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item;
        ImageView cover;
        TextView title;
        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);
            item=itemView.findViewById(R.id.single_contact_item);
            cover=itemView.findViewById(R.id.singlecontactbookcover);
            title=itemView.findViewById(R.id.singlecontactbookname);
        }
    }

}
