package com.example.chat.friendChatPackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.chatSectionPackage.chatSectionActivity;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class friendChat extends AppCompatActivity {

    private ImageView friendProfilePic;
    private TextView friendName;
    private EditText messageToSend;
    private RecyclerView messageList;
    private ProgressBar nameProgress, imageProgress, messagesProgress;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();
    private String friendUserUid;
    private ArrayList<messageClass> messages = new ArrayList<>();
    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_chat);

        getId();


        friendUserUid = getIntent().getStringExtra("friendUserUid");



        loadMessages();
        setFriendImageAndName();


        messageToSend.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                    (actionId == EditorInfo.IME_ACTION_DONE)) {
                NewMessageSent();
            }
            return false;
        });

    }


    private void getId(){
        friendProfilePic = findViewById(R.id.friendProfilePicChat);
        friendName = findViewById(R.id.friendNameChat);
        messageToSend = findViewById(R.id.MessageToSend);
        messageList = findViewById(R.id.messageList);
        nameProgress = findViewById(R.id.progressBarNameFriendChat);
        imageProgress = findViewById(R.id.progressBarImageFriendChat);
        messagesProgress = findViewById(R.id.progressBarChatFriendChat);

        friendProfilePic.setVisibility(View.INVISIBLE);
        friendName.setVisibility(View.INVISIBLE);
        messageList.setVisibility(View.INVISIBLE);
    }

    private void loadMessages(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        Query query = database.getReference().child("users/"+
                user.getUid()+"/friends/"+friendUserUid+"/messages/");

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                messageClass msg = snapshot.getValue(messageClass.class);
                msg.setRead(1);

                resetRead(msg, snapshot.getKey());


                messages.add(msg);


            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {}

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {}

            @Override
            public void onCancelled(DatabaseError error) {}
        });


        FirebaseRecyclerOptions<messageClass> options =
                new FirebaseRecyclerOptions.Builder<messageClass>()
                        .setQuery(query, messageClass.class)
                        .build();



        adapter = new FirebaseRecyclerAdapter<messageClass, RecyclerView.ViewHolder>(options) {

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                if(viewType==1){
                    return new SentMessageHolder(LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.send_message_design, parent,false));
                }
                else{
                    return new ReceivedMessageHolder(LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.receive_message_design, parent,false));
                }
            }

            @Override
            protected void onBindViewHolder(RecyclerView.ViewHolder holder, int position, messageClass model) {
                messageClass msg = messages.get(position);
                if(messages.get(position).getType().equalsIgnoreCase("send")){
                    ((SentMessageHolder) holder).bind(msg);
                }
                else{
                    ((ReceivedMessageHolder) holder).bind(msg);
                }
            }

            @Override
            public int getItemCount() {
                messagesProgress.setVisibility(View.INVISIBLE);
                messageList.setVisibility(View.VISIBLE);

                return messages.size();
            }

            @Override
            public int getItemViewType(int position) {
                if(messages.get(position).getType().equalsIgnoreCase("send")){
                    return 1;
                }
                else return 2;
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
//                addDates();
                notifyDataSetChanged();
                messageList.smoothScrollToPosition(messages.size());
            }
        };

        messageList.setLayoutManager(new LinearLayoutManager(this));
        messageList.setAdapter(adapter);
        messageList.smoothScrollToPosition(messages.size());


    }

    private void setFriendImageAndName(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("users/"+
                friendUserUid+"/username");

        reference1.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                friendName.setText(Objects.requireNonNull(task.getResult()).getValue(String.class));
                friendName.setVisibility(View.VISIBLE);
                nameProgress.setVisibility(View.INVISIBLE);
                String pic = "https://avatars.dicebear.com/api/initials/"+friendName.getText().toString()+".svg";

                Picasso.get().load(pic).fit()
                    .into(friendProfilePic, new Callback() {
                        @Override
                        public void onSuccess() {
                            imageProgress.setVisibility(View.INVISIBLE);
                            friendProfilePic.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
            }
        });
    }


    private void NewMessageSent(){
        String MessageToSend = messageToSend.getText().toString();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        String [] dateTime = dtf.format(now).split(" ",2);

        messageClass NewMsg = new messageClass(dateTime[0],dateTime[1],MessageToSend,"send",1),
                NewMsg2 = new messageClass(dateTime[0],dateTime[1],MessageToSend,"receive",0);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/"+
                user.getUid()+"/friends/"+friendUserUid+"/messages/");
        reference = reference.push();
        String [] st = reference.toString().split("/");

        reference.setValue(NewMsg);

        reference = FirebaseDatabase.getInstance().getReference("users/"+
                friendUserUid+"/friends/"+user.getUid()+"/messages/"+st[st.length-1]);
        reference.setValue(NewMsg2);

        reference = FirebaseDatabase.getInstance().getReference("users/"+
                user.getUid()+"/LastMessages/"+friendUserUid+"/");
        reference.setValue(NewMsg);

        reference = FirebaseDatabase.getInstance().getReference("users/"+
                friendUserUid+"/LastMessages/"+user.getUid()+"/");
        reference.setValue(NewMsg2);

        messageToSend.setText("");

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, chatSectionActivity.class));
    }


    void resetRead(messageClass msg, String uid){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/"+
                user.getUid()+"/friends/"+friendUserUid+"/messages/"+
                uid);
        ref.setValue(msg);

        ref = FirebaseDatabase.getInstance().getReference("users/"+
                user.getUid()+"/LastMessages/"+friendUserUid+"/");

        ref.setValue(msg);
    }


}