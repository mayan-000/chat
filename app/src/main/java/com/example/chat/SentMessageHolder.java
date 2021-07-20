package com.example.chat;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class SentMessageHolder extends RecyclerView.ViewHolder {
    TextView messageText, timeText, dateText;

    public SentMessageHolder(View itemView) {
        super(itemView);

        messageText = itemView.findViewById(R.id.text_gchat_message_me);
        timeText = itemView.findViewById(R.id.text_gchat_timestamp_me);
        dateText = itemView.findViewById(R.id.text_gchat_datestamp_me);
    }

    void bind(messageClass message) {
        messageText.setText(message.getMessage());
        timeText.setText(message.getTime());
        dateText.setText(message.getDate());
    }
}
