package com.example.myapplication.WebPage;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.example.myapplication.App;
import com.example.myapplication.R;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by jarod on 11/18/2017.
 */

public class ReviewPageFragment extends Fragment{
    private static final String TAG = "ReviewPageFragment";
    private Uri mUri;
    private FrameLayout mWebContainer;
    private WebView mWebView;
    public static ReviewPageFragment newInstance() {
        return new ReviewPageFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_page,container,false);
        mUri = getActivity().getIntent().getData();
        mWebContainer = (FrameLayout) view.findViewById(R.id.web_view);
        mWebView = new WebView(getActivity().getApplicationContext());
        mWebContainer.addView(mWebView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (Uri.parse(url).getHost().equals("play.google.com")) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        Activity host = (Activity) view.getContext();
                        host.startActivity(intent);
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
        });
        mWebView.clearCache(true);
        mWebView.loadUrl(mUri.toString());
        return view;
    }

    public boolean onBackPressed(){
        if(mWebView.canGoBack()){
            mWebView.goBack();
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"OnDestroy()");
        super.onDestroy();
        //mWebContainer.removeAllViews();
        mWebContainer.removeAllViews();
        mWebContainer.removeAllViewsInLayout();
        mWebView.destroy();
        mWebView = null;
        RefWatcher refWatcher = App.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }



}
