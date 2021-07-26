package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
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

public class chatSectionActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private RecyclerView recentChatList;
    private ArrayList<recentChatClass> recents = new ArrayList<>();
    private FirebaseRecyclerAdapter adapter;

    private TabLayout tabLayout;
    private ViewPager2 viewPagerChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_section);

        Log.d("msg","NoFrag");

        tabLayout = findViewById(R.id.tabLayoutChatSection);
        viewPagerChat = findViewById(R.id.viewPagerChatSection);
        viewPagerChat.setPageTransformer(new ZoomOutPageTransformer());

        viewPagerAdapter adapter = new viewPagerAdapter(getSupportFragmentManager()
        ,getLifecycle());

        viewPagerChat.setAdapter(adapter);


        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPagerChat,
                true, true, (tab, position) -> {
                    if(position==0){
                        tab.setText("Chats");
                    }
                    else if(position==1){
                        tab.setText("Friends");
                    }
                    else{
                        tab.setText("Profile");
                    }
                });

        mediator.attach();


    }



}