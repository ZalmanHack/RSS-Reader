package com.zalman_hack.mvvmrss.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.zalman_hack.mvvmrss.databases.AppDatabase;
import com.zalman_hack.mvvmrss.databases.ItemWithChannelAndCategories;
import com.zalman_hack.mvvmrss.databases.entities.Channel;
import com.zalman_hack.mvvmrss.network.RssParser;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;


public class AppRepo {

    public AppDatabase appDatabase;
    public RssParser rssParser;
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Inject
    public AppRepo(AppDatabase appDatabase, RssParser rssParser) {
        this.appDatabase = appDatabase;
        this.rssParser = rssParser;
    }

    public boolean insertChannel(Channel channel) {
        try {
            this.rssParser.load(channel.link);
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

    public LiveData<List<ItemWithChannelAndCategories>> getItemsOfChannelLive(long channelId) {
        return appDatabase.channelsFeedDao().getItemsOfChannelLive(channelId);
    }

    public boolean updateChannels() {
        try {
            boolean result = true;
            List<Channel> channels = appDatabase.channelsFeedDao().getChannelsAll();
            for(Channel channel : channels) {
                this.rssParser.load(channel.link);
                if(rssParser.isDataLoaded()) {
                    List<ItemWithChannelAndCategories> items = rssParser.getItems();
                    appDatabase.channelsFeedDao().updateChannelItems(channel, items);
                }
                else {
                    result = false;
                }
            }
            return result;
        } catch (Exception e) {
            Log.e("updateChannels", e.getMessage());
            return false;
        }
    }
}