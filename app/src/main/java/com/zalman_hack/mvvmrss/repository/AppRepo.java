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

    public boolean insertChannel(Channel channel) {
        try {
            RssParser rssParser = new RssParser(channel.link);
            if(rssParser.isDataLoaded()) {
                channel = rssParser.getChannel();
                appDatabase.channelsFeedDao().insertChannel(channel);
                List<ItemWithChannelAndCategories> items = rssParser.getItems();
                appDatabase.channelsFeedDao().updateChannelItems(channel, items);
                return true;
            }
            return false;
        } catch (Exception ignore) {
            return false;
        }
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
                if(rssParser.isDataLoaded()) {
                    List<ItemWithChannelAndCategories> items = rssParser.getItems();
                    appDatabase.channelsFeedDao().updateChannelItems(channel, items);
                }
                else {
                    result = false;
                }
            }
            return result;
        } catch (Exception ignore) {
            return false;
        }
    }
}