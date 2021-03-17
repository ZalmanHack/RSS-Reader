package com.zalman_hack.mvvmrss.adapters;

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
        this.channels = channels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChannelBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_channel, parent, false);
        return new ChannelAdapter.ViewHolderChannel(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Channel channelModel = channels.get(position);
        ((ChannelAdapter.ViewHolderChannel) holder).binding.setChannelModel(channelModel);
        ((ChannelAdapter.ViewHolderChannel) holder).binding.setListener(clickChannelInterface);
    }

    @Override
    public int getItemCount() {
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
