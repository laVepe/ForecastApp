package com.example.pafi.weatherapp.RestAPI;

import com.example.pafi.weatherapp.RestAPI.Result;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;


public interface ResultAPI {
    @GET("2.5/forecast/city?APPID=69dd880b968b6f8f0a2440f2e93beadb")
    Call<Result> getByLocation(@Query("lat") double lat, @Query("lon") double lon);

    @GET("2.5/forecast/city?APPID=69dd880b968b6f8f0a2440f2e93beadb")
    Call<Result> getByCity(@Query("q") String name);
}
