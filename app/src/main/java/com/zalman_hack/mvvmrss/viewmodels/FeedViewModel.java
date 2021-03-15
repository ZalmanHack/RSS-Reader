package com.zalman_hack.mvvmrss.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zalman_hack.mvvmrss.databases.ChannelWithItems;
import com.zalman_hack.mvvmrss.databases.ItemWithChannelAndCategories;
import com.zalman_hack.mvvmrss.databases.entities.Channel;
import com.zalman_hack.mvvmrss.repository.AppRepo;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FeedViewModel extends AndroidViewModel {

    private final AppRepo appRepo;
    public static final MutableLiveData<Boolean> refreshStateLive = new MutableLiveData<>();
    private final Executor executor = Executors.newSingleThreadExecutor();

    public FeedViewModel(@NonNull Application application) {
        super(application);
        appRepo = new AppRepo(application);
        updateChannels();
        Log.i("FeedViewModel", "init");
    }

    public LiveData<List<ChannelWithItems>> getItemsAllLive() {
        return appRepo.getItemsAllLive();
    }

    public LiveData<List<Channel>> getChannelsAllLive() {
        return appRepo.getChannelsAllLive();
    }

    public void insertChannel(Channel channel) {
        appRepo.insertChannel(channel);
    }

    public LiveData<List<ItemWithChannelAndCategories>> getItemsOfChannelLive(long channel_id) {
        return appRepo.getItemsOfChannelLive(channel_id);
    }

    public void updateChannels() {
        executor.execute(() -> {
            refreshStateLive.postValue(true);
            appRepo.updateChannels();
            refreshStateLive.postValue(false);
        });
    }

    public void deleteChannelAll() {
        appRepo.deleteChannelAll();
    }

    public void deleteChannelOf(String name) {
        appRepo.deleteChannelOf(name);
    }

    public void insertItems(Channel channel, ItemWithChannelAndCategories item) {
        appRepo.insertItem(channel, item);
    }
}
