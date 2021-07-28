package com.example.chat.chatSectionPackage.recentChatPackage;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class recentChatsSeenHolder extends RecyclerView.ViewHolder {

    private TextView nameText, messageText, timeText;
    private ImageView profileImage;
    private ProgressBar nameBar, imageBar, messageBar, timeBar;

    public recentChatsSeenHolder(View itemView) {
        super(itemView);

        nameText = itemView.findViewById(R.id.friendNameRecentSeen);
        messageText = itemView.findViewById(R.id.friendMessageRecentSeen);
        timeText = itemView.findViewById(R.id.friendMessageTimeRecentSeen);
        profileImage = itemView.findViewById(R.id.friendProfilePicRecentSeen);
        nameBar = itemView.findViewById(R.id.progressBarNameRecentChatsSeen);
        imageBar = itemView.findViewById(R.id.progressBarImageRecentChatsSeen);
        messageBar = itemView.findViewById(R.id.progressBarMessageRecentChatsSeen);
        timeBar = itemView.findViewById(R.id.progressBarTimeRecentChatsSeen);

        nameText.setVisibility(View.INVISIBLE);
        messageText.setVisibility(View.INVISIBLE);
        timeText.setVisibility(View.INVISIBLE);
        profileImage.setVisibility(View.INVISIBLE);

    }

    void bind(messageClassNew message, String name, String image){
//       message, read, time, date, type, Uid

        messageText.setText(message.getMessage());
        messageBar.setVisibility(View.INVISIBLE);
        messageText.setVisibility(View.VISIBLE);

        timeText.setText(message.getDate()+" "+message.getTime());
        timeBar.setVisibility(View.INVISIBLE);
        timeText.setVisibility(View.VISIBLE);

        nameText.setText(name);
        nameText.setVisibility(View.VISIBLE);
        nameBar.setVisibility(View.INVISIBLE);

        Picasso.get().load(image).fit().into(profileImage, new Callback() {
            @Override
            public void onSuccess() {
                profileImage.setVisibility(View.VISIBLE);
                imageBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

}
