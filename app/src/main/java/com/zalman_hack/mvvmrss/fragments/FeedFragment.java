package com.zalman_hack.mvvmrss.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.zalman_hack.mvvmrss.adapters.FeedAdapter;
import com.zalman_hack.mvvmrss.adapters.FeedsPageAdapter;
import com.zalman_hack.mvvmrss.databases.ItemWithChannelAndCategories;
import com.zalman_hack.mvvmrss.databases.entities.Channel;
import com.zalman_hack.mvvmrss.databinding.FragmentFeedBinding;
import com.zalman_hack.mvvmrss.viewmodels.FeedViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private static final String ARG_PARAM1 = "channel";
    private static final String className = FeedsPageAdapter.class.getName();

    private static int fragmentsCount = 0;
    private long fragmentId = 0;

    private FragmentFeedBinding binding;
    private Channel channel;
    private FeedAdapter feedAdapter;
    private FeedViewModel feedViewModel;

    public FeedFragment() {
        super();
        fragmentsCount += 1;
        fragmentId = fragmentsCount;
    }

    public static FeedFragment newInstance(Channel channel) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, channel);
        fragment.setArguments(args);
        return fragment;
    }

    public long getFragmentId() {
        return fragmentId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(className,"Fragment on Create -------------------------------------------------");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            channel = getArguments().getParcelable(ARG_PARAM1);
            Log.i("onCreate", channel.name);
            feedAdapter = new FeedAdapter(getContext(), getResources().getConfiguration());
            feedViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()).create(FeedViewModel.class);
            feedViewModel.getItemsOfChannelLive(channel.channel_id).observe(this.getActivity(), new Observer<List<ItemWithChannelAndCategories>>() {
                @Override
                public void onChanged(List<ItemWithChannelAndCategories> itemWithChannelAndCategories) {
                    feedAdapter.setChannelsWithItems(channel, itemWithChannelAndCategories);
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // View view =  inflater.inflate(R.layout.fragment_feed, container, false);
        Log.i("onCreateView", channel.name);
        binding = FragmentFeedBinding.inflate(inflater);
        binding.projectRecyclerView.setAdapter(feedAdapter);
        binding.projectRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return binding.getRoot();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("onStop", channel.name);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("onResume", channel.name);

        //this.feedAdapter.notifyDataSetChanged();
    }
}