package com.example.schat.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.schat.models.User;

public class AndroidUtil {
    public static void showToast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
    public static void passUserAsIntent(Intent intent, User user) {
        intent.putExtra("userName", user.getUserName());
        intent.putExtra("phoneNumber", user.getPhoneNumber());
        intent.putExtra("userId", user.getUserId());
    }
    public static User getUserFromIntent(Intent intent){
        User user = new User();
        user.setUserName(intent.getStringExtra("userName"));
        user.setPhoneNumber(intent.getStringExtra("phoneNumber"));
        user.setUserId(intent.getStringExtra("userId"));
        return user;
    }
}
