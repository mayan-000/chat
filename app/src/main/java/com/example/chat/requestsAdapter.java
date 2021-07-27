package com.example.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class requestsAdapter extends RecyclerView.Adapter {
    private ArrayList<String> requestsArrayList;

    public requestsAdapter(ArrayList<String> requestsArrayList) {
        this.requestsArrayList = requestsArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new requestsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_design_requests, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((requestsViewHolder) holder).bind(requestsArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return requestsArrayList.size();
    }

    public void clear(){
        requestsArrayList.clear();
        notifyDataSetChanged();
    }

    public class requestsViewHolder extends RecyclerView.ViewHolder{

        private ImageView requestImage;
        private TextView requestName;
        private ProgressBar progressBarName, progressBarImage;
        private Button acceptButton;


        public requestsViewHolder(@NonNull View itemView) {
            super(itemView);

            requestImage = itemView.findViewById(R.id.friendImageRequestsFragment);
            requestName = itemView.findViewById(R.id.friendNameRequestsFragment);
            progressBarImage = itemView.findViewById(R.id.progressBarImageRequestsFragment);
            progressBarName = itemView.findViewById(R.id.progressBarNameRequestsFragment);
            acceptButton = itemView.findViewById(R.id.acceptButtonRequestsFragment);

            requestName.setVisibility(View.INVISIBLE);
            requestImage.setVisibility(View.INVISIBLE);

        }

        void bind(String uid){
            acceptButton.setText("Accept");
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(
                    "users/"+uid+"/username/"
            );

            reference.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    requestName.setText(task.getResult().getValue(String.class));
                    progressBarName.setVisibility(View.INVISIBLE);
                    requestName.setVisibility(View.VISIBLE);
                }
            });

            reference = FirebaseDatabase.getInstance().getReference(
                    "users/"+uid+"/ProfilePic/"
            );

            reference.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Picasso.get().load(task.getResult().getValue(String.class)).fit()
                            .into(requestImage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    progressBarImage.setVisibility(View.INVISIBLE);
                                    requestImage.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });
                }
            });

            acceptButton.setOnClickListener(v -> {
                acceptButton.setText("Accepted");

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference(
                        "users/"+user.getUid()+"/requestsReceived/"
                );

                ref.removeValue();

                ref = FirebaseDatabase.getInstance().getReference(
                        "users/"+uid+"/requestsSent/"
                );

                ref.removeValue();


                ref = FirebaseDatabase.getInstance().getReference(
                        "users/"+user.getUid()+"/friends/"
                );

                ref.child(uid).setValue(true);


                ref = FirebaseDatabase.getInstance().getReference(
                        "users/"+uid+"/friends/"
                );

                ref.child(user.getUid()).setValue(true);


            });

        }

    }
}
