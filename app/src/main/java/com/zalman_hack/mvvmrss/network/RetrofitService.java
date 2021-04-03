package com.zalman_hack.mvvmrss.network;

import javax.inject.Inject;

public class RetrofitService {

    public final RetrofitServiceNewsInterface serviceNews;

    @Inject
    public RetrofitService(RetrofitServiceNewsInterface serviceNews) {
        this.serviceNews = serviceNews;
    }
}
