package com.example.schat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hbb20.CountryCodePicker;

public class PhoneLoginActivity extends AppCompatActivity {
    CountryCodePicker countryCode;
    EditText phoneNumber;
    Button sendOTP;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_phone_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        countryCode = findViewById(R.id.countryCode);
        phoneNumber = findViewById(R.id.phoneNumber);
        sendOTP = findViewById(R.id.sendOTP_btn);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        //Link phoneNumber to countryCode
        countryCode.registerCarrierNumberEditText(phoneNumber);
        sendOTP.setOnClickListener((v)->{
            if(!countryCode.isValidFullNumber())
            {
                phoneNumber.setError("Số di động không hợp lệ!");
                return;
            }
            //Navigate to LoginOTP Activity
            Intent intent = new Intent(PhoneLoginActivity.this, LoginOTPActivity.class);
            //Passing phoneNumber value to LoginOTP Activity
            intent.putExtra("phone", countryCode.getFullNumberWithPlus());
            startActivity(intent);
        });
    }
}