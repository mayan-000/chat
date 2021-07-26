package com.example.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class friendsFragment extends Fragment {
    private TextView addFriends;
    private RecyclerView friendsList;
    private ProgressBar progressBar;
    private LinearLayout oopiseLayout;
    private ArrayList<String> friendsArrayList = new ArrayList<>();
    private friendsAdapter adapter;
    public friendsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friends,container,false);

        addFriends = view.findViewById(R.id.addFriendsButtonFriendFragment);
        friendsList = view.findViewById(R.id.friendsList);
        friendsList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        progressBar = view.findViewById(R.id.progressBarFriendsFragment);
        oopiseLayout = view.findViewById(R.id.oopsieMessageFriendsFragment);
        friendsList.setVisibility(View.INVISIBLE);
        oopiseLayout.setVisibility(View.INVISIBLE);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(
                "users/"+user.getUid()+"/friends/");


        reference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for (DataSnapshot data : Objects.requireNonNull(task.getResult()).getChildren()) {
                    String friendsUid = data.getKey();
                    friendsArrayList.add(friendsUid);
                }

                adapter = new friendsAdapter(friendsArrayList);

                friendsList.setAdapter(adapter);

                progressBar.setVisibility(View.INVISIBLE);
                if(friendsArrayList.size()==0){
                    oopiseLayout.setVisibility(View.VISIBLE);
                }
                else{
                    friendsList.setVisibility(View.VISIBLE);
                }
            }
        });


        addFriends.setOnClickListener(v -> {

        });



        return view;

    }


    public static friendsFragment getInstance(){
        return new friendsFragment();
    }
}