package com.example.chat;

import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class friendsAdapter extends RecyclerView.Adapter {

    private ArrayList<String> friendList = new ArrayList<>();

    public friendsAdapter(ArrayList<String> friendList) {
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_design_friend,
                parent, false);

        friendViewHolder holder = new friendViewHolder(view);
        view.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();

            Intent i = new Intent(parent.getContext(), friendChat.class);
            i.putExtra("friendUserUid", friendList.get(pos));
            parent.getContext().startActivity(i);
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((friendViewHolder)holder).bind(friendList.get(position));
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public class friendViewHolder extends RecyclerView.ViewHolder{

        private ImageView friendImage;
        private TextView friendName;
        private ProgressBar imageProgress, nameProgress;
        public friendViewHolder(@NonNull View itemView) {
            super(itemView);

            friendImage = itemView.findViewById(R.id.friendImageFriendFragment);
            friendName = itemView.findViewById(R.id.friendNameFriendFragement);
            imageProgress = itemView.findViewById(R.id.progressBarfriendsImageAdapter);
            nameProgress = itemView.findViewById(R.id.progressBarfriendsNameAdapter);
        }

        void bind(String friend){
            friendName.setVisibility(View.INVISIBLE);
            friendImage.setVisibility(View.INVISIBLE);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(
                    "users/"+friend+"/username/"
            );

            reference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    friendName.setText(task.getResult().getValue(String.class));
                    friendName.setVisibility(View.VISIBLE);
                    nameProgress.setVisibility(View.INVISIBLE);
                }
            });


            reference = FirebaseDatabase.getInstance().getReference(
                    "users/"+friend+"/ProfilePic/"
            );

            reference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){

                    Picasso.get().load(task.getResult().getValue(String.class)).fit()
                            .into(friendImage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    imageProgress.setVisibility(View.INVISIBLE);
                                    friendImage.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });
                }
            });
        }
    }



}
