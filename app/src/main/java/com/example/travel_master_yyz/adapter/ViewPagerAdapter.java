package com.example.travel_master_yyz.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.travel_master_yyz.page_fragment.FollowedFragment;
import com.example.travel_master_yyz.page_fragment.FollowersFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new FollowedFragment();
        } else {
            return new FollowersFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
