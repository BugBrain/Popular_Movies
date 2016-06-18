package com.example.android.popularmovies;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.popularmovies.sync.MovieSyncAdapter;
import com.example.android.popularmovies.data.MovieCursorAdapter;
import com.example.android.popularmovies.data.MovieProvider;

import java.util.ArrayList;

/**
 * Created by Warren on 5/22/2016.
 */
public class MainMovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public ImageAdapter mImageAdapter;

    private ArrayList<MovieObject> movieList;

    private static final int CURSOR_LOADER_ID = 0;

    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;

    private MovieCursorAdapter mMovieCursorAdapter;

    public MainMovieFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey("sort")) {
            movieList = new ArrayList<MovieObject>();
        }
        else {
            movieList = savedInstanceState.getParcelableArrayList("sort");
        }

    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("sort", movieList);
        super.onSaveInstanceState(outState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMovieCursorAdapter = new MovieCursorAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.movies_grid);
        gridView.setAdapter(mMovieCursorAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            }
        });


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    public void updateMovieDb(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String setViewPref = prefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_default));
        MovieSyncAdapter.syncImmediately(getActivity(), setViewPref);
    }

    private void loadFromDb(){
        Cursor c = getActivity().getContentResolver().query(MovieProvider.Movies.Content_URI,
                null,
                null,
                null,
                null);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        return new CursorLoader(getActivity(), MovieProvider.Movies.Content_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){
        mMovieCursorAdapter.swapCursor(data);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        mMovieCursorAdapter.swapCursor(null);
    }

}
