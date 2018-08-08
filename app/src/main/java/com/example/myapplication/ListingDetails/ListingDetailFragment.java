package com.example.myapplication.ListingDetails;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.App;
import com.example.myapplication.R;
import com.example.myapplication.models.yelp.Business;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by jarod on 10/30/2017.
 */

public class ListingDetailFragment extends Fragment{
    private final String TAG = "ListingDetailFragment";
    private TextView listingName;
    private MapView mapView;
    private Button button;
    private FloatingActionButton mDirectionButton;
    private ImageView mRatingImage;
    private TextView mRatingCountText;
    private TextView mPriceAndCategoryText;
    private TextView mDistance;
    private TextView mPhoneNumber;
    private ListingDetailViewModel mViewModel;
    public static ListingDetailFragment newInstance(){
        return new ListingDetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listing_detail,container,false);
        final Business business = getActivity().getIntent().getParcelableExtra("business");
        mViewModel = new ListingDetailViewModel();
        listingName = (TextView) view.findViewById(R.id.listing_detial_text);
        button = (Button) view.findViewById(R.id.button2);
        mDirectionButton = (FloatingActionButton) view.findViewById(R.id.directionButton);
        mRatingImage = (ImageView) view.findViewById((R.id.imageView));
        mRatingCountText = (TextView) view.findViewById(R.id.numOfReviewsText);
        mPriceAndCategoryText = (TextView) view.findViewById(R.id.price_type_text);
        mDistance = (TextView) view.findViewById(R.id.distanceText);
        mPhoneNumber = (TextView) view.findViewById(R.id.phonenNumberText);

        mRatingImage.setImageResource(mViewModel.loadRating(business.getRating()));
        mRatingCountText.setText("Based on " + business.getReviewCount() + " reviews");
        mPriceAndCategoryText.setText(business.getPrice() + " \u2022  " + mViewModel.getCategoryString(business.getCategories()));
        mDistance.setText(Double.toString(business.getDistance()));

        mPhoneNumber.setText(business.getDisplayPhone());
        if(business.getName() != null || !business.getName().equals(""))
            listingName.setText(business.getName());
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                LatLng latLng = new LatLng(business.getCoordinates().getLatitude(),business.getCoordinates().getLongitude());
                Marker mark = googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(business.getName())
                //.snippet(mViewModel.getAddress(business))
                );
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri intentUri = Uri.parse("geo:" + business.getCoordinates().getLatitude() + "," + business.getCoordinates().getLongitude() + "?q=" + Uri.encode(business.getName()) + " " + business.getLocation().getAddress1());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW,intentUri);
                if(mapIntent.resolveActivity(getActivity().getPackageManager()) != null){
                    Log.d(TAG,"Launch map");
                    startActivity(mapIntent);
                }else{
                    Log.d(TAG,"cannot launch map intent");
                }
            }
        });

        mDirectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri intentUri = Uri.parse("geo:" + business.getCoordinates().getLatitude() + "," + business.getCoordinates().getLongitude() + "?q=" + Uri.encode(business.getName()) + " " + business.getLocation().getAddress1());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW,intentUri);
                if(mapIntent.resolveActivity(getActivity().getPackageManager()) != null){
                    Log.d(TAG,"Launch map");
                    startActivity(mapIntent);
                }else{
                    Log.d(TAG,"cannot launch map intent");
                }
            }
        });

        listingName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Couldn't get rid of the memory leak using a webview so switch to a view Intent.
                /*Intent webView = new Intent(getActivity(),ReviewPageActivity.class);
                webView.setData(Uri.parse(business.getUrl()));
                startActivity(webView);*/
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(business.getUrl()));
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mapView != null){
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        if(mapView != null){
            mapView.onPause();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        if(mapView != null)
            mapView.onStop();
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if(mapView != null){
            mapView.onLowMemory();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mapView != null)
            mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mapView != null){
            mapView.onDestroy();
        }
        mapView = null;
        RefWatcher refWatcher = App.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

        /*private void loadRating(Double rating,int numReviews) {
        rating =  Math.round(rating * 2) / 2.0;
        if (rating == 0)
            mRatingImage.setImageResource(R.drawable.stars_small_0);
        else if (rating == 1)
            mRatingImage.setImageResource(R.drawable.stars_small_1);
        else if (rating == 1.5)
            mRatingImage.setImageResource(R.drawable.stars_small_1_half);
        else if (rating == 2)
            mRatingImage.setImageResource(R.drawable.stars_small_2);
        else if (rating == 2.5)
            mRatingImage.setImageResource(R.drawable.stars_small_2_half);
        else if (rating == 3)
            mRatingImage.setImageResource(R.drawable.stars_small_3);
        else if (rating == 3.5)
            mRatingImage.setImageResource(R.drawable.stars_small_3_half);
        else if (rating == 4)
            mRatingImage.setImageResource(R.drawable.stars_small_4);
        else if (rating == 4.5)
            mRatingImage.setImageResource(R.drawable.stars_small_4_half);
        else if (rating == 5)
            mRatingImage.setImageResource(R.drawable.stars_small_5);
        mRatingCountText.setText("Based on " + numReviews + " reviews");

    }

    public void setCategoryString(){
        String cats = business.getCategories().get(0).getTitle();
        if(business.getCategories().size() > 1) {
            for (int i = 1; i < business.getCategories().size() && i < 3; i++)
                cats += ", "+ business.getCategories().get(i).getTitle();
        }
        mPriceAndCategoryText.setText(business.getPrice() + " \u2022  " + cats);
    }*/
}
