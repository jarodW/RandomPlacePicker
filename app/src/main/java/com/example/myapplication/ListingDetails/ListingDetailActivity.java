package com.example.myapplication.ListingDetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.myapplication.R;

/**
 * Created by jarod on 10/29/2017.
 */

public class ListingDetailActivity extends AppCompatActivity{
    private final String TAG = "ListingDetailActivity";
    private ListingDetailFragment fragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_detail);
        FragmentManager fm = getSupportFragmentManager();
        fragment = ListingDetailFragment.newInstance();
        fm.beginTransaction().add(R.id.fragment_container_listing_detail,fragment).commit();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG,"onDestroy");
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().remove(fragment);
        super.onDestroy();
    }
}
