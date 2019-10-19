package com.androlord.prayaas.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androlord.prayaas.DataClass.Messege;
import com.androlord.prayaas.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewChatAdapter extends RecyclerView.Adapter<RecyclerViewChatAdapter.ChatViewHolder>{
    ArrayList<Messege> messeges=new ArrayList<Messege>();
    public RecyclerViewChatAdapter(ArrayList<Messege> messeges)
    {
        this.messeges=messeges;

    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d("ak47", "onCreateViewHolder: chatadapter");
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_chat_messege,viewGroup,false);
        ChatViewHolder chatViewHolder=new ChatViewHolder(v);
        return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder chatViewHolder, int i) {
        Log.d("ak47", "onBindViewHolder: "+i);
        chatViewHolder.recived.setText(messeges.get(i).messege);
        chatViewHolder.sent.setText(messeges.get(i).messege);
        if (messeges.get(i).sent)
            chatViewHolder.recived.setVisibility(View.GONE);
        else
            chatViewHolder.sent.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        Log.d("ak47", "getItemCount: "+messeges.size());
        return messeges.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView sent,recived;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            sent=itemView.findViewById(R.id.sendMessegeBubble);
            recived=itemView.findViewById(R.id.recivedMessegeBubble);
        }
    }
}
