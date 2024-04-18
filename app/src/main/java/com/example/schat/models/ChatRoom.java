package com.example.schat.models;
import com.google.firebase.Timestamp;

import java.util.List;

public class ChatRoom {
    String id;
    List<String> userIds;
    Timestamp lastMessageTimestamp;
    String lastMessageId;

    public ChatRoom() {
    }
    public ChatRoom(String id, List<String> userIds, Timestamp lastMessageTimestamp, String lastMessageId) {
        this.id = id;
        this.userIds = userIds;
        this.lastMessageTimestamp = lastMessageTimestamp;
        this.lastMessageId = lastMessageId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public Timestamp getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(Timestamp lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public String getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(String lastMessageId) {
        this.lastMessageId = lastMessageId;
    }
}
