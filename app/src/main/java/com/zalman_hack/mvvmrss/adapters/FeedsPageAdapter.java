package com.zalman_hack.mvvmrss.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.zalman_hack.mvvmrss.databases.entities.Channel;
import com.zalman_hack.mvvmrss.fragments.FeedFragment;

import java.util.ArrayList;
import java.util.List;

public class FeedsPageAdapter extends FragmentStateAdapter {

    private static final String className = FeedsPageAdapter.class.getName();
    private final List<FeedFragment> fragments = new ArrayList<>();
    private final ArrayList<Long> fragmentIDs = new ArrayList<>();

    public FeedsPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    // TODO Оптимизировать удаление и доавление новых каналов
    public void setChannels(List<Channel> channels) {
        //Log.i(className, MessageFormat.format("setChannels count: {0}", channels.size()));
        fragments.clear();
        fragmentIDs.clear();
        for (int i = 0; i < fragments.size();) {
            FeedFragment fragment = fragments.get(i);
            fragment.getParentFragmentManager().beginTransaction().remove(fragment).commit();
            fragments.remove(i);
            fragmentIDs.remove(i);
            //notifyItemRemoved(i);
        }
        for (int i = 0; i < channels.size(); i++) {
            FeedFragment fragment = FeedFragment.newInstance(channels.get(i));
            fragments.add(fragment);
            fragmentIDs.add((long) fragment.hashCode());
            //notifyItemInserted(i);
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
