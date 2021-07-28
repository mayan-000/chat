package com.example.chat.addFriendsPackage.suggestionFragmentPackage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;


public class suggestedFriendsFragment extends Fragment {

    private RecyclerView suggestedPeopleList;
    private ProgressBar progressBarRecycler;
    private TextView nosuggestion;
    private SwipeRefreshLayout swipe;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ArrayList<String> suggestedList = new ArrayList<>();
    private HashMap<String, Boolean> userFriends = new HashMap<>();
    private suggestedFriendsAdapter adapter;

    public suggestedFriendsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suggested_friends, container, false);

        suggestedPeopleList = view.findViewById(R.id.suggestedPeopleList);
        progressBarRecycler = view.findViewById(R.id.progressBarSuggestedFragment);
        nosuggestion = view.findViewById(R.id.noSuggestionTextView);
        swipe = view.findViewById(R.id.swipeSuggestedFriends);
        suggestedPeopleList.setLayoutManager(new LinearLayoutManager(view.getContext()));

        adapter = new suggestedFriendsAdapter(suggestedList);
        suggestedPeopleList.setAdapter(adapter);

        suggestedPeopleList.setVisibility(View.INVISIBLE);
        nosuggestion.setVisibility(View.INVISIBLE);

        userFriendsFun();

        swipe.setOnRefreshListener(() -> {

            suggestedPeopleList.setVisibility(View.INVISIBLE);
            progressBarRecycler.setVisibility(View.VISIBLE);
            nosuggestion.setVisibility(View.INVISIBLE);
            adapter.clear();
            userFriends.clear();


            userFriendsFun();


        });

        swipe.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;
    }


    void userFriendsFun(){

        userFriends.put(user.getUid(), true);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(
                "users/"+user.getUid()+"/friends/"
        );

        reference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for (DataSnapshot data: task.getResult().getChildren()) {
                    userFriends.put(data.getKey(), true);
                }
            }
        });

        reference = FirebaseDatabase.getInstance().getReference(
                "users/"+user.getUid()+"/requestsSent/"
        );

        reference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for (DataSnapshot data: task.getResult().getChildren()) {
                    userFriends.put(data.getValue(String.class), true);
                }
            }
        });


        reference = FirebaseDatabase.getInstance().getReference(
                "users/"+user.getUid()+"/requestsReceived/"
        );

        reference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for (DataSnapshot data: task.getResult().getChildren()) {
                    userFriends.put(data.getValue(String.class), true);
                }
            }
        });


        reference = FirebaseDatabase.getInstance().getReference(
                "users/"
        );

        reference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for (DataSnapshot data: task.getResult().getChildren()) {
                    if (userFriends.containsKey(data.getKey())) ;
                    else {
                        suggestedList.add(data.getKey());
                    }
                }

                progressBarRecycler.setVisibility(View.INVISIBLE);
                if(suggestedList.size()==0){
                    nosuggestion.setVisibility(View.VISIBLE);
                }
                else{
                    suggestedPeopleList.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
                swipe.setRefreshing(false);

            }


        });



    }

    public static suggestedFriendsFragment getInstance(){
        return new suggestedFriendsFragment();
    }
}
