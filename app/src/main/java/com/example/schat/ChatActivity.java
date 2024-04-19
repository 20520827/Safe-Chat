package com.example.schat;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schat.adapters.SearchedUsers;
import com.example.schat.adapters.SentMessages;
import com.example.schat.models.ChatMessage;
import com.example.schat.models.ChatRoom;
import com.example.schat.models.User;
import com.example.schat.utils.AndroidUtil;
import com.example.schat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.Arrays;

public class ChatActivity extends AppCompatActivity {
    User otherUser;
    String chatroomId;
    SentMessages cmAdapter;
    ChatRoom cRoom;
    TextView otherUsername;
    RecyclerView rv;
    EditText userInput;
    ImageButton backBtn;
    ImageButton sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        otherUser = AndroidUtil.getUserFromIntent(getIntent());
        chatroomId = FirebaseUtil.getChatRoomId(FirebaseUtil.currentUserId(), otherUser.getUserId());
        otherUsername = findViewById(R.id.other_username);
        rv = findViewById(R.id.message_rv);
        userInput = findViewById(R.id.message_input);
        backBtn = findViewById(R.id.backButton);
        sendBtn = findViewById(R.id.send_btn);

        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });
        sendBtn.setOnClickListener(v -> {
            String message = userInput.getText().toString();
            sendMessage(message);
        });
        otherUsername.setText(otherUser.getUserName());
        generateChatRoom();
        setChatRecyclerView();
    }

    void setChatRecyclerView() {
        Query query = FirebaseUtil.getChatMessages(chatroomId).orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ChatMessage> options = new FirestoreRecyclerOptions.Builder<ChatMessage>().setQuery(query, ChatMessage.class).build();
        cmAdapter = new SentMessages(options, getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        rv.setLayoutManager(manager);
        rv.setAdapter(cmAdapter);
        cmAdapter.startListening();
        cmAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                rv.smoothScrollToPosition(0);
            }
        });
    }

    void sendMessage(String message) {
        cRoom.setLastMessageTimestamp(Timestamp.now());
        cRoom.setLastMessageId(FirebaseUtil.currentUserId());
        FirebaseUtil.getChatRoom(chatroomId).set(cRoom);

        ChatMessage cm = new ChatMessage(message, FirebaseUtil.currentUserId(), Timestamp.now());
        FirebaseUtil.getChatMessages(chatroomId).add(cm).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    userInput.setText("");
                }
            }
        });
    }

    void generateChatRoom() {
        FirebaseUtil.getChatRoom(chatroomId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    cRoom = task.getResult().toObject(ChatRoom.class);
                    if(cRoom == null)//first time chat
                    {
                        cRoom = new ChatRoom(chatroomId, Arrays.asList(FirebaseUtil.currentUserId(), otherUser.getUserId()), Timestamp.now(), "");
                        FirebaseUtil.getChatRoom(chatroomId).set(cRoom);
                    }
                }
            }
        });
    }
}