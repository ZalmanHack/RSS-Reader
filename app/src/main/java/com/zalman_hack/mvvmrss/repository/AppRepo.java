package com.zalman_hack.mvvmrss.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.zalman_hack.mvvmrss.databases.AppDatabase;
import com.zalman_hack.mvvmrss.databases.ChannelWithItems;
import com.zalman_hack.mvvmrss.databases.ItemWithChannelAndCategories;
import com.zalman_hack.mvvmrss.databases.entities.Channel;
import com.zalman_hack.mvvmrss.network.RssParser;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppRepo {

    private AppDatabase appDatabase;
    private Executor executor = Executors.newSingleThreadExecutor();

    public AppRepo(Context context) {
        appDatabase = AppDatabase.getInstance(context);
    }


    public void insertItem(Channel channel, ItemWithChannelAndCategories item) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.channelsFeedDao().insertItem(channel, item);
            }
        });
    }


    public void insertChannel(Channel channel) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.channelsFeedDao().insertChannel(channel);
            }
        });
    }

    public void deleteFeedsAll() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.channelsFeedDao().deleteFeedsAll();
            }
        });
    }

    public void deleteChannelAll() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.
                        channelsFeedDao().
                        deleteChannelAll();
            }
        });
    }

    public void deleteChannelOf(String name) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.channelsFeedDao().deleteChannelOf(name);
            }
        });
    }

    public LiveData<List<Channel>> getChannelsAllLive()  {
        return appDatabase.channelsFeedDao().getChannelsAllLive();
    }

    public LiveData<List<ChannelWithItems>> getItemsAllLive()  {
        return appDatabase.channelsFeedDao().getItemsAllLive();
    }


    public LiveData<List<ItemWithChannelAndCategories>> getItemsOfChannelLive(long channelId) {
        return appDatabase.channelsFeedDao().getItemsOfChannelLive(channelId);
    }

    public void updateChannels() {
        try {
            List<Channel> channels = appDatabase.channelsFeedDao().getChannelsAll();
            for(Channel channel : channels) {
                RssParser rssParser = new RssParser(channel.link);
                List<ItemWithChannelAndCategories> items = rssParser.getItems();
                appDatabase.channelsFeedDao().updateChannelItems(channel, items);
            }
        } catch (Exception ignore) { }
    }

    public void updateChannelsFuture_() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Channel> channels = appDatabase.channelsFeedDao().getChannelsAll();
                for(Channel channel : channels) {
                    RssParser rssParser = new RssParser(channel.link);
                    List<ItemWithChannelAndCategories> items = rssParser.getItems();
                    appDatabase.channelsFeedDao().updateChannelItems(channel, items);
                }
            }
        });
    }
}