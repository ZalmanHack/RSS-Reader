package com.zalman_hack.mvvmrss.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.zalman_hack.mvvmrss.databases.entities.Category;
import com.zalman_hack.mvvmrss.databases.entities.Channel;
import com.zalman_hack.mvvmrss.databases.entities.Item;
import com.zalman_hack.mvvmrss.databases.entities.ItemWithCategory;

@TypeConverters({Converters.class})
@Database(entities = {Item.class, Category.class, ItemWithCategory.class, Channel.class}, exportSchema = false, version = 17)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "feed_database.db";
    public static AppDatabase instance;
    private static final Object LOCK = new Object();

    public abstract ChannelsFeedDao channelsFeedDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}
