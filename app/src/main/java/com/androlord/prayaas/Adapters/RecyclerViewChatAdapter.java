package com.androlord.prayaas.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androlord.prayaas.DataClass.Messege;
import com.androlord.prayaas.R;
import com.google.android.gms.common.data.SingleRefDataBufferIterator;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewChatAdapter extends RecyclerView.Adapter<RecyclerViewChatAdapter.ChatViewHolder>{
    ArrayList<Messege> messeges=new ArrayList<Messege>();
    Context context;
    public RecyclerViewChatAdapter(Context context,ArrayList<Messege> messeges)
    {
        this.messeges=messeges;
        this.context=context;

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
        if(messeges.get(i).messege.trim().indexOf("LOCATION:-")==0)
        {
            final Double lat,lon;
            String m =messeges.get(i).messege.trim();
            lat=Double.parseDouble(m.substring(m.indexOf(":-")+2,m.indexOf("||")));
            lon=Double.parseDouble(m.substring(m.indexOf("||")+2,m.length()));
            Log.d("ak47",lat+" "+lon);
            chatViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lon + " ("+")";

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                    context.startActivity(intent);
                }
            });


        }


    }

    @Override
    public int getItemCount() {
        Log.d("ak47", "getItemCount: "+messeges.size());
        return messeges.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView sent,recived;
        LinearLayout linearLayout;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            sent=itemView.findViewById(R.id.sendMessegeBubble);
            recived=itemView.findViewById(R.id.recivedMessegeBubble);
            linearLayout=itemView.findViewById(R.id.ChatBubbleContainer);
        }
    }
}
