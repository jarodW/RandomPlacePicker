package com.example.myapplication.Main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.App;
import com.example.myapplication.ListingDetails.ListingDetailActivity;
import com.example.myapplication.R;
import com.example.myapplication.models.yelp.Business;
import com.example.myapplication.network.NetworkUtils;
import com.example.myapplication.network.YelpAPI;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import com.squareup.leakcanary.RefWatcher;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jarod on 10/6/2017.
 */

public class MainFragment extends Fragment{
    private static final String RATINGS_KEY = "ratings-key";
    private static final String PRICE_KEY = "price-key";
    private static final String DISTANCE_KEY = "distance-key";
    private static final String TAG = "MainFragment";
    private static GoogleApiClient mClient;
    private EditText mCategoryText;
    private TextView mCategoryLabel;
    private Spinner mDistanceSpinner;
    private RatingBar mRating;
    private CheckBox mCheckBoxOne;
    private CheckBox mCheckBoxTwo;
    private CheckBox mCheckBoxThree;
    private CheckBox mCheckBoxFour;
    private Button mSearchButton;
    private ProgressBar mProgressBar;
    //private TextView mTextView;
    private SharedPreferences mSharedPreferences;
    private static String mType;
    private static YelpAPI mYelpAPI;
    private MainViewModel mainViewModel;
    private CompositeDisposable mCompositeDisposable;
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefEditor;
    private TextView mTitleView;
    public static MainFragment newInstance(String type, GoogleApiClient client){
        mClient = client;
        mType = type;
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (ViewGroup)inflater.inflate(R.layout.fragment_main, container, false);
        mTitleView = (TextView) view.findViewById(R.id.title_label);
        mCategoryText = (EditText) view.findViewById(R.id.category_editText);
        mCategoryLabel = (TextView) view.findViewById(R.id.category_text_label);
        mDistanceSpinner = (Spinner) view.findViewById(R.id.distance_spinner);
        mRating = (RatingBar) view.findViewById(R.id.ratings_bar);
        mCheckBoxOne = (CheckBox) view.findViewById(R.id.one_dollar_sign);
        mCheckBoxTwo = (CheckBox) view.findViewById(R.id.two_dollar_sign);
        mCheckBoxThree = (CheckBox) view.findViewById(R.id.three_dollar_sign);
        mCheckBoxFour = (CheckBox) view.findViewById(R.id.four_dollar_sign);
        mSearchButton = (Button) view.findViewById(R.id.search_button);
        mSharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        //mTextView = (TextView) view.findViewById(R.id.textView);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefEditor = prefs.edit();
        mYelpAPI = NetworkUtils.createYelpService(getString(R.string.yelp));
        mainViewModel = new MainViewModel(mYelpAPI);
        mCompositeDisposable = new CompositeDisposable();

        setUp();

        if(mType.equals("bars")){
            mCategoryText.setHint(R.string.bar_hints);
        }else if(mType.equals("cafes")){
            mCategoryText.setHint(R.string.cafe_hints);
        }else{
            mCategoryText.setHint(R.string.restaurant_hints);
        }
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchButton.setEnabled(false);
                mProgressBar.setVisibility(View.VISIBLE);
                haveLocationPermission();
                if (setPrice() == false ) {
                    mSearchButton.setEnabled(true);
                    Toast.makeText(getContext(),"Must set price "  + mRating.getRating(),Toast.LENGTH_SHORT).show();
                } else {
                    mainViewModel.setCategory(mType, mCategoryText.getText().toString());
                    mainViewModel.search()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableSingleObserver<Business>() {
                                @Override
                                public void onSuccess(Business business) {
                                    if (business.getCategories() == null) {
                                        Toast.makeText(getContext(), "No Results Match Your Query", Toast.LENGTH_SHORT).show();
                                        mSearchButton.setEnabled(true);
                                    } else if (business != null) {
                                        Intent intent = new Intent(getContext(), ListingDetailActivity.class);
                                        intent.putExtra("business", business);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(getContext(), "There was an error with your request", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "Something went wrong");
                                    e.printStackTrace();
                                    Log.d(TAG, "Msg: " + e.getMessage());
                                    mSearchButton.setEnabled(true);
                                }
                            });
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        mSearchButton.setEnabled(true);
        mProgressBar.setVisibility(View.INVISIBLE);
        Log.d(TAG,"onStart");
        super.onStart();
    }

    private void setUp(){
        mTitleView.setText(mType.substring(0, 1).toUpperCase() + mType.substring(1));
        mCheckBoxOne.setChecked(prefs.getBoolean(PRICE_KEY+1+mType,true));
        mCheckBoxTwo.setChecked(prefs.getBoolean(PRICE_KEY+2+mType,true));
        mCheckBoxThree.setChecked(prefs.getBoolean(PRICE_KEY+3+mType,true));
        mCheckBoxFour.setChecked(prefs.getBoolean(PRICE_KEY+4+mType,true));
        mRating.setRating(prefs.getInt(RATINGS_KEY+mType,4));
        mDistanceSpinner.setSelection(prefs.getInt(DISTANCE_KEY+mType,0));
        mainViewModel.setDistance(Integer.valueOf(getResources().getStringArray(R.array.distances)[mDistanceSpinner.getSelectedItemPosition()]));
        mainViewModel.setRating((int)mRating.getRating());

        mDistanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prefEditor.putInt(DISTANCE_KEY+mType,position);
                prefEditor.apply();
                mainViewModel.setDistance(Integer.valueOf(getResources().getStringArray(R.array.distances)[mDistanceSpinner.getSelectedItemPosition()]));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                prefEditor.putInt(RATINGS_KEY+mType,(int)rating);
                prefEditor.apply();
                mainViewModel.setRating((int)rating);
            }
        });
        mCheckBoxOne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefEditor.putBoolean(PRICE_KEY+1+mType,isChecked);
                prefEditor.apply();
            }
        });
        mCheckBoxTwo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefEditor.putBoolean(PRICE_KEY+2+mType,isChecked);
                prefEditor.apply();
            }
        });
        mCheckBoxThree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefEditor.putBoolean(PRICE_KEY+3+mType,isChecked);
                prefEditor.apply();
            }
        });
        mCheckBoxFour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefEditor.putBoolean(PRICE_KEY+4+mType,isChecked);
                prefEditor.apply();
            }
        });

    }

    public void haveLocationPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mClient);
        if (mLastLocation != null) {
            mainViewModel.setLocation(mLastLocation.getLatitude(),mLastLocation.getLongitude());
        }
        else {
                    mainViewModel.setLocation(41.150615,-74.575488);
        }
    }

    private boolean setPrice(){
        //No Check boxes set
        if((mCheckBoxOne.isChecked()|| mCheckBoxTwo.isChecked() || mCheckBoxThree.isChecked() || mCheckBoxFour.isChecked()) == false)
            return false;

        mainViewModel.setPrice(mCheckBoxOne.isChecked(),mCheckBoxTwo.isChecked(), mCheckBoxThree.isChecked(),mCheckBoxFour.isChecked());
        return true;
    }

    @Override
    public void onResume() {
        Log.d(TAG,"onResume");
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mCompositeDisposable.dispose();
        RefWatcher refWatcher = App.getRefWatcher(getActivity());
        refWatcher.watch(this);
        super.onDestroy();
    }

}
