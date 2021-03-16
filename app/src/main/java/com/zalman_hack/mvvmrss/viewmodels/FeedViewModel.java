package com.zalman_hack.mvvmrss.viewmodels;

import android.app.Application;

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

    public enum UpdatedState {
        NOT_UPDATED,
        UPDATING,
        SUCCESSFUL,
        UN_SUCCESSFUL
    }

    private final AppRepo appRepo;
    public MutableLiveData<UpdatedState> refreshStateLive = new MutableLiveData<>();
    public MutableLiveData<UpdatedState> insertChannelStateLive = new MutableLiveData<>();
    private final Executor executor = Executors.newSingleThreadExecutor();

    public FeedViewModel(@NonNull Application application) {
        super(application);
        appRepo = new AppRepo(application);
        refreshStateLive.postValue(UpdatedState.NOT_UPDATED);
        insertChannelStateLive.postValue(UpdatedState.NOT_UPDATED);
        updateChannels();
    }

    public LiveData<List<ChannelWithItems>> getItemsAllLive() {
        return appRepo.getItemsAllLive();
    }

    public LiveData<List<Channel>> getChannelsAllLive() {
        return appRepo.getChannelsAllLive();
    }

    public LiveData<List<ItemWithChannelAndCategories>> getItemsOfChannelLive(long channel_id) {
        return appRepo.getItemsOfChannelLive(channel_id);
    }

    public void updateChannels() {
        executor.execute(() -> {
            refreshStateLive.postValue(UpdatedState.UPDATING);
            boolean updated_state = appRepo.updateChannels();
            if (updated_state)
                refreshStateLive.postValue(UpdatedState.SUCCESSFUL);
            else
                refreshStateLive.postValue(UpdatedState.UN_SUCCESSFUL);
        });
    }

    public void deleteChannelAll() {
        appRepo.deleteChannelAll();
    }

    public void deleteChannelOf(Channel channel) {
        appRepo.deleteChannelOf(channel);
    }

    public void insertItems(Channel channel, ItemWithChannelAndCategories item) {
        appRepo.insertItem(channel, item);
    }

    public void insertChannel(Channel channel) {
        executor.execute(() -> {
            insertChannelStateLive.postValue(UpdatedState.UPDATING);
            boolean updated_state = appRepo.insertChannel(channel);
            if (updated_state)
                insertChannelStateLive.postValue(UpdatedState.SUCCESSFUL);
            else
                insertChannelStateLive.postValue(UpdatedState.UN_SUCCESSFUL);
        });
    }
}
