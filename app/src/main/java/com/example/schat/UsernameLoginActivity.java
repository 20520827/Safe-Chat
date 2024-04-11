package com.example.schat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.schat.models.User;
import com.example.schat.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

public class UsernameLoginActivity extends AppCompatActivity {
    String phoneNumber;
    User user;

    EditText inputUsername;
    Button logIn;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_username_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        inputUsername = findViewById(R.id.inputUsername);
        logIn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progressBar);

        phoneNumber = getIntent().getExtras().getString("phone");
        getUsername();
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUsername();
            }
        });
    }
    //Get username from firebase.
    void getUsername(){
        setInProgress(true);
        FirebaseUtil.currentUserDetail().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                    user = task.getResult().toObject(User.class);
                    if(user != null){
                        inputUsername.setText(user.getUserName());
                    }
                }
            }
        });
    }

    void setUsername()
    {
        String username = inputUsername.getText().toString();
        if(username.isEmpty() || username.length() < 3)
        {
            inputUsername.setError("Tên người dùng phải chứa ít nhất 3 kí tự");
            return;
        }
        setInProgress(true);

        if(user != null){
            //Update username if user is already in firebase's collection
            user.setUserName(username);
        }else {
            //User is not in firebase's collection add to collection
            user = new User(phoneNumber, username, Timestamp.now());
        }
        //Navigate to main activity
        FirebaseUtil.currentUserDetail().set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setInProgress(false);
                if(task.isSuccessful()) {
                    Intent intent = new Intent(UsernameLoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    void setInProgress(boolean isInProgress){
        if(isInProgress) {
            progressBar.setVisibility(View.VISIBLE);
            logIn.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            logIn.setVisibility(View.VISIBLE);
        }
    }
}