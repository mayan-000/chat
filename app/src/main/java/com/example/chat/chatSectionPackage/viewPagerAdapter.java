package com.example.chat.chatSectionPackage;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.chat.chatSectionPackage.friendsFragmentPackage.friendsFragment;
import com.example.chat.chatSectionPackage.profileFragmentPackage.profileFragment;
import com.example.chat.chatSectionPackage.recentChatPackage.recentChatsFragment;

public class viewPagerAdapter extends FragmentStateAdapter {

    public viewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;

        if(position==0){
            fragment = recentChatsFragment.getInstance();
        }
        else if(position==1){
            fragment = friendsFragment.getInstance();
        }
        else{
            fragment = profileFragment.getInstance();
        }


        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
