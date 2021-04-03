package com.zalman_hack.mvvmrss.di;

import com.zalman_hack.mvvmrss.databases.AppDatabase;
import com.zalman_hack.mvvmrss.network.RssParser;
import com.zalman_hack.mvvmrss.repository.AppRepo;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppRepoModule {

    @Provides
    @Singleton
    public AppRepo provideAppRepo(AppDatabase appDatabase, RssParser rssParser) {
        return new AppRepo(appDatabase, rssParser);
    }
}
