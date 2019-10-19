package com.androlord.prayaas.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androlord.prayaas.R;
import com.androlord.prayaas.Support.AdminBookView;
import com.androlord.prayaas.Support.BookDetails;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerViewReportList extends RecyclerView.Adapter<RecyclerViewReportList.myViewHolder>{
    ArrayList<HashMap<String,String>> list= new ArrayList<HashMap<String, String>>();
    Context context;
    public RecyclerViewReportList(Context context,ArrayList<HashMap<String,String>> list)
    {
        this.list=list;
        this.context=context;
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_reports,parent,false);
        myViewHolder myViewHolder=new myViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, final int position) {
        holder.name.setText(list.get(position).get("Title"));
        Glide.with(context).asBitmap().load(list.get(position).get("Cover")).transform(new RoundedCorners(4)).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(holder.imageView);
        holder.singleReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = list.get(position);
                Intent intent=new Intent(context, BookDetails.class);
                intent.putExtra("bookdetails",map);
                intent.putExtra("Admin","Yes");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        LinearLayout singleReport;
        ImageView imageView;
        TextView name;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.reportBookCover);
            name=itemView.findViewById(R.id.reportBookTitle);
            singleReport=itemView.findViewById(R.id.singleReport);
        }
    }
}
