package com.androlord.prayaas.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androlord.prayaas.DataClass.EbookDetails;
import com.androlord.prayaas.R;

import java.util.ArrayList;

public class RecyclerViewAdapterEbooks extends RecyclerView.Adapter<RecyclerViewAdapterEbooks.ebookViewHolder>{
    ArrayList<EbookDetails> list=new ArrayList<EbookDetails>();
    public RecyclerViewAdapterEbooks(ArrayList<EbookDetails> list)
    {
        this.list=list;
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
    public void onBindViewHolder(@NonNull ebookViewHolder holder, int position) {
        holder.name.setText(list.get(position).name);
        Log.d("ak47", "onBindViewHolder: "+list.get(position).name);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ebookViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        public ebookViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.single_ebook_name);
        }
    }
}
