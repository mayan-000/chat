package com.example.chat;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class requestsFragment extends Fragment {

    private RecyclerView requestsList;
    private ProgressBar progressBar;
    private TextView noRequests;
    private SwipeRefreshLayout swipe;
    private ArrayList<String> requestsArrayList = new ArrayList<>();
    private requestsAdapter adapter;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public requestsFragment() {}



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requests, container, false);

        requestsList = view.findViewById(R.id.requestsList);
        progressBar = view.findViewById(R.id.progressBarRequestsFragment);
        noRequests = view.findViewById(R.id.noRequests);
        swipe = view.findViewById(R.id.swipeRequestsFragment);

        requestsList.setLayoutManager(new LinearLayoutManager(getContext()));
        requestsList.setVisibility(View.INVISIBLE);
        noRequests.setVisibility(View.INVISIBLE);

        adapter = new requestsAdapter(requestsArrayList);
        requestsList.setAdapter(adapter);


        loadRequests();

        swipe.setOnRefreshListener(() -> {
            requestsList.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            noRequests.setVisibility(View.INVISIBLE);
            adapter.clear();

            loadRequests();
        });

        swipe.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        return view;
    }

    private void loadRequests(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(
                "users/"+user.getUid()+"/requestsReceived/"
        );


        reference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for (DataSnapshot data : task.getResult().getChildren()) {
                    requestsArrayList.add(data.getValue(String.class));
                }


                progressBar.setVisibility(View.INVISIBLE);
                if(requestsArrayList.size()==0){
                    noRequests.setVisibility(View.VISIBLE);
                }
                else{
                    requestsList.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();

                swipe.setRefreshing(false);
            }
        });
    }

    public static requestsFragment getInstance(){
        return new requestsFragment();
    }
}