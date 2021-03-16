package com.zalman_hack.mvvmrss.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.zalman_hack.mvvmrss.activities.ShowItemActivity;
import com.zalman_hack.mvvmrss.adapters.FeedAdapter;
import com.zalman_hack.mvvmrss.adapters.OnClickItemInterface;
import com.zalman_hack.mvvmrss.databases.ItemWithChannelAndCategories;
import com.zalman_hack.mvvmrss.databases.entities.Channel;
import com.zalman_hack.mvvmrss.databinding.FragmentFeedBinding;
import com.zalman_hack.mvvmrss.viewmodels.FeedViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment implements OnClickItemInterface {

    private static final String ARG_PARAM1 = "channel";

    private FragmentFeedBinding binding;
    private Channel channel;
    private FeedAdapter feedAdapter;
    private FeedViewModel feedViewModel;

    public FeedFragment() {
        super();
    }

    public static FeedFragment newInstance(Channel channel) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, channel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            channel = getArguments().getParcelable(ARG_PARAM1);

            feedAdapter = new FeedAdapter(getContext(), getResources().getConfiguration(), this);
            feedViewModel = new ViewModelProvider(this.getActivity()).get(FeedViewModel.class);
            feedViewModel.getItemsOfChannelLive(channel.channel_id).observe(this,
                    itemList -> feedAdapter.setChannelsWithItems(channel, itemList));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFeedBinding.inflate(inflater, container, false);
        binding.itemRecyclerView.setAdapter(feedAdapter);
        binding.itemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClickItem(ItemWithChannelAndCategories itemModel) {
        Intent intent = new Intent(this.getContext(), ShowItemActivity.class);
        intent.putExtra("itemModel", itemModel);
        intent.putExtra("channelModel", channel);
        startActivity(intent);
    }
}