package com.example.chat.friendChatPackage;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.R;


public class ReceivedMessageHolder extends RecyclerView.ViewHolder{

        private TextView messageText, timeText, dateText;

        public ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_gchat_message_other);
            timeText = itemView.findViewById(R.id.text_gchat_timestamp_other);
            dateText = itemView.findViewById(R.id.text_gchat_datestamp_other);
        }

        public void bind(messageClass msg){
            messageText.setText(msg.getMessage());
            timeText.setText(msg.getTime());
            dateText.setText(msg.getDate());
        }
    }

//}
