package com.zalman_hack.mvvmrss.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayoutMediator;
import com.zalman_hack.mvvmrss.R;
import com.zalman_hack.mvvmrss.adapters.FeedsPageAdapter;
import com.zalman_hack.mvvmrss.databinding.FragmentFeedsPageBinding;
import com.zalman_hack.mvvmrss.viewmodels.FeedViewModel;
import com.zalman_hack.mvvmrss.viewmodels.templates.UpdatedState;

import static androidx.navigation.fragment.NavHostFragment.findNavController;
import static java.lang.String.valueOf;

public class FeedsPageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private FragmentFeedsPageBinding binding;
    private FeedsPageAdapter adapter;
    private FeedViewModel feedViewModel;

    public FeedsPageFragment() { }

    public static FeedsPageFragment newInstance() {
        FeedsPageFragment fragment = new FeedsPageFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getWindow().setNavigationBarColor(requireContext().getColor(R.color.white));
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        feedViewModel = new ViewModelProvider(requireActivity()).get(FeedViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adapter = new FeedsPageAdapter(getChildFragmentManager(), getLifecycle());
        binding = FragmentFeedsPageBinding.inflate(inflater, container, false);
        binding.viewPager.setAdapter(adapter);
        binding.swipeRefreshLayout.setOnRefreshListener(this);
        binding.swipeRefreshLayout.setColorSchemeColors(requireContext().getColor(R.color.saffron));
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration);

        feedViewModel.getChannelsAllLive().observe(requireActivity(),
                channels -> {
                    adapter.setChannels(channels);
                    new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                            (tab, position) -> {
                                Log.i("UPDATE CHANNELS", valueOf(position));
                                if(channels.size() > position)
                                    tab.setText(channels.get(position).name);
                            }).attach();
                });
        feedViewModel.refreshStateLive.observe(requireActivity(),
                state -> {
                    if (state == UpdatedState.UPDATING)
                        binding.swipeRefreshLayout.setRefreshing(true);
                    else if(state == UpdatedState.UN_SUCCESSFUL || state == UpdatedState.SUCCESSFUL) {
                        binding.swipeRefreshLayout.setRefreshing(false);
                        if(state == UpdatedState.UN_SUCCESSFUL)
                            Toast.makeText(getActivity(), getString(R.string.update_failed), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onRefresh() {
        feedViewModel.updateChannels();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            NavDirections directions = FeedsPageFragmentDirections.actionFeedsPageFragmentToSettingsFragment();
            findNavController(this).navigate(directions);
        }
        return super.onOptionsItemSelected(item);
    }
}