package com.example.chat.addFriendsPackage;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.chat.addFriendsPackage.requestsFragmentPackage.requestsFragment;
import com.example.chat.addFriendsPackage.suggestionFragmentPackage.suggestedFriendsFragment;

public class viewPagerAdapterAddFriends extends FragmentStateAdapter {
    public viewPagerAdapterAddFriends(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        if (position==0){
            fragment = suggestedFriendsFragment.getInstance();
        }
        else {
            fragment = requestsFragment.getInstance();
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
