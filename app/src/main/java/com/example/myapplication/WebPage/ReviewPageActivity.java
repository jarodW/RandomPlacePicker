package com.example.myapplication.WebPage;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.example.myapplication.App;
import com.example.myapplication.R;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by jarod on 11/18/2017.
 */

public class ReviewPageActivity extends AppCompatActivity{
    /*private ReviewPageFragment mReviewPageFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_page);
        FragmentManager fm = getSupportFragmentManager();
        mReviewPageFragment = ReviewPageFragment.newInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fm.beginTransaction().add(R.id.fragment_container_web_view,mReviewPageFragment).commit();
    }

    @Override
    public void onBackPressed() {
        if(mReviewPageFragment.onBackPressed())
            return;
        else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return(super.onOptionsItemSelected(item));
    }*/

    private static final String TAG = "ReviewPageFragment";
    private Uri mUri;
    private WebView mWebView;
    private FrameLayout mWebContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        setContentView(R.layout.test);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        mUri = getIntent().getData();
        mWebContainer = (FrameLayout) findViewById(R.id.web_view);
        mWebView = new WebView(getApplicationContext());
        mWebContainer.addView(mWebView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.clearCache(true);
        mWebView.loadUrl(mUri.toString());
    }


    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()){
            mWebView.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"OnDestroy()");
        super.onDestroy();
        RefWatcher refWatcher = App.getRefWatcher(this);
        refWatcher.watch(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return(super.onOptionsItemSelected(item));
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals("play.google.com")) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                    startActivity(intent);
                    return true;
                } catch (ActivityNotFoundException e) {
                    // Google Play app is not installed, you may want to open the app store link
                    Uri uri = Uri.parse(url);
                    view.loadUrl("http://play.google.com/store/apps/" + uri.getHost() + "?" + uri.getQuery());
                    return false;
                }

            }
            return false;
        }


    }

    private void releaseWebView() {

        mWebContainer.removeAllViews();
        if(mWebView != null){
            mWebView.setTag(null);
            mWebView.clearHistory();
            mWebView.removeAllViews();
            mWebView.clearCache(true);
            mWebView.destroy();
            mWebView = null;
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        releaseWebView();
    }

}
