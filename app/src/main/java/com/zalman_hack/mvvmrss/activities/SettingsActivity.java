package com.zalman_hack.mvvmrss.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.zalman_hack.mvvmrss.R;
import com.zalman_hack.mvvmrss.adapters.ChannelAdapter;
import com.zalman_hack.mvvmrss.adapters.OnClickChannelInterface;
import com.zalman_hack.mvvmrss.databases.entities.Channel;
import com.zalman_hack.mvvmrss.databinding.ActivitySettingsBinding;
import com.zalman_hack.mvvmrss.fragments.DeleteDialogFragment;
import com.zalman_hack.mvvmrss.helpers.CustomDrawableCompat;
import com.zalman_hack.mvvmrss.viewmodels.FeedViewModel;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity implements OnClickChannelInterface {

    private ActivitySettingsBinding binding;
    private FeedViewModel feedViewModel;
    private ChannelAdapter adapter;
    private String linkChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(getColor(R.color.white));
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new ChannelAdapter(this);
        binding.channelRecyclerView.setAdapter(adapter);
        binding.channelRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        feedViewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        feedViewModel.getChannelsAllLive().observe(this, channels -> adapter.setChannels(channels));

        feedViewModel.insertChannelStateLive.observe(this,
                state -> {
                    if (state.equals(FeedViewModel.UpdatedState.UPDATING)) {
                        binding.add.setVisibility(View.INVISIBLE);
                        binding.progressBar.setVisibility(View.VISIBLE);
                    }
                    else if(state.equals(FeedViewModel.UpdatedState.UN_SUCCESSFUL) || state.equals(FeedViewModel.UpdatedState.SUCCESSFUL)) {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        binding.add.setVisibility(View.VISIBLE);

                        if(state.equals(FeedViewModel.UpdatedState.SUCCESSFUL)) {
                            binding.urlText.setText("");
                            Toast.makeText(this, getString(R.string.update_successful), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(this, getString(R.string.feed_resource_not_found), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        binding.urlText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                linkChannel = String.valueOf(s);
            }
            @Override
            public void afterTextChanged(Editable s) {  }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        });

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        CustomDrawableCompat.setColorFilter(Objects.requireNonNull(binding.toolbar.getNavigationIcon()), getColor(R.color.saffron));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onDeleteChannel(Channel channel) {
        DeleteDialogFragment.newInstance(channel).show(getSupportFragmentManager(), "deleteDialog");
    }

    public void onInsertChannel(View view) {
        Channel channel = new Channel();
        channel.link = this.linkChannel;
        feedViewModel.insertChannel(channel);
    }
}