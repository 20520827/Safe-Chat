package com.example.schat.adapters;

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

import com.example.schat.ChatActivity;
import com.example.schat.R;
import com.example.schat.models.ChatMessage;
import com.example.schat.utils.AndroidUtil;
import com.example.schat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import org.w3c.dom.Text;

public class SentMessages extends FirestoreRecyclerAdapter<ChatMessage, SentMessages.ChatModelView> {
    Context context;
    public SentMessages(@NonNull FirestoreRecyclerOptions<ChatMessage> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatModelView ChatModelView, int i, @NonNull ChatMessage ChatMessage) {
        if(ChatMessage.getSenderId().equals(FirebaseUtil.currentUserId())){
            ChatModelView.lChat.setVisibility(View.GONE);
            ChatModelView.rChat.setVisibility(View.VISIBLE);
            ChatModelView.rightChat.setText(ChatMessage.getMessage());
        }else{
            ChatModelView.rChat.setVisibility(View.GONE);
            ChatModelView.lChat.setVisibility(View.VISIBLE);
            ChatModelView.leftChat.setText(ChatMessage.getMessage());
        }
    }

    @NonNull
    @Override
    public ChatModelView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_message, parent,false);
        return new ChatModelView(view);
    }

    class ChatModelView extends RecyclerView.ViewHolder{
        LinearLayout lChat, rChat;
        TextView leftChat, rightChat;
        public ChatModelView(@NonNull View itemView) {
            super(itemView);
            lChat = itemView.findViewById(R.id.lchat_layout);
            rChat = itemView.findViewById(R.id.rchat_layout);
            leftChat = itemView.findViewById(R.id.left_chat);
            rightChat = itemView.findViewById(R.id.right_chat);
        }
    }
}
