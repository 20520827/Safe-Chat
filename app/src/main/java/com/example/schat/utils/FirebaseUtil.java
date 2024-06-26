package com.example.schat.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtil {
    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }
    public static boolean isLoggedIn(){
        return (currentUserId() != null);
    }
    public static DocumentReference currentUserDetail(){
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }
    public static CollectionReference allUsersRef(){
        return FirebaseFirestore.getInstance().collection("users");
    }
    public static DocumentReference getChatRoom(String chatroomId){
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId);
    }
    public static CollectionReference getChatMessages(String chatroomId){
        return getChatRoom(chatroomId).collection("chats");
    }
    public static String getChatRoomId(String userId1, String userId2) {
        if(userId1.hashCode() < userId2.hashCode()) {
            return userId1 + "_" + userId2;
        }else {
            return userId2 + "_" + userId1;
        }
    }
}
