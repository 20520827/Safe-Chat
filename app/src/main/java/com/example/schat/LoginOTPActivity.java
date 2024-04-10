package com.example.schat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.schat.utils.AndroidUtil;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoginOTPActivity extends AppCompatActivity {
    String phoneNumber;
    Long timeOut = 60L;
    String verificationCode;
    PhoneAuthProvider.ForceResendingToken fsToken;

    EditText inputOTP;
    Button confirm;
    ProgressBar progressBar;
    TextView resendOTP;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_otpactivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inputOTP = findViewById(R.id.inputOTP);
        confirm = findViewById(R.id.confirm_btn);
        progressBar = findViewById(R.id.progressBar);
        resendOTP = findViewById(R.id.resendOTP);

        phoneNumber = getIntent().getExtras().getString("phone");
        sendOTP(phoneNumber, false);
    }

    void sendOTP(String phoneNumber, boolean isResent){
        setInProgress(true);
        PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(mAuth);
        builder.setPhoneNumber(phoneNumber);
        builder.setTimeout(timeOut, TimeUnit.SECONDS);
        builder.setActivity(this);
        builder.setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                logIn(phoneAuthCredential);
                setInProgress(false);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                AndroidUtil.showToast(getApplicationContext(), "Xác thực OTP thất bại");
                setInProgress(false);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                fsToken = forceResendingToken;
                AndroidUtil.showToast(getApplicationContext(), "Mã OTP đã được gửi thành công");
                setInProgress(false);
            }
        });
        if(isResent){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(fsToken).build());
        }else{
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }

    void logIn(PhoneAuthCredential phoneAuthCredential) {
        //Login then go to next activity
    }

    void setInProgress(boolean isInProgress){
        if(isInProgress) {
            progressBar.setVisibility(View.VISIBLE);
            confirm.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            confirm.setVisibility(View.VISIBLE);
        }
    }
}