package com.example.schat;

import android.content.Intent;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
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

        confirm.setOnClickListener(v -> {
            String enteredOTP = inputOTP.getText().toString();
            PhoneAuthCredential paCred = PhoneAuthProvider.getCredential(verificationCode, enteredOTP);
            logIn(paCred);
            setInProgress(true);
        });

        resendOTP.setOnClickListener(v -> {
            sendOTP(phoneNumber, true);
        });
    }

    void sendOTP(String phoneNumber, boolean isResent){
        startTimeOutTimer();
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

    void startTimeOutTimer() {
        resendOTP.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeOut--;
                resendOTP.setText("Gửi lại mã OTP sau " + timeOut +" giây.");
                if(timeOut <= 0)
                {
                    timeOut = 60L;
                    timer.cancel();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resendOTP.setEnabled(true);
                        }
                    });
                }
            }
        }, 0, 1000);
    }

    void logIn(PhoneAuthCredential phoneAuthCredential) {
        //Login then go to next activity
        setInProgress(true);
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                    Intent intent = new Intent(LoginOTPActivity.this, UsernameLoginActivity.class);
                    intent.putExtra("phone", phoneNumber);
                    startActivity(intent);
                }else {
                    AndroidUtil.showToast(getApplicationContext(), "Mã OTP không đúng");
                }
            }
        });
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