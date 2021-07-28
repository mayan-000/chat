package com.example.chat.chatSectionPackage.recentChatPackage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.chat.R;
import com.example.chat.friendChatPackage.friendChat;
import com.example.chat.friendChatPackage.messageClass;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;


public class recentChatsFragment extends Fragment {
    private RecyclerView recentChats;
    private LinearLayout oopsieMessage;
    private FirebaseRecyclerAdapter<messageClassNew, RecyclerView.ViewHolder> adapter;
    private ArrayList<messageClassNew> recents = new ArrayList<>();
    private FirebaseUser user;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_recent_chats, container, false);
        recentChats = view.findViewById(R.id.recentChats);
        oopsieMessage = view.findViewById(R.id.oopsieMessageRecentChatsFragment);
        user = FirebaseAuth.getInstance().getCurrentUser();

        recentChats.setVisibility(View.INVISIBLE);
        oopsieMessage.setVisibility(View.INVISIBLE);

////////////////////
        Query query = FirebaseDatabase.getInstance().getReference("users/"+user.getUid()
                +"/LastMessages/");


        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                messageClass msg1 = snapshot.getValue(messageClass.class);
                msg1.decrypt();
                messageClassNew msg = new messageClassNew(msg1);
                msg.setUid(snapshot.getKey());

                recents.add(msg);

            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
                String uid = snapshot.getKey();
                messageClass msg1 = snapshot.getValue(messageClass.class);
                msg1.decrypt();
                messageClassNew msg = new messageClassNew(msg1);
                msg.setUid(uid);

                for (messageClassNew m : recents) {
                    if(uid.equalsIgnoreCase(m.getUid())){
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
            protected void onBindViewHolder(RecyclerView.ViewHolder holder, int position,
                                            messageClassNew model) {

                messageClassNew msg = recents.get(position);

                final String[] nameString = {""};
                final String[] imageString = {""};
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(
                        "users/"+recents.get(position).getUid()+"/username/");

                reference.get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        nameString[0] = task.getResult().getValue(String.class);
                    }
                });

                reference = FirebaseDatabase.getInstance().getReference(
                        "users/"+recents.get(position).getUid()+"/ProfilePic/");

                reference.get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        imageString[0] = task.getResult().getValue(String.class);

                        if(recents.get(position).getRead()==1){
                            ((recentChatsSeenHolder) holder).bind(msg, nameString[0], imageString[0]);
                        }
                        else{
                            ((recentChatsUnseenHolder) holder).bind(msg, nameString[0], imageString[0]);
                        }

                    }
                });


            }

            @Override
            public int getItemCount() {
                if(recents.size()==0){
                    oopsieMessage.setVisibility(View.VISIBLE);
                }
                else{
                    oopsieMessage.setVisibility(View.INVISIBLE);
                    recentChats.setVisibility(View.VISIBLE);
                }
                return recents.size();
            }

            @Override
            public int getItemViewType(int position) {
                return recents.get(position).getRead();
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                Collections.sort(recents);
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
    public void onDestroyView() {
        super.onDestroyView();
        adapter.stopListening();
    }


    public static recentChatsFragment getInstance(){
        return new recentChatsFragment();
    }

}




