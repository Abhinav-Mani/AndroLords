package com.androlord.prayaas.Adapters;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androlord.prayaas.R;
import com.androlord.prayaas.Support.BookDetails;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterBooks extends RecyclerView.Adapter<RecyclerViewAdapterBooks.ViewHolder>{
    @NonNull
    ArrayList<Map<String,String>> list=new ArrayList<Map<String,String>>();
    Context context;
    public RecyclerViewAdapterBooks(ArrayList<Map<String,String>> list,Context context)
    {
        this.list=list;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_book,viewGroup,false);
        ViewHolder vh=new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.d("ak47", "onBindViewHolder: "+list.get(i).get("Title"));
        viewHolder.bookname.setText(list.get(i).get("Title"));
        viewHolder.author.setText(list.get(i).get("Author"));
       // viewHolder.status.setText(list.get(i).get("Pref"));
       viewHolder.rating.setRating(Float.parseFloat(list.get(i).get("Rating"))/2);
        Glide.with(context).asBitmap().load(list.get(i).get("Cover")).transform(new RoundedCorners(4)).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(viewHolder.cover);
        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, BookDetails.class);
                intent.putExtra("bookdetails",(HashMap<String, String>) list.get(i));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item;
        TextView author,bookname;
        ImageView cover;
        FrameLayout frameLayout;
        RatingBar rating;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            author=itemView.findViewById(R.id.single_book_author);
            bookname=itemView.findViewById(R.id.single_book_name);
            cover=itemView.findViewById(R.id.single_book_image);
            rating=itemView.findViewById(R.id.rating);
            frameLayout=itemView.findViewById(R.id.books);
            item=itemView.findViewById(R.id.single_book_item);

        }
    }
}
