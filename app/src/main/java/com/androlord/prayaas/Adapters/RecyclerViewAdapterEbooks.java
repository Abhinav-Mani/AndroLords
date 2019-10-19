package com.androlord.prayaas.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.pdf.PdfRenderer;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androlord.prayaas.DataClass.EbookDetails;
import com.androlord.prayaas.R;
import com.androlord.prayaas.Support.ViewPdf;

import java.util.ArrayList;

public class RecyclerViewAdapterEbooks extends RecyclerView.Adapter<RecyclerViewAdapterEbooks.ebookViewHolder>{
    ArrayList<EbookDetails> list=new ArrayList<EbookDetails>();
    Context context;
    public RecyclerViewAdapterEbooks(Context context,ArrayList<EbookDetails> list)
    {
        this.list=list;
        this.context=context;
    }
    @NonNull
    @Override
    public ebookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("ak47", "onCreateViewHolder: ");
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.singleebookitem,parent,false);
        ebookViewHolder ebookViewHolder=new ebookViewHolder(v);
        return ebookViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ebookViewHolder holder, final int position) {
        holder.name.setText(list.get(position).name);
        Log.d("ak47", "onBindViewHolder: "+list.get(position).url);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ViewPdf.class);
                intent.putExtra("Data", list.get(position));
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ebookViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView name;
        public ebookViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.single_ebook_name);
            linearLayout=itemView.findViewById(R.id.EbookItem);
        }
    }
}
