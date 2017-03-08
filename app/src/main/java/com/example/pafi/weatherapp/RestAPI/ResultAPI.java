package com.example.pafi.weatherapp.RestAPI;

import com.example.pafi.weatherapp.Constants;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface ResultAPI {
    @GET("2.5/forecast?APPID="+ Constants.API_KEY)
    Call<Result> getByLocation(@Query("lat") double lat, @Query("lon") double lon);

    @GET("2.5/forecast?APPID="+Constants.API_KEY)
    Call<Result> getByCity(@Query("q") String name);
}
