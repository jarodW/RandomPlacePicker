package com.example.myapplication.Favorites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;

/**
 * Created by jarod on 10/29/2017.
 */

public class FavoritesPageFragment extends Fragment{
    private TextView mEditText;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_page, container, false);
        mEditText = (TextView)view.findViewById(R.id.pagetext);
        return view;
    }

}
