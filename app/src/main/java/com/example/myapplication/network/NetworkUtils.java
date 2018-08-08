package com.example.myapplication.network;

import android.content.SharedPreferences;
import android.nfc.Tag;
import android.util.Log;

import com.example.myapplication.App;
import com.example.myapplication.models.places.Example;
import com.example.myapplication.models.places.Result;
import com.example.myapplication.models.yelp.Yelp;

import java.io.File;
import java.util.Map;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jarod on 10/15/2017.
 */

public class NetworkUtils {
    private static final String TAG = "NetworkUtils";
    private static final String google_base_url = "https://maps.googleapis.com/";
    private static final String yelp_base_url = "https://api.yelp.com/";

    private static Retrofit.Builder yelpBuilder = new Retrofit.Builder()
            .baseUrl(yelp_base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    private static Retrofit.Builder googleBuilder  = new Retrofit.Builder()
            .baseUrl(google_base_url)
            .addConverterFactory(GsonConverterFactory.create());
    private static File file = getTempFile();
    private static Cache cache = getCache();

    private static OkHttpClient.Builder googleHttpClient = new OkHttpClient.Builder().addInterceptor(new TokenInterceptor());
    private static Retrofit distanceRetrofit = googleBuilder.client(googleHttpClient.build()).build();
    private static DistanceAPI distanceApi = null;

    private static OkHttpClient.Builder yelpHttpClient = new OkHttpClient.Builder().cache(cache);
    private static Retrofit yelpRetrofit = yelpBuilder.client(yelpHttpClient.build()).build();
    private static YelpAPI yelpApi = null;

    static{
        file = getTempFile();
        if(file != null) {
            cache = getCache();
            yelpHttpClient = new OkHttpClient.Builder().cache(cache);
        }
        else{
            yelpHttpClient = new OkHttpClient.Builder();
        }

    }

    public static YelpAPI createYelpService(String authToken){
        if(yelpApi != null){
            return yelpApi;
        }
        AuthenticationInterceptor interceptor = new AuthenticationInterceptor(authToken);
        yelpHttpClient.addInterceptor(interceptor);
        yelpBuilder.client(yelpHttpClient.build());
        yelpRetrofit = yelpBuilder.build();
        yelpApi = yelpRetrofit.create(YelpAPI.class);
        return yelpApi;
    }

    public static DistanceAPI createDistanceService(){
        if(distanceApi != null)
            return distanceApi;
        distanceApi = distanceRetrofit.create(DistanceAPI.class);
        return distanceApi;
    }



    public static File getTempFile(){
            try {
                return File.createTempFile("Cache", null, App.getAppContext().getCacheDir());
            } catch (Exception ioe) {
                return null;
            }

    }

    public static Cache getCache(){
        if(cache == null){
            if(file == null){
                System.out.println("Cache Method File null");
            }
                return new Cache(file,1000);
        }
        else
            return cache;
    }

    /*
    private static Retrofit.Builder googleBuilder  = new Retrofit.Builder()
            .baseUrl(google_base_url)
            .addConverterFactory(GsonConverterFactory.create());
    private static OkHttpClient.Builder googleHttpClient = new OkHttpClient.Builder().addInterceptor(new TokenInterceptor());
    private static Retrofit retrofit = googleBuilder.client(googleHttpClient.build()).build();
    private static PlacesAPI googleApi = retrofit.create(PlacesAPI.class);

    public static void search(Map<String, String> data){
        googleApi.getUser(data).enqueue(
                new Callback<Example>() {
                    @Override
                    public void onResponse(Call<Example> call, Response<Example> response) {
                        Example example = response.body();
                        if(example.getNextPageToken() != null) {
                            for(Result result : example.getResults()){
                                if(result.getRating() <  3){
                                    example.getResults().remove(result);
                                }
                            }
                        }
                        else
                            Log.d(TAG,"Next page token does not exist");
                    }
                    @Override
                    public void onFailure(Call<Example> call, Throwable t) {
                    }
                }
        );
    }
    public static void getAuth(String a, String b, final String c, final SharedPreferences sharedPreferences){
        yelpApi.getToken(a,b,c).enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                Auth auth = response.body();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("token",auth.getAccessToken());
                editor.apply();
            }
            @Override
            public void onFailure(Call<Auth> call, Throwable t) {
            }
        });
    }*/

}