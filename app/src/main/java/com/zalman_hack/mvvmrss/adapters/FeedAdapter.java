package com.zalman_hack.mvvmrss.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zalman_hack.mvvmrss.R;
import com.zalman_hack.mvvmrss.databases.ItemWithChannelAndCategories;
import com.zalman_hack.mvvmrss.databases.entities.Channel;
import com.zalman_hack.mvvmrss.databinding.ItemContainerLandscapeBinding;
import com.zalman_hack.mvvmrss.databinding.ItemContainerPortraitBinding;

import java.text.MessageFormat;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Channel channel;
    private List<ItemWithChannelAndCategories> itemList;
    private final Context context;
    private final Configuration configuration;
    private final OnClickItemInterface clickItemInterface;

    public FeedAdapter(Context context, Configuration configuration, OnClickItemInterface clickItemInterface) {
        this.configuration = configuration;
        this.context = context;
        this.clickItemInterface = clickItemInterface;
    }

    public void setChannelsWithItems(Channel channel, List<ItemWithChannelAndCategories> itemList) {
        Log.i("FeedAdapter", MessageFormat.format("setChannelsWithItems. itemList count: {0}", itemList.size()));
        boolean isDataChanged = false;
        if(this.channel == null || !this.channel.equals(channel)) {
            this.channel = channel;
            isDataChanged = true;
        }
        if(this.itemList == null || !this.itemList.equals(itemList)) {
            this.itemList = itemList;
            isDataChanged = true;
        }
        if(isDataChanged) {
            this.notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.i("FeedAdapter", "onBindViewHolder");
        if (itemList != null) {
            if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                ItemWithChannelAndCategories item = itemList.get(position);
                ((ViewHolderPortrait) holder).binding.container.setAnimation(AnimationUtils.loadAnimation(context,R.anim.item_transition));
                ((ViewHolderPortrait) holder).binding.setItemModel(item);
                ((ViewHolderPortrait) holder).binding.setChannelModel(channel);
                ((ViewHolderPortrait) holder).binding.setListener(clickItemInterface);
            } else {
                ItemWithChannelAndCategories item = itemList.get(position);
                ((ViewHolderLandscape) holder).binding.setItemModel(item);
                ((ViewHolderLandscape) holder).binding.setChannelModel(channel);
                ((ViewHolderLandscape) holder).binding.setListener(clickItemInterface);
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("FeedAdapter", "onCreateViewHolder");
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            ItemContainerPortraitBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.item_container_portrait, parent, false);
            return new ViewHolderPortrait(binding);
        } else {
            ItemContainerLandscapeBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.item_container_landscape, parent, false);
            return new ViewHolderLandscape(binding);
        }
    }

    @Override
    public int getItemCount() {
        if (itemList != null) {
            return itemList.size();
        }
        return 0;
    }

    public static class ViewHolderPortrait extends RecyclerView.ViewHolder {
        private ItemContainerPortraitBinding binding;
        public ViewHolderPortrait(@NonNull ItemContainerPortraitBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class ViewHolderLandscape extends RecyclerView.ViewHolder {
        private ItemContainerLandscapeBinding binding;
        public ViewHolderLandscape(@NonNull ItemContainerLandscapeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
