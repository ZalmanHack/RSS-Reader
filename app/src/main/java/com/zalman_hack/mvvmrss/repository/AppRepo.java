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

    private final AppDatabase appDatabase;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public AppRepo(Context context) {
        appDatabase = AppDatabase.getInstance(context);
    }


    public void insertItem(Channel channel, ItemWithChannelAndCategories item) {
        executor.execute(() -> appDatabase.channelsFeedDao().insertItem(channel, item));
    }


    public boolean insertChannel(Channel channel) {
        try {
            RssParser rssParser = new RssParser(channel.link);
            if(!rssParser.isDataLoaded()) {
                return false;
            }
            channel = rssParser.getChannel();
            appDatabase.channelsFeedDao().insertChannel(channel);
            List<ItemWithChannelAndCategories> items = rssParser.getItems();
            appDatabase.channelsFeedDao().updateChannelItems(channel, items);
            return true;
        } catch (Exception ignore) {
            return false;
        }
    }

    public void deleteFeedsAll() {
        executor.execute(() -> appDatabase.channelsFeedDao().deleteFeedsAll());
    }

    public void deleteChannelAll() {
        executor.execute(() -> appDatabase.
                channelsFeedDao().
                deleteChannelAll());
    }

    public void deleteChannelOf(Channel channel) {
        executor.execute(() -> appDatabase.channelsFeedDao().deleteChannelOf(channel.name, channel.link));
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

    public boolean updateChannels() {
        try {
            boolean result = true;
            List<Channel> channels = appDatabase.channelsFeedDao().getChannelsAll();
            for(Channel channel : channels) {
                RssParser rssParser = new RssParser(channel.link);
                if(!rssParser.isDataLoaded()) {
                    result = false;
                }
                else {
                    List<ItemWithChannelAndCategories> items = rssParser.getItems();
                    appDatabase.channelsFeedDao().updateChannelItems(channel, items);
                }
            }
            return result;
        } catch (Exception ignore) {
            return false;
        }
    }

    public void updateChannelsFuture_() {
        executor.execute(() -> {
            List<Channel> channels = appDatabase.channelsFeedDao().getChannelsAll();
            for(Channel channel : channels) {
                RssParser rssParser = new RssParser(channel.link);
                List<ItemWithChannelAndCategories> items = rssParser.getItems();
                appDatabase.channelsFeedDao().updateChannelItems(channel, items);
            }
        });
    }
}