package com.zalman_hack.mvvmrss.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.zalman_hack.mvvmrss.BR;
import com.zalman_hack.mvvmrss.R;
import com.zalman_hack.mvvmrss.databases.entities.Channel;

import java.util.ArrayList;
import java.util.List;

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ChannelViewHolder>
{
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
    public ChannelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ChannelViewHolder.create(LayoutInflater.from(parent.getContext()),parent,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ChannelViewHolder holder, int position) {
        holder.bindTo(channels.get(position), clickChannelInterface);
    }

    @Override
    public int getItemCount() {
        if (channels != null)
            return channels.size();
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_channel;
    }

    static class ChannelViewHolder extends RecyclerView.ViewHolder {

        private final ViewDataBinding binding;

        static ChannelViewHolder create(LayoutInflater inflater, ViewGroup parent, int viewType) {
            ViewDataBinding binding = DataBindingUtil.inflate(inflater, viewType, parent, false);
            return new ChannelViewHolder(binding);
        }

        public ChannelViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        public void bindTo(Channel channelModel, OnClickChannelInterface channelInterface) {
            binding.setVariable(BR.channelModel, channelModel);
            binding.setVariable(BR.channelListener, channelInterface);
            binding.executePendingBindings();
        }
    }
}
