package com.example.chat.addFriendsPackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.chat.R;
import com.example.chat.zoomTransformer.ZoomOutPageTransformer;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class addFriendsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);


        tabLayout = findViewById(R.id.tabLayoutAddFriends);
        viewPager = findViewById(R.id.viewPagerAddFriends);
        viewPager.setPageTransformer(new ZoomOutPageTransformer());

        viewPagerAdapterAddFriends adapter = new viewPagerAdapterAddFriends(
                getSupportFragmentManager(), getLifecycle());

        viewPager.setAdapter(adapter);


        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager,
                true, true, (tab, position) -> {
            if(position==0){
                tab.setText("Suggestions");
            }
            else{
                tab.setText("Requests");
            }
        });


        mediator.attach();

    }
}