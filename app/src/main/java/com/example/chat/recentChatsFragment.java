package com.example.chat;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Objects;


public class recentChatsFragment extends Fragment {
    private RecyclerView recentChats;
    private FirebaseRecyclerAdapter<messageClassNew, RecyclerView.ViewHolder> adapter;
    private ArrayList<messageClassNew> recents = new ArrayList<>();
    private FirebaseUser user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recent_chats, container, false);
        recentChats = view.findViewById(R.id.recentChats);

        user = FirebaseAuth.getInstance().getCurrentUser();

////////////////////
        Query query = FirebaseDatabase.getInstance().getReference().child("users/"+user.getUid()
                +"/LastMessages/");


        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                messageClassNew msg = snapshot.getValue(messageClassNew.class);
                recents.add(msg);
//                Log.d("msg",msg.getMessage());
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
                messageClassNew msg = snapshot.getValue(messageClassNew.class);
                for (messageClassNew m : recents) {
                    if(msg.getUid().equalsIgnoreCase(m.getUid())){
                        int pos = recents.indexOf(m);
                        recents.set(pos, msg);
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {}

            @Override
            public void onCancelled(DatabaseError error) {}
        });


        FirebaseRecyclerOptions<messageClassNew> options =
                new FirebaseRecyclerOptions.Builder<messageClassNew>()
                        .setQuery(query, messageClassNew.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<messageClassNew, RecyclerView.ViewHolder>(options) {

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                if(viewType==1){
                    View view = LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.recent_chats_seen, parent,false);

                    recentChatsSeenHolder holder = new recentChatsSeenHolder(view);
                    view.setOnClickListener(v -> {
                        int pos = holder.getAdapterPosition();
                        Intent i = new Intent(getContext(), friendChat.class);
                        i.putExtra("friendUserUid",recents.get(pos).getUid());
                        startActivity(i);
                    });
                    return holder;
                }
                else{
                    View view = LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.recent_chats_unseen, parent,false);

                    recentChatsUnseenHolder holder = new recentChatsUnseenHolder(view);
                    view.setOnClickListener(v -> {
                        int pos = holder.getAdapterPosition();
                        Intent i = new Intent(getContext(), friendChat.class);
                        i.putExtra("friendUserUid",recents.get(pos).getUid());
                        startActivity(i);
                    });
                    return holder;
                }
            }

            @Override
            protected void onBindViewHolder(RecyclerView.ViewHolder holder, int position, messageClassNew model) {
                messageClassNew msg = recents.get(position);
                if(recents.get(position).getRead()==1){
                    ((recentChatsSeenHolder) holder).bind(msg);
                }
                else{
                    ((recentChatsUnseenHolder) holder).bind(msg);
                }
            }

            @Override
            public int getItemCount() {
                return recents.size();
            }

            @Override
            public int getItemViewType(int position) {
                if(recents.get(position).getRead()==1){
                    return 1;
                }
                else return 2;
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                messageClassNew.sort(recents);
                notifyDataSetChanged();
            }
        };

        recentChats.setLayoutManager(new LinearLayoutManager(getContext()));
        recentChats.setAdapter(adapter);

////////////////////////


        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}




