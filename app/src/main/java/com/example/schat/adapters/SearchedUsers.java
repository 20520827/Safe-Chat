package com.example.schat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schat.R;
import com.example.schat.models.User;
import com.example.schat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import org.w3c.dom.Text;

public class SearchedUsers extends FirestoreRecyclerAdapter<User, SearchedUsers.UserModelView> {
    Context context;
    public SearchedUsers(@NonNull FirestoreRecyclerOptions<User> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelView userModelView, int i, @NonNull User user) {
        userModelView.username.setText(user.getUserName());
        userModelView.phonenumber.setText(user.getPhoneNumber());
        if(user.getUserId().equals(FirebaseUtil.currentUserId()))
        {
            userModelView.username.setText(user.getUserName() + " (Tôi)");
        }

        userModelView.itemView.setOnClickListener(v -> {
            //Navigate to chat activity
        });
    }

    @NonNull
    @Override
    public UserModelView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.searched_users_result, parent,false);
        return new UserModelView(view);
    }

    class UserModelView extends RecyclerView.ViewHolder{
        TextView username;
        TextView phonenumber;
        ImageView profilePic;
        public UserModelView(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.searched_username);
            phonenumber = itemView.findViewById(R.id.searched_phonenumber);
            profilePic = itemView.findViewById(R.id.profile_pic);
        }
    }
}
