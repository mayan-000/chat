package com.example.chat.chatSectionPackage.friendsFragmentPackage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.addFriendsPackage.addFriendsActivity;
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
    private SwipeRefreshLayout swipe;
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
        adapter = new friendsAdapter(friendsArrayList);

        friendsList.setAdapter(adapter);

        progressBar = view.findViewById(R.id.progressBarFriendsFragment);
        oopiseLayout = view.findViewById(R.id.oopsieMessageFriendsFragment);
        swipe = view.findViewById(R.id.swipeFriendFragment);
        friendsList.setVisibility(View.INVISIBLE);
        oopiseLayout.setVisibility(View.INVISIBLE);

        setFriendsList();

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                friendsArrayList.clear();
                setFriendsList();
            }
        });

        swipe.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        addFriends.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), addFriendsActivity.class));
        });



        return view;

    }

    void setFriendsList(){
        friendsList.setVisibility(View.INVISIBLE);
        oopiseLayout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(
                "users/"+user.getUid()+"/friends/");


        reference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for (DataSnapshot data : Objects.requireNonNull(task.getResult()).getChildren()) {
                    String friendsUid = data.getKey();
                    friendsArrayList.add(friendsUid);
                }

                adapter.notifyDataSetChanged();
                swipe.setRefreshing(false);

                progressBar.setVisibility(View.INVISIBLE);
                if(friendsArrayList.size()==0){
                    oopiseLayout.setVisibility(View.VISIBLE);
                }
                else{
                    friendsList.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    public static friendsFragment getInstance(){
        return new friendsFragment();
    }
}