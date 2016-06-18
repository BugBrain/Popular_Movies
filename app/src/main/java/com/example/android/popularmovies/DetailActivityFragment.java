package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieColumns;
import com.example.android.popularmovies.data.MovieProvider;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by Warren on 5/24/2016.
 */
public class DetailActivityFragment extends Fragment{

    public static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    public ImageAdapter mImageAdapter;

    private ArrayList<MovieObject> movieList;

    public TextView mOriginalTitle, mPlotSynopsis, mRatings, mReleaseDate;
    public ListView mPoster;
    public CheckBox mFavoriteStar;

    private MovieObject movieObject;

    private String imageFilePath;

    public DetailActivityFragment(){}

    private boolean isFavorite = false;

    @Override
    public void onStart(){
        super.onStart();

        //grabs intent
        Intent intent = getActivity().getIntent();
        //grabs movieObject from intent
        movieObject = (MovieObject) intent.getParcelableExtra("selectedMovie");

        //initalizes new array and puts movie object into it to pass it to the Image Adapter
        movieList = new ArrayList<MovieObject>();
        movieList.add(movieObject);
        mImageAdapter = new ImageAdapter(getActivity(), movieList);

        mOriginalTitle.setText(movieObject.getOriginalTitle());
        mPoster.setAdapter(mImageAdapter);
        mPlotSynopsis.setText(movieObject.getPlotSynopsis());
        mRatings.setText("Ratings: " + movieObject.getVoteAverage());
        mReleaseDate.setText("Release Date: " + movieObject.getReleaseDate());

        Picasso.with(getContext()).load(movieObject.getPosterPath()).into(target);

        //check to see if movie exists in db
//        String[] projection = {MovieColumns.MOVIE_ID};
//        String mSelectionClause = MovieColumns.MOVIE_ID + " =?";
//        String[] mSelectionArgs = {movieObject.getId()};
//
//        Cursor mCursor = getActivity().getContentResolver().query(
//                MovieProvider.Movies.Content_URI,
//                projection,
//                mSelectionClause,
//                mSelectionArgs,
//                null
//        );
//
//        if(mCursor == null){
//            return;
//        } else if(mCursor.moveToFirst()){
//            String movieDbId = mCursor.getString(0);
//            isFavorite = true;
//        }






    }



    public void insertMovieDbData(){
        Log.d(LOG_TAG, "insert");

        ContentValues values = new ContentValues();

        values.put(MovieColumns.MOVIE_ID, movieObject.getId());
        values.put(MovieColumns.TITLE, movieObject.getOriginalTitle());
        values.put(MovieColumns.RELEASE_DATE, movieObject.getReleaseDate());
        values.put(MovieColumns.VOTE_AVERAGE, movieObject.getVoteAverage());
        values.put(MovieColumns.POSTER_PATH, movieObject.getPosterPath());
        Uri uri = getActivity().getContentResolver().insert(MovieProvider.Movies.Content_URI, values);


    }

    public void deleteMovieDbData(){
        String mSelectionClause = MovieColumns.MOVIE_ID + " LIKE ?";
        String[] mSelectionArgs = {movieObject.id};

        int mRowsDeleted = 0;

        mRowsDeleted = getActivity().getContentResolver().delete(
                MovieProvider.Movies.Content_URI,
                mSelectionClause,
                mSelectionArgs
        );

    }

    private Target target = new Target(){
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from){
            new Thread(new Runnable() {
                @Override
                public void run() {

                    File file = new File(
                            Environment.getExternalStorageDirectory().getPath()
                            + "/saved.jpg");
                    try {
                        file.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,ostream);
                        ostream.close();

                        imageFilePath = file.getCanonicalPath();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }).start();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable){}

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable){}
    };

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

        mFavoriteStar = (CheckBox) rootView.findViewById(R.id.favorite_checkbox);
        if(isFavorite){
            mFavoriteStar.setChecked(true);
        }
        mFavoriteStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //If checked, store movie info to database
                if(((CheckBox) v).isChecked()){
                    //add to database
                    insertMovieDbData();
                } else {
                    //delete from database
                    deleteMovieDbData();
                }
            }
        });



        return rootView;
    }
}
