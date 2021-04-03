package com.zalman_hack.mvvmrss.di;

import com.zalman_hack.mvvmrss.network.RetrofitServiceNewsInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Module
@InstallIn(SingletonComponent.class)
public class RetrofitModule {

    @Provides
    @Singleton
    public RetrofitServiceNewsInterface provideNewsRetrofitService(Retrofit retrofit) {
        return retrofit.create(RetrofitServiceNewsInterface.class);
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        String BASE_URL = "https://google.com";
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .build();
    }
}
