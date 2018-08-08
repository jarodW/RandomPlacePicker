package com.example.myapplication.network;

import java.util.Map;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import  com.example.myapplication.models.yelp.Yelp;

/**
 * Created by jarod on 10/22/2017.
 */

public interface YelpAPI {
    @FormUrlEncoded
    @POST("oauth2/token")
    Single<Response<Auth>> getToken(
            @Field("grant_type") String type,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret);
    @GET("v3/businesses/search")
    Single<Response<Yelp>> search(
            @QueryMap Map<String, String> option);
}
