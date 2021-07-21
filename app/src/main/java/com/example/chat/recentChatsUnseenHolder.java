package com.example.chat;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class recentChatsUnseenHolder extends RecyclerView.ViewHolder {
    private TextView nameText, messageText, timeText, unreadText;
    private ImageView profileImage;


    public recentChatsUnseenHolder(View itemView) {
        super(itemView);
        nameText = itemView.findViewById(R.id.friendNameRecentUnseen);
        messageText = itemView.findViewById(R.id.friendMessageRecentUnseen);
        timeText = itemView.findViewById(R.id.friendMessageTimeRecentUnseen);
        profileImage = itemView.findViewById(R.id.friendProfilePicRecentUnseen);
        unreadText = itemView.findViewById(R.id.friendUnreadRecentUnseen);
    }

    void bind(messageClassNew message){

        messageText.setText(message.getMessage());
        timeText.setText(message.getDate()+" "+message.getTime());


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(
                "users/"+message.getUid()+"/username/");

        reference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                nameText.setText(task.getResult().getValue(String.class));
            }
        });

        reference = FirebaseDatabase.getInstance().getReference(
                "users/"+message.getUid()+"/ProfilePic/");

        reference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Picasso.get().load(task.getResult().getValue(String.class))
                        .into(profileImage);
            }
        });

        unreadText.setText("+NEW");

    }
}
