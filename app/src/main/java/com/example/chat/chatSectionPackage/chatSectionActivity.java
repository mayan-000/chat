package com.example.chat.chatSectionPackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.chat.R;
import com.example.chat.zoomTransformer.ZoomOutPageTransformer;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class chatSectionActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPagerChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_section);


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