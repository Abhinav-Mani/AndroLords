package com.androlord.prayaas.Adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androlord.prayaas.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterMyBooks extends RecyclerView.Adapter<RecyclerViewAdapterMyBooks.MyViewHolder>{

    @NonNull
    ArrayList<Map<String,String>> list=new ArrayList<Map<String,String>>();
    Context context;
    public RecyclerViewAdapterMyBooks(ArrayList<Map<String,String>> list,Context context)
    {
        this.list=list;
        this.context=context;
    }

    @Override
    public RecyclerViewAdapterMyBooks.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mysingle_book,viewGroup,false);
        RecyclerViewAdapterMyBooks.MyViewHolder vh=new RecyclerViewAdapterMyBooks.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterMyBooks.MyViewHolder viewHolder, int i) {
        Log.d("ak47", "onBindViewHolder: "+list.get(i));
        viewHolder.bookname.setText("Title:-"+list.get(i).get("Title"));
        viewHolder.author.setText("Author:-"+list.get(i).get("Author"));
        viewHolder.intent.setText("Intentions:-"+list.get(i).get("Pref"));
        viewHolder.exam.setText("Exam:-"+list.get(i).get("Exam"));
        Glide.with(context).asBitmap().load(list.get(i).get("Cover")).transform(new RoundedCorners(10)).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(viewHolder.cover);

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView author,bookname,intent,exam;
        ImageView cover;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            author=itemView.findViewById(R.id.mysinglebook_author);
            bookname=itemView.findViewById(R.id.mysinglebook_title);
            cover=itemView.findViewById(R.id.mysinglebook_cover);
            intent=itemView.findViewById(R.id.mysinglebook_intent);
            exam=itemView.findViewById(R.id.mysinglebook_city);
        }
    }
}
