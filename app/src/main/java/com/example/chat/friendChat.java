package com.example.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

public class friendChat extends AppCompatActivity {

    private ImageView friendProfilePic;
    private TextView friendName;
    private EditText messageToSend;
    private RecyclerView messageList;
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
    }

    private void loadMessages(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        Query query = database.getReference().child("users/"+
                user.getUid()+"/friends/"+friendUserUid+"/messages");

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                messageClass msg = snapshot.getValue(messageClass.class);
                msg.setRead(1);
                messages.add(msg);

                if(msg.getType().equalsIgnoreCase("receive")){
                    DatabaseReference ref = database.getReference("users/"+
                            user.getUid()+"/friends/"+friendUserUid+"/messages/"+
                            snapshot.getKey());
                    ref.setValue(msg);
//                    ref = database.getReference("users/"+
//                            friendUserUid+"/friends/"+user.getUid()+"/messages/"+
//                            snapshot.getKey());
//
//                    msg.setType("send");
//                    ref.setValue(msg);
                }
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
            }
        });

        DatabaseReference reference = database.getReference("users/"+friendUserUid+"/ProfilePic");

        reference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                new GetImageFromUrl(friendProfilePic).execute(Objects.requireNonNull(task.getResult())
                        .getValue(String.class));
            }
        });

    }

    public class GetImageFromUrl extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        public GetImageFromUrl(ImageView img){
            this.imageView = img;
        }
        @Override
        protected Bitmap doInBackground(String... url) {
            String stringUrl = url[0];
            Bitmap bitmap = null;
            InputStream inputStream;
            try {
                inputStream = new java.net.URL(stringUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap){
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }


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
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


//    OnbackButton Left


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, chatSectionActivity.class));
    }
}