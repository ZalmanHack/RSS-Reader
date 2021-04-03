package com.zalman_hack.mvvmrss.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zalman_hack.mvvmrss.adapters.OnClickChannelInterface;
import com.zalman_hack.mvvmrss.databases.entities.Channel;
import com.zalman_hack.mvvmrss.repository.AppRepo;
import com.zalman_hack.mvvmrss.viewmodels.templates.UpdatedState;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SettingsViewModel extends ViewModel implements OnClickChannelInterface {

    private final AppRepo appRepo;
    public MutableLiveData<UpdatedState> insertChannelStateLive = new MutableLiveData<>();
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Inject
    public SettingsViewModel(AppRepo appRepo) {
        this.appRepo = appRepo;
    }

    public LiveData<List<Channel>> getChannelsAllLive() {
        return appRepo.getChannelsAllLive();
    }

    @Override
    public void onDeleteChannel(Channel channel) {
        appRepo.deleteChannelOf(channel);
    }

    @Override
    public void onInsertChannel(String linkChannel) {
        executor.execute(() -> {
            insertChannelStateLive.postValue(UpdatedState.UPDATING);
            Channel channel = new Channel();
            channel.link = linkChannel;
            boolean updated_state = appRepo.insertChannel(channel);
            if (updated_state)
                insertChannelStateLive.postValue(UpdatedState.SUCCESSFUL);
            else
                insertChannelStateLive.postValue(UpdatedState.UN_SUCCESSFUL);
        });
    }
}
