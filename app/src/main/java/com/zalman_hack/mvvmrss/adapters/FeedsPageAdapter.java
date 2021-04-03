package com.zalman_hack.mvvmrss.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.zalman_hack.mvvmrss.databases.entities.Channel;
import com.zalman_hack.mvvmrss.fragments.FeedFragment;

import java.util.ArrayList;
import java.util.List;

public class FeedsPageAdapter extends FragmentStateAdapter {

    private final List<FeedFragment> fragments = new ArrayList<>();
    private final ArrayList<Long> fragmentIDs = new ArrayList<>();

    public FeedsPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public void setChannels(List<Channel> channels) {
        fragments.clear();
        fragmentIDs.clear();
        for (int i = 0; i < fragments.size();) {
            FeedFragment fragment = fragments.get(i);
            fragment.getChildFragmentManager().beginTransaction().remove(fragment).commitNow();
            fragments.remove(i);
            fragmentIDs.remove(i);
        }
        for (int i = 0; i < channels.size(); i++) {
            FeedFragment fragment = FeedFragment.newInstance(channels.get(i));
            fragments.add(fragment);
            fragmentIDs.add((long) fragment.hashCode());
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }

    @Override
    public long getItemId(int position) {
        return fragmentIDs.get(position);
    }

    @Override
    public boolean containsItem(long itemId) {
        return fragmentIDs.contains(itemId);
    }
}
