package com.example.chat.addFriendsPackage.suggestionFragmentPackage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class suggestedFriendsAdapter extends RecyclerView.Adapter {
    private ArrayList<String> suggestedList;

    public suggestedFriendsAdapter(ArrayList<String> suggestedList) {
        this.suggestedList = suggestedList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_design_suggested,
                parent, false);

        return new suggestedFriendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((suggestedFriendsViewHolder) holder).bind(suggestedList.get(position));
    }

    @Override
    public int getItemCount() {
        return suggestedList.size();
    }

    public void clear(){
        suggestedList.clear();
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<String> list){
        suggestedList.addAll(list);
        notifyDataSetChanged();
    }



    public class suggestedFriendsViewHolder extends RecyclerView.ViewHolder{
        private ImageView suggestedImage;
        private TextView suggestedName;
        private ProgressBar progressBarImage, progressBarName;
        private Button addButton;

        public suggestedFriendsViewHolder(@NonNull View itemView) {
            super(itemView);

            suggestedImage = itemView.findViewById(R.id.friendImageAddFriendFragment);
            suggestedName = itemView.findViewById(R.id.friendNameAddFriendFragment);
            progressBarImage = itemView.findViewById(R.id.progressBarAddFriendImage);
            progressBarName = itemView.findViewById(R.id.progressBarAddFriendName);
            addButton = itemView.findViewById(R.id.addButtonSuggestedFragment);

            suggestedName.setVisibility(View.INVISIBLE);
            suggestedImage.setVisibility(View.INVISIBLE);
        }

        void bind(String uid){
            addButton.setText("ADD");

            final DatabaseReference[] reference = {FirebaseDatabase.getInstance().getReference(
                    "users/" + uid + "/username/"
            )};

            reference[0].get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    suggestedName.setText(task.getResult().getValue(String.class));
                    progressBarName.setVisibility(View.INVISIBLE);
                    suggestedName.setVisibility(View.VISIBLE);
                }
            });

            reference[0] = FirebaseDatabase.getInstance().getReference(
                    "users/"+uid+"/ProfilePic/"
            );

            reference[0].get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Picasso.get().load(task.getResult().getValue(String.class)).fit()
                            .into(suggestedImage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    progressBarImage.setVisibility(View.INVISIBLE);
                                    suggestedImage.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });


                }
            });

            addButton.setOnClickListener(v -> {
                addButton.setClickable(false);
                addButton.setText("SENT");


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                reference[0] = FirebaseDatabase.getInstance().getReference(
                        "users/" + user.getUid() + "/requestsSent/"
                );

                reference[0] = reference[0].push();

                String[] path = reference[0].toString().split("/");

                reference[0].setValue(uid);

                reference[0] = FirebaseDatabase.getInstance().getReference(
                        "users/" + uid + "/requestsReceived/"+path[path.length-1]+"/"
                );

                reference[0].setValue(user.getUid());

            });

        }
    }
}
