package com.zalman_hack.mvvmrss.di;

import com.zalman_hack.mvvmrss.network.RetrofitService;
import com.zalman_hack.mvvmrss.network.RssParser;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class RssParserModule {

    @Provides
    @Singleton
    public RssParser provideRssParser(RetrofitService service) {
        return new RssParser(service);
    }
}
