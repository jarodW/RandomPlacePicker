package com.example.myapplication.Main;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.myapplication.Favorites.FavoritesFragment;
import com.example.myapplication.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private BottomNavigationView mBottomNavigationView;
    private GoogleApiClient mClient;
    private static final String TAG = "MainActivity";
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        disableShiftMode(mBottomNavigationView);
        final FragmentManager fm = getSupportFragmentManager();
        mClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, this)
                .build();
        mBottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    Fragment frag;
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_resturant:
                                frag = MainFragment.newInstance("restaurants",mClient);
                                fm.beginTransaction().replace(R.id.fragment_container,frag).commit();
                                return true;
                            case R.id.action_bars:
                                frag = MainFragment.newInstance("bars",mClient);
                                fm.beginTransaction().replace(R.id.fragment_container,frag).commit();
                                return true;
                            case R.id.action_cafes:
                                frag = MainFragment.newInstance("cafes",mClient);
                                fm.beginTransaction().replace(R.id.fragment_container,frag).commit();
                                return true;
                            /*Will add back into the feature
                            case R.id.action_favorites:
                                frag = FavoritesFragment.newInstance(mClient);
                                fm.beginTransaction().replace(R.id.fragment_container,frag).commit();
                                return true;
                            */
                        }
                        return false;
                    }
                });
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if(fragment == null){
            fragment = MainFragment.newInstance("restaurants",mClient);
            fm.beginTransaction().add(R.id.fragment_container,fragment).commit();
        }
    }

    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }

    @Override
    protected void onStart() {
        mClient.connect();
        super.onStart();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
       Log.d(TAG,"Successfully connected");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG,"API Client Connection Failed!");

    }


    @Override
    protected void onStop() {
        mClient.disconnect();
        super.onStop();
    }

}
