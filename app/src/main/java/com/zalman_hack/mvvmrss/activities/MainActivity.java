package com.zalman_hack.mvvmrss.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.tabs.TabLayoutMediator;
import com.zalman_hack.mvvmrss.R;
import com.zalman_hack.mvvmrss.adapters.FeedsPageAdapter;
import com.zalman_hack.mvvmrss.databinding.ActivityMainBinding;
import com.zalman_hack.mvvmrss.viewmodels.FeedViewModel;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ActivityMainBinding binding;
    private FeedsPageAdapter adapter;
    private FeedViewModel feedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(getColor(R.color.white));
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new FeedsPageAdapter(this);
        binding.viewPager.setAdapter(adapter);

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
        binding.swipeRefreshLayout.setColorSchemeColors(getColor(R.color.saffron));
        feedViewModel.refreshStateLive.observe(MainActivity.this,
                state -> {
                    if (state.equals(FeedViewModel.UpdatedState.UPDATING)) {
                        binding.swipeRefreshLayout.setRefreshing(true);
                    }
                    else if(state.equals(FeedViewModel.UpdatedState.UN_SUCCESSFUL) || state.equals(FeedViewModel.UpdatedState.SUCCESSFUL)) {
                        binding.swipeRefreshLayout.setRefreshing(false);
                        if(state.equals(FeedViewModel.UpdatedState.UN_SUCCESSFUL))
                            Toast.makeText(this, getString(R.string.update_failed), Toast.LENGTH_SHORT).show();
                    }
                });

        setSupportActionBar(binding.toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onRefresh() {
        feedViewModel.updateChannels();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}