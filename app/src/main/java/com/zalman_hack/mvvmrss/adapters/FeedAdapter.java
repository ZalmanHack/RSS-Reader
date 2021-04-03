package com.zalman_hack.mvvmrss.adapters;

import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.zalman_hack.mvvmrss.R;
import com.zalman_hack.mvvmrss.BR;
import com.zalman_hack.mvvmrss.databases.ItemWithChannelAndCategories;
import com.zalman_hack.mvvmrss.databases.entities.Channel;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {
    private Channel channel;
    private List<ItemWithChannelAndCategories> itemList;
    private final Configuration configuration;
    private final OnClickItemInterface clickItemInterface;

    public FeedAdapter(Configuration configuration, OnClickItemInterface clickItemInterface) {
        this.configuration = configuration;
        this.clickItemInterface = clickItemInterface;
    }

    public void setChannelsWithItems(Channel channel, List<ItemWithChannelAndCategories> itemList) {
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
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        if (itemList != null) {
            holder.bindTo(itemList.get(position), channel, clickItemInterface);
        }
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return FeedViewHolder.create(LayoutInflater.from(parent.getContext()), parent, viewType);
    }

    @Override
    public int getItemCount() {
        if (itemList != null) {
            return itemList.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            return R.layout.item_container_portrait;
        return R.layout.item_container_landscape;
    }

    static class FeedViewHolder extends RecyclerView.ViewHolder {

        private final ViewDataBinding binding;

        static FeedViewHolder create(LayoutInflater inflater, ViewGroup parent, int viewType) {
            ViewDataBinding binding = DataBindingUtil.inflate(inflater, viewType, parent, false);
            return new FeedViewHolder(binding);
        }

        public FeedViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindTo(ItemWithChannelAndCategories itemModel,
                           Channel channelModel,
                           OnClickItemInterface listener) {
            binding.setVariable(BR.itemModel, itemModel);
            binding.setVariable(BR.channelModel, channelModel);
            binding.setVariable(BR.itemListener, listener);
            binding.executePendingBindings();
        }
    }
}
