package com.example.myapplication.network;

import com.example.myapplication.models.places.Example;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by jarod on 10/16/2017.
 */

public interface PlacesAPI {
    @GET("maps/api/place/nearbysearch/json")
    Call<Example> getUser(
            @QueryMap Map<String,String> options);
    @GET("maps/api/place/nearbysearch/json")
    Call<Example> nextPage(
            @Query("pagetoken") String token);
}
