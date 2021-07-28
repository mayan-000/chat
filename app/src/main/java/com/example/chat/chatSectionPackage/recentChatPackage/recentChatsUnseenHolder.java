package com.example.chat.chatSectionPackage.recentChatPackage;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class recentChatsUnseenHolder extends RecyclerView.ViewHolder {
    private TextView nameText, messageText, timeText, unreadText;
    private ImageView profileImage;
    private ProgressBar nameBar, messageBar, timeBar, imageBar;

    public recentChatsUnseenHolder(View itemView) {
        super(itemView);
        nameText = itemView.findViewById(R.id.friendNameRecentUnseen);
        messageText = itemView.findViewById(R.id.friendMessageRecentUnseen);
        timeText = itemView.findViewById(R.id.friendMessageTimeRecentUnseen);
        profileImage = itemView.findViewById(R.id.friendProfilePicRecentUnseen);
        unreadText = itemView.findViewById(R.id.friendUnreadRecentUnseen);
        nameBar = itemView.findViewById(R.id.progressBarNameRecentChatsUnseen);
        messageBar = itemView.findViewById(R.id.progressBarMessageRecentChatsUnseen);
        timeBar = itemView.findViewById(R.id.progressBarTimeRecentChatsUnseen);
        imageBar = itemView.findViewById(R.id.progressBarImageRecentChatsUnseen);

        nameText.setVisibility(View.INVISIBLE);
        messageText.setVisibility(View.INVISIBLE);
        timeText.setVisibility(View.INVISIBLE);
        profileImage.setVisibility(View.INVISIBLE);

    }

    void bind(messageClassNew message, String name, String image){

        messageText.setText(message.getMessage());
        messageText.setVisibility(View.VISIBLE);
        messageBar.setVisibility(View.INVISIBLE);

        timeText.setText(message.getDate()+" "+message.getTime());
        timeText.setVisibility(View.VISIBLE);
        timeBar.setVisibility(View.INVISIBLE);

        nameText.setText(name);
        nameText.setVisibility(View.VISIBLE);
        nameBar.setVisibility(View.INVISIBLE);

        Picasso.get().load(image).fit().into(profileImage, new Callback() {
            @Override
            public void onSuccess() {
                profileImage.setVisibility(View.VISIBLE);
                imageBar.setVisibility(View.INVISIBLE);
                Log.d("msg",image);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        unreadText.setText("+NEW");

    }
}
