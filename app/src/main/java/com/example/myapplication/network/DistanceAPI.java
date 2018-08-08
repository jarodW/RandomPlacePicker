package com.example.myapplication.network;

import com.example.myapplication.models.places.Example;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by jarod on 5/4/2018.
 */

public interface DistanceAPI {
    @GET("maps/api/distancematrix/json")
    Call<Example> getUser(
            @QueryMap Map<String,String> options);
}
