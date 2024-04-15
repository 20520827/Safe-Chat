package com.example.schat;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

public class SearchUserActivity extends AppCompatActivity {
    EditText searchInput;
    ImageButton backBtn;
    ImageButton searchBtn;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        searchInput = findViewById(R.id.search_input);
        backBtn = findViewById(R.id.backButton);
        searchBtn = findViewById(R.id.search_user_btn);
        rv = findViewById(R.id.search_user_rv);

        searchInput.requestFocus();
        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        searchBtn.setOnClickListener(v->{
            String searchTerm = searchInput.getText().toString();
            if(searchTerm.isEmpty() || searchTerm.length() < 3) {
                searchInput.setError("Tên người dùng không hợp lệ");
                return;
            }
            setSearchRecycleView(searchTerm);
        });

        
    }

    void setSearchRecycleView(String searchTerm) {
    }
}