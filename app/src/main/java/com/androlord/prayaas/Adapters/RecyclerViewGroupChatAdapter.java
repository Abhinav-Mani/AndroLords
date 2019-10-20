package com.androlord.prayaas.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androlord.prayaas.DataClass.GroupMessage;
import com.androlord.prayaas.R;

import java.util.ArrayList;

public class RecyclerViewGroupChatAdapter extends RecyclerView.Adapter<RecyclerViewGroupChatAdapter.myViewHolder>{
    ArrayList<GroupMessage> list=new ArrayList<GroupMessage>();
    String currentUser;
    public RecyclerViewGroupChatAdapter(ArrayList<GroupMessage> list,String currentUser)
    {
        this.list=list;
        this.currentUser=currentUser;
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_group_chat_bubble,parent,false);
        myViewHolder myViewHolder=new myViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.cuurent.setText(list.get(position).user);
        holder.other.setText(list.get(position).user);
        holder.recivedMessege.setText(list.get(position).messege);
        holder.sendMessege.setText(list.get(position).messege);
        Log.e("ak47", "onBindViewHolder: "+currentUser.equalsIgnoreCase(list.get(position).user));
        if(currentUser.equalsIgnoreCase(list.get(position).user))
        {
            holder.recivedMessege.setVisibility(View.GONE);
            holder.other.setVisibility(View.GONE);
        }
        else
        {
            holder.sendMessege.setVisibility(View.GONE);
            holder.cuurent.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        TextView sendMessege,recivedMessege,cuurent,other;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            sendMessege=itemView.findViewById(R.id.sendMessegeGrp);
            recivedMessege=itemView.findViewById(R.id.recivedMessegeGrp);
            cuurent=itemView.findViewById(R.id.sendercur);
            other=itemView.findViewById(R.id.senderNONcur);
        }
    }
}
