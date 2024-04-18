package com.example.schat;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schat.models.User;
import com.example.schat.utils.AndroidUtil;

public class ChatActivity extends AppCompatActivity {
    User otherUser;
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
        otherUsername = findViewById(R.id.other_username);
        rv = findViewById(R.id.message_rv);
        userInput = findViewById(R.id.message_input);
        backBtn = findViewById(R.id.backButton);
        sendBtn = findViewById(R.id.send_btn);

        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        otherUsername.setText(otherUser.getUserName());
    }
}