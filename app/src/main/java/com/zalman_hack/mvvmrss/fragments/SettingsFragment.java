package com.zalman_hack.mvvmrss.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zalman_hack.mvvmrss.R;
import com.zalman_hack.mvvmrss.adapters.ChannelAdapter;
import com.zalman_hack.mvvmrss.databinding.FragmentSettingsBinding;
import com.zalman_hack.mvvmrss.helpers.CustomDrawableCompat;
import com.zalman_hack.mvvmrss.viewmodels.SettingsViewModel;
import com.zalman_hack.mvvmrss.viewmodels.templates.UpdatedState;

import java.util.Objects;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private SettingsViewModel settingsViewModel;
    private ChannelAdapter adapter;
    private String linkChannel;

    public SettingsFragment() { }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getWindow().setNavigationBarColor(requireContext().getColor(R.color.white));
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        requireActivity().setTheme(R.style.Theme_MVVMRSS);
        settingsViewModel = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(getLayoutInflater(), container, false);
        adapter = new ChannelAdapter(settingsViewModel);
        binding.channelRecyclerView.setAdapter(adapter);
        binding.channelRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(((AppCompatActivity)requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        CustomDrawableCompat.setColorFilter(Objects.requireNonNull(binding.toolbar.getNavigationIcon()), requireContext().getColor(R.color.saffron));

        binding.add.setOnClickListener(
                v -> settingsViewModel.onInsertChannel(linkChannel));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration);

        settingsViewModel.getChannelsAllLive().observe(requireActivity(),
                channels -> adapter.setChannels(channels));

        settingsViewModel.insertChannelStateLive.observe(getViewLifecycleOwner(),
                state -> {
                    if (state.equals(UpdatedState.UPDATING)) {
                        binding.add.setVisibility(View.INVISIBLE);
                        binding.progressBar.setVisibility(View.VISIBLE);
                    }
                    else if(state.equals(UpdatedState.UN_SUCCESSFUL) || state.equals(UpdatedState.SUCCESSFUL)) {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        binding.add.setVisibility(View.VISIBLE);
                        if(state.equals(UpdatedState.SUCCESSFUL)) {
                            binding.urlText.setText("");
                            Toast.makeText(getActivity(), getString(R.string.update_successful), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getActivity(), getString(R.string.feed_resource_not_found), Toast.LENGTH_SHORT).show();
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
    }

}