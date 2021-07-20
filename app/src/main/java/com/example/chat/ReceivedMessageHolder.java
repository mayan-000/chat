package com.example.chat;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class ReceivedMessageHolder extends RecyclerView.ViewHolder{

        private TextView messageText, timeText, dateText;

        public ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_gchat_message_other);
            timeText = itemView.findViewById(R.id.text_gchat_timestamp_other);
            dateText = itemView.findViewById(R.id.text_gchat_datestamp_other);
        }

        void bind(messageClass msg){
            messageText.setText(msg.getMessage());
            timeText.setText(msg.getTime());
            dateText.setText(msg.getDate());
        }
    }

//}
