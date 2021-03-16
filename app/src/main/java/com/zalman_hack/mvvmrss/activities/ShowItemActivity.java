package com.zalman_hack.mvvmrss.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.zalman_hack.mvvmrss.R;
import com.zalman_hack.mvvmrss.adapters.CategoryAdapter;
import com.zalman_hack.mvvmrss.databases.ItemWithChannelAndCategories;
import com.zalman_hack.mvvmrss.databases.entities.Channel;
import com.zalman_hack.mvvmrss.databinding.ActivityShowItemBinding;
import com.zalman_hack.mvvmrss.helpers.MyDrawableCompat;

import java.util.Objects;

public class ShowItemActivity extends AppCompatActivity {

    private ActivityShowItemBinding binding;
    private ItemWithChannelAndCategories itemModel = new ItemWithChannelAndCategories();
    private Channel channelModel = new Channel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(getColor(R.color.white));
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityShowItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().hasExtra("itemModel")) {
            itemModel = getIntent().getParcelableExtra("itemModel");
            binding.setItemModel(itemModel);
            CategoryAdapter adapter = new CategoryAdapter(itemModel.categories);
            binding.categoryRecyclerView.setAdapter(adapter);
            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
            layoutManager.setFlexDirection(FlexDirection.ROW);
            layoutManager.setJustifyContent(JustifyContent.FLEX_START);
            layoutManager.setAlignItems(AlignItems.FLEX_START);
            binding.categoryRecyclerView.setLayoutManager(layoutManager);
        }

        if (getIntent().hasExtra("channelModel")) {
            channelModel = getIntent().getParcelableExtra("channelModel");
            binding.setChannelModel(channelModel);
        }

        setSupportActionBar(binding.toolbar);
        // для добавления кнопки назад в активити
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);



        binding.appBar.addOnOffsetChangedListener(((appBarLayout, verticalOffset) -> {
            Log.i("appBar", String.valueOf(verticalOffset) + " : " + String.valueOf(appBarLayout.getTotalScrollRange()));
            int colorRes = (Math.abs(verticalOffset) > appBarLayout.getTotalScrollRange() * 0.75) ? R.color.saffron : R.color.white;
            MyDrawableCompat.setColorFilter(Objects.requireNonNull(binding.toolbar.getNavigationIcon()), getColor(colorRes));
            for(int i = 0; i < binding.toolbar.getMenu().size(); i++) {
                MyDrawableCompat.setColorFilter(binding.toolbar.getMenu().getItem(i).getIcon(), getColor(colorRes));
            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_item_menu, menu);
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
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