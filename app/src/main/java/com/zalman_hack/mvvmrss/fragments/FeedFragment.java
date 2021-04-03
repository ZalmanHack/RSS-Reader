package com.zalman_hack.mvvmrss.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.zalman_hack.mvvmrss.adapters.FeedAdapter;
import com.zalman_hack.mvvmrss.adapters.OnClickItemInterface;
import com.zalman_hack.mvvmrss.databases.ItemWithChannelAndCategories;
import com.zalman_hack.mvvmrss.databases.entities.Channel;
import com.zalman_hack.mvvmrss.databinding.FragmentFeedBinding;
import com.zalman_hack.mvvmrss.viewmodels.FeedViewModel;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class FeedFragment extends Fragment implements OnClickItemInterface {

    private static final String ARG_PARAM1 = "channel";

    private Channel channel;
    private FeedAdapter feedAdapter;


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
        FeedViewModel feedViewModel = new ViewModelProvider(this.requireActivity()).get(FeedViewModel.class);
        if (getArguments() != null) {
            channel = getArguments().getParcelable(ARG_PARAM1);
            feedAdapter = new FeedAdapter(getResources().getConfiguration(), this);
            feedViewModel.getItemsOfChannelLive(channel.channel_id).observe(this,
                    itemList -> feedAdapter.setChannelsWithItems(channel, itemList));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentFeedBinding binding = FragmentFeedBinding.inflate(inflater, container, false);
        binding.itemRecyclerView.setAdapter(feedAdapter);
        binding.itemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClickItem(ItemWithChannelAndCategories itemModel) {
        NavDirections directions = FeedsPageFragmentDirections.actionFeedsPageFragmentToShowItemFragment(itemModel, channel);

        findNavController(this).navigate(directions);
    }
}