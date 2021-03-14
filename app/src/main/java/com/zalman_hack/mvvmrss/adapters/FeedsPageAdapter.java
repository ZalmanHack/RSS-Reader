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

    private List<Channel> channels = new ArrayList<Channel>();;
    private static final String className = FeedsPageAdapter.class.getName();
    private List<FeedFragment> fragments = new ArrayList<FeedFragment>();
    private ArrayList<Long> fragmentIDs = new ArrayList<Long>();

    public FeedsPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public void setChannels(List<Channel> channels) {
        //Log.i(className, MessageFormat.format("setChannels count: {0}", channels.size()));
        for (int i = 0; i < fragments.size();) {
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
        this.channels = channels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
        //FeedFragment fragment = FeedFragment.newInstance(channels.get(position));
        //Log.i(className, MessageFormat.format("createFragment id: {0} position: {1}", fragment.getId(), position));
        //return fragment;
    }

    @Override
    public int getItemCount() {
        //Log.i(className, MessageFormat.format("getItemCount count: {0}", channels.size()));
        //return channels.size();
        return fragments.size();
    }

    @Override
    public long getItemId(int position) {
        //Log.i(className, MessageFormat.format("getItemId: {0} position: {1}", fragmentIDs.get(position), position));
        return fragmentIDs.get(position);
        //return channels.get(position).channel_id;
        //return RecyclerView.NO_ID;
    }

    @Override
    public boolean containsItem(long itemId) {
        //Log.i(className, MessageFormat.format("containsItem: {0} Item id: {1}",itemIds.contains(itemId), itemId));
        return fragmentIDs.contains(itemId);
        //return super.containsItem(itemId);
    }
}
