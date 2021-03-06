package com.zalman_hack.mvvmrss.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zalman_hack.mvvmrss.databases.ItemWithChannelAndCategories;
import com.zalman_hack.mvvmrss.databases.entities.Channel;
import com.zalman_hack.mvvmrss.repository.AppRepo;
import com.zalman_hack.mvvmrss.viewmodels.templates.UpdatedState;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FeedViewModel extends ViewModel {

    private final AppRepo appRepo;
    public MutableLiveData<UpdatedState> refreshStateLive = new MutableLiveData<>();
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Inject
    public FeedViewModel(AppRepo appRepo) {
        this.appRepo = appRepo;
        refreshStateLive.postValue(UpdatedState.NOT_UPDATED);
        updateChannels();
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
}
