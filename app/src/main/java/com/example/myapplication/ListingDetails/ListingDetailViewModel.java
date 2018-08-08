package com.example.myapplication.ListingDetails;

import com.example.myapplication.R;
import com.example.myapplication.models.yelp.Business;
import com.example.myapplication.models.yelp.Category;

import java.util.List;

/**
 * Created by jarod on 3/21/2018.
 */

public class ListingDetailViewModel {
    public int loadRating(Double rating) {
        rating =  Math.round(rating * 2) / 2.0;
        if (rating == 0)
            return R.drawable.stars_small_0;
        else if (rating == 1)
            return R.drawable.stars_small_1;
        else if (rating == 1.5)
            return R.drawable.stars_small_1_half;
        else if (rating == 2)
            return R.drawable.stars_small_2;
        else if (rating == 2.5)
            return R.drawable.stars_small_2_half;
        else if (rating == 3)
            return R.drawable.stars_small_3;
        else if (rating == 3.5)
            return R.drawable.stars_small_3_half;
        else if (rating == 4)
            return R.drawable.stars_small_4;
        else if (rating == 4.5)
            return R.drawable.stars_small_4_half;
        else if (rating == 5)
            return R.drawable.stars_small_5;
        return 0;
    }

    public String getCategoryString(List<Category> categories){
        String cats = categories.get(0).getTitle();
        if(categories.size() > 1) {
            for (int i = 1; i < categories.size() && i < 3; i++)
                cats += ", "+ categories.get(i).getTitle();
        }

        return cats;
    }

    public String getAddress(Business business){
        String address = business.getLocation().getAddress1();
        String city = business.getLocation().getCity();
        String state = business.getLocation().getState();
        String zipcode = business.getLocation().getZipCode();
        return address + "\n" + city + ", " + state  + " " + zipcode;
    }
}
