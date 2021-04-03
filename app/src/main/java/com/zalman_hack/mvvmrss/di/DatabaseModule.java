package com.zalman_hack.mvvmrss.di;

import android.content.Context;

import androidx.room.Room;

import com.zalman_hack.mvvmrss.databases.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

    @Singleton
    @Provides
    public AppDatabase provideAppDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                AppDatabase.DATABASE_NAME)
                .build();
    }


}
