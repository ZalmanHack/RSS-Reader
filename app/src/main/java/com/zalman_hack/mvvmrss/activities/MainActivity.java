package com.zalman_hack.mvvmrss.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.tabs.TabLayoutMediator;
import com.zalman_hack.mvvmrss.adapters.FeedsPageAdapter;
import com.zalman_hack.mvvmrss.adapters.OnClickItemInterface;
import com.zalman_hack.mvvmrss.databases.ItemWithChannelAndCategories;
import com.zalman_hack.mvvmrss.databases.entities.Channel;
import com.zalman_hack.mvvmrss.databases.entities.Item;
import com.zalman_hack.mvvmrss.databinding.ActivityMainBinding;
import com.zalman_hack.mvvmrss.viewmodels.FeedViewModel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static java.lang.String.*;

public class MainActivity extends AppCompatActivity implements OnClickItemInterface, SwipeRefreshLayout.OnRefreshListener {

    private ActivityMainBinding binding;
    private FeedsPageAdapter adapter;
    private FeedViewModel feedViewModel;
    private final Executor executor = Executors.newSingleThreadExecutor();

    private static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new FeedsPageAdapter(this);
        binding.viewPager.setAdapter(adapter);

        //feedViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(FeedViewModel.class);
        feedViewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        feedViewModel.getChannelsAllLive().observe(this,
                channels -> {
                    adapter.setChannels(channels);
                    new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                            (tab, position) -> {
                                Log.i("UPDATE CHANNELS", valueOf(position));
                                if(channels.size() > position) {
                                    tab.setText(channels.get(position).name);
                                }
                            }).attach();
                });

        binding.swipeRefreshLayout.setOnRefreshListener(this);
        FeedViewModel.refreshStateLive.observe(MainActivity.this,
                aBoolean -> binding.swipeRefreshLayout.setRefreshing(aBoolean));

        //onButtonDelClick(new View(this));
        binding.add.setOnClickListener(this::onButtonAddClick);
        binding.del.setOnClickListener(this::onButtonDelClick);
    }

    @Override
    public void onClickItem(ItemWithChannelAndCategories projectModel) {
        Intent intent = new Intent(MainActivity.this, ShowItemActivity.class);
        intent.putExtra("model", projectModel);
        startActivity(intent);
    }

    public void onButtonAddClick(View v)
    {
        count+=1;
        Channel channel = new Channel();
        channel.name = "1";
        channel.link = "https://www.androidpolice.com/feed/";
        ItemWithChannelAndCategories item = new ItemWithChannelAndCategories();
        item.item = new Item();
        item.item.title = "title" + count;
        item.item.description = "description";
        feedViewModel.insertItems(channel, item);
        /*
        List<String> channels = new ArrayList<String>(Arrays.asList(
                "https://vc.ru/rss/all",
                "https://www.androidpolice.com/feed/",
                "https://blog.humblebundle.com/rss",
                "https://www.eurosport.ru/rss.xml"));

        for (int i = 1; i <= channels.size(); i++) {
            Channel channel = new Channel();
            channel.name = String.valueOf(i);// + String.valueOf(new Date().getTime());
            channel.link = channels.get(i-1);
            feedViewModel.insertChannel(channel);
        }
         */
    }

    public void onButtonDelClick(View v)
    {
        feedViewModel.deleteChannelAll();
        //feedViewModel.deleteChannelOf();
        //appRepo.deleteChannelOf("РИА новости");
        Log.i("MYTEG", "Del Click");
    }

    @Override
    public void onRefresh() {
        feedViewModel.updateChannels();
    }
}