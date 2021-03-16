package com.zalman_hack.mvvmrss.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zalman_hack.mvvmrss.R;
import com.zalman_hack.mvvmrss.databases.entities.Channel;
import com.zalman_hack.mvvmrss.databinding.ItemChannelBinding;

import java.util.ArrayList;
import java.util.List;

public class ChannelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Channel> channels = new ArrayList<>();
    private final OnClickChannelInterface clickChannelInterface;

    public ChannelAdapter(OnClickChannelInterface clickChannelInterface) {
        this.clickChannelInterface = clickChannelInterface;
    }

    public void setChannels(List<Channel> channels) {
        Log.i("ChannelAdapter", "setChannels");
        this.channels = channels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("ChannelAdapter", "onCreateViewHolder");
        ItemChannelBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_channel, parent, false);
        return new ViewHolderChannel(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.i("ChannelAdapter", "onBindViewHolder: " + String.valueOf(position));
        Channel channelModel = channels.get(position);
        ((ViewHolderChannel) holder).binding.setChannelModel(channelModel);
        ((ViewHolderChannel) holder).binding.setListener(clickChannelInterface);
    }

    @Override
    public int getItemCount() {
        Log.i("ChannelAdapter", "getItemCount: " + String.valueOf(channels.size()));
        if (channels != null) {
            return channels.size();
        }
        return 0;
    }

    public static class ViewHolderChannel extends RecyclerView.ViewHolder {
        private final ItemChannelBinding binding;
        public ViewHolderChannel(@NonNull ItemChannelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
