package com.zalman_hack.mvvmrss.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;

@Module
@InstallIn(SingletonComponent.class)
public class OkHttpClientModule {

    @Provides
    @Singleton
    public OkHttpClient provideClient() {
        return new OkHttpClient.Builder().build();
    }
}
