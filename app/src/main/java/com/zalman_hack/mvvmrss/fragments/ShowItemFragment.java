package com.zalman_hack.mvvmrss.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.zalman_hack.mvvmrss.R;
import com.zalman_hack.mvvmrss.adapters.CategoryAdapter;
import com.zalman_hack.mvvmrss.databases.ItemWithChannelAndCategories;
import com.zalman_hack.mvvmrss.databases.entities.Channel;
import com.zalman_hack.mvvmrss.databinding.FragmentShowItemBinding;
import com.zalman_hack.mvvmrss.helpers.CustomDrawableCompat;

import java.util.Objects;

public class ShowItemFragment extends Fragment {

    private static final String ARG_PARAM1 = "itemModel";
    private static final String ARG_PARAM2 = "channelModel";

    private FragmentShowItemBinding binding;
    private ItemWithChannelAndCategories itemModel;

    public ShowItemFragment() { }

    public static ShowItemFragment newInstance(ItemWithChannelAndCategories itemModel, Channel channelModel) {
        ShowItemFragment fragment = new ShowItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, itemModel);
        args.putParcelable(ARG_PARAM2, channelModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getWindow().setNavigationBarColor(requireContext().getColor(R.color.white));
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentShowItemBinding.inflate(getLayoutInflater());

        if (getArguments() != null) {
            itemModel = getArguments().getParcelable(ARG_PARAM1);
            binding.setItemModel(itemModel);
            CategoryAdapter adapter = new CategoryAdapter(itemModel.categories);
            binding.categoryRecyclerView.setAdapter(adapter);
            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
            layoutManager.setFlexDirection(FlexDirection.ROW);
            layoutManager.setJustifyContent(JustifyContent.FLEX_START);
            layoutManager.setAlignItems(AlignItems.FLEX_START);
            binding.categoryRecyclerView.setLayoutManager(layoutManager);
            Channel channelModel = getArguments().getParcelable(ARG_PARAM2);
            binding.setChannelModel(channelModel);
        }

        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder().build();
        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.show_item_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        CustomDrawableCompat.setColorFilter(Objects.requireNonNull(binding.toolbar.getNavigationIcon()), requireContext().getColor(R.color.saffron));
        for (int i = 0; i < binding.toolbar.getMenu().size(); i++) {
            CustomDrawableCompat.setColorFilter(binding.toolbar.getMenu().getItem(i).getIcon(), requireContext().getColor(R.color.saffron));
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open_in_browser:
                String url = itemModel.item.item_link;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return true;
            case R.id.action_share:
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, itemModel.item.item_link);
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}