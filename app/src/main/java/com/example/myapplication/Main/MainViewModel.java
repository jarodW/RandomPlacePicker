package com.example.myapplication.Main;

import android.annotation.SuppressLint;
import android.location.Location;

import com.example.myapplication.models.yelp.Business;
import com.example.myapplication.network.YelpAPI;
import com.google.android.gms.common.api.GoogleApiClient;
import com.example.myapplication.models.yelp.Yelp;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import retrofit2.Response;

/**
 * Created by jarod on 3/19/2018.
 */

public class MainViewModel {
    private YelpAPI mYelpAPI;
    private static Random sRandom;
    private final int distanceConversion  = 1600;
    private Map<String, String> data;
    private int mRating;
    public MainViewModel(YelpAPI mYelpAPI){
        data =  new HashMap<>();
        initData();
        this.mYelpAPI = mYelpAPI;
        sRandom = new Random();
    }

    public Single<Business>  search(){
        return mYelpAPI.search(data).map(yelpResponse -> {
            Yelp yelp = yelpResponse.body();
            if(yelpResponse.code() == 200) {
                if (yelp.getTotal() == 0 || yelp == null) {
                    return new Business();
                }else{
                    int result = sRandom.nextInt(yelp.getBusinesses().size());
                    Business business = yelp.getBusinesses().get(result);
                    int count =0;
                    while (business.getRating() < mRating && count < 50) {
                        result = sRandom.nextInt(yelp.getBusinesses().size());
                        business = yelp.getBusinesses().get(result);
                        count++;
                    }
                    if(count == 50)
                        return new Business();
                    return business;
                }
            }
            else
                throw new Exception();
        });
    }

    public void setDistance(int distance){
        int mDistance = distance * distanceConversion;
        data.put("radius",String.valueOf(mDistance));
    }

    public void setLocation(double latitude, double longitude){
        data.put("longitude",String.valueOf(longitude));
        data.put("latitude",String.valueOf(latitude));
    }

    public void setCategory(String category, String term){
            data.put("categories",category);
            if(term != null || term.length() != 0)
                data.put("term",term);
    }

    public void setPrice(boolean one, boolean two, boolean three, boolean four){
        if(one==false && two== false && three== false  &&four == false){
            if(data.containsKey("price"))
                data.remove("price");
            return;
        }
        StringBuilder price = new StringBuilder();
        if(one){
            price.append("1");
        }
        if(two){
            if(price.length() != 0)
                price.append(",2");
            else
                price.append("2");
        }
        if(three){
            if(price.length() != 0)
                price.append(",3");
            else
                price.append("3");
        }
        if(four){
            if(price.length() != 0)
                price.append(",4");
            else
                price.append("4");
        }
        data.put("price",price.toString());
    }

    public void initData(){
        data.put("open_now","true");
        data.put("limit","50");
    }

    public void setRating(int rating){
       mRating = rating;
    }
}
