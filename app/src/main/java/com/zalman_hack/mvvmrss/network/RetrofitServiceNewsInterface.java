package com.zalman_hack.mvvmrss.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface RetrofitServiceNewsInterface {
    @GET
    @Headers({"Accept: application/json",
              "User-Agent: Mozilla/5.0 (Linux; Android 10) " +
                          "AppleWebKit/537.36 (KHTML, like Gecko) " +
                          "Chrome/89.0.4389.105 " +
                          "Mobile Safari/537.36"})
    Call<ResponseBody> getRss(@Url String url);
}
