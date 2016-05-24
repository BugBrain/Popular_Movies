package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Warren on 5/24/2016.
 */
public class DetailActivityFragment extends Fragment{

    public ImageAdapter mImageAdapter;

    private ArrayList<MovieObject> movieList;

    public TextView mOriginalTitle, mPlotSynopsis, mRatings, mReleaseDate;
    public ListView mPoster;

    public DetailActivityFragment(){}

    @Override
    public void onStart(){
        super.onStart();

        //grabs intent
        Intent intent = getActivity().getIntent();
        //grabs movieObject from intent
        MovieObject movieObject = (MovieObject) intent.getParcelableExtra("selectedMovie");

        //initalizes new array and puts movie object into it to pass it to the Image Adapter
        movieList = new ArrayList<MovieObject>();
        movieList.add(movieObject);
        mImageAdapter = new ImageAdapter(getActivity(), movieList);

        mOriginalTitle.setText(movieObject.getOriginalTitle());
        mPoster.setAdapter(mImageAdapter);
        mPlotSynopsis.setText(movieObject.getPlotSynopsis());
        mRatings.setText("Ratings: " + movieObject.getVoteAverage());
        mReleaseDate.setText("Release Date: " + movieObject.getReleaseDate());

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mOriginalTitle = (TextView) rootView.findViewById(R.id.detail_title);

        mPoster = (ListView) rootView.findViewById(R.id.detail_poster);

        mPlotSynopsis = (TextView) rootView.findViewById(R.id.detail_plot);

        mRatings = (TextView) rootView.findViewById(R.id.detail_user_rating);

        mReleaseDate = (TextView) rootView.findViewById(R.id.detail_release_date);



        return rootView;
    }
}
