package com.example.android.popularmovies;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.data.MovieCursorAdapter;
import com.example.android.popularmovies.sync.MovieSyncAdapter;

import java.util.ArrayList;

/**
 * Created by Warren on 5/22/2016.
 */
public class MainMovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public ImageAdapter mImageAdapter;

    public static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    private ArrayList<MovieObject> movieList;

    public static final String SYNC_PREFS = "prefs";
    private String sort;

    private static final String POPULAR = "popular";
    private static final String RATED = "top_rated";
    private static final String FAVORITE = "favorite";

    private static final int CURSOR_LOADER_ID_MOVIES = 0;

    public static final int COL_MOVIE_ID = 0;
    public static final int COL_TITLE = 1;
    public static final int COL_POSTER_PATH = 2;
    public static final int COL_RELEASE_DATE = 3;
    public static final int COL_VOTE_AVERAGE = 4;
    public static final int COL_MOVIE_PLOT = 5;
    public static final int COL_TRAILERS_AND_REVIEWS = 6;

    private int mPosition = ListView.INVALID_POSITION;

    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;

    private MovieCursorAdapter mMovieCursorAdapter;

    Callback mCallback;

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri selectedMovieUri);
    }

    public MainMovieFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMovieCursorAdapter = new MovieCursorAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        sort = Utility.getViewSettings(getContext());

        // Get a reference to the GridView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.movies_grid);
        gridView.setAdapter(mMovieCursorAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                String movieIdSelected = cursor.getString(COL_MOVIE_ID);
                if(cursor != null){
                    mCallback = (Callback) getActivity();
                    String viewSettingsSelected = Utility.getViewSettings(getContext());
                    switch (viewSettingsSelected) {
                        case POPULAR: {
                            mCallback.onItemSelected(MovieContract.PopularMovieEntry
                                    .buildMoviesByIdUri(movieIdSelected));
                            break;
                        }
                        case RATED: {
                            mCallback.onItemSelected(MovieContract.RatedMovieEntry
                                    .buildMoviesByIdUri(movieIdSelected));
                            break;
                        }
                        case FAVORITE: {
                            mCallback.onItemSelected(MovieContract.FavoriteMovieEntry
                                    .buildMoviesByIdUri(movieIdSelected));
                            break;
                        }
                        default:{
                            throw new UnsupportedOperationException("mCallback selection error: " + viewSettingsSelected);
                        }

                    }
                }
                mPosition = position;
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        Log.v(LOG_TAG, "onActivityCreated");
        newCursor();
        super.onActivityCreated(savedInstanceState);
    }

    public void newCursor(){
        String viewSettings = Utility.getViewSettings(getContext());
        Log.v(LOG_TAG, "updatedCursor: viewSettings: " + viewSettings);
        Bundle savedInfo = new Bundle();
        savedInfo.putString(SYNC_PREFS, viewSettings);
        getLoaderManager().initLoader(CURSOR_LOADER_ID_MOVIES, savedInfo, this);
    }

    public void restartCursor(){
        String viewSettings = Utility.getViewSettings(getContext());
        Log.v(LOG_TAG, "restartCursor: viewSettings: " + viewSettings);
        Bundle savedInfo = new Bundle();
        savedInfo.putString(SYNC_PREFS, viewSettings);
        getLoaderManager().restartLoader(CURSOR_LOADER_ID_MOVIES, savedInfo, this);
    }

    public void onMovieChangeSettingsChange(){
        Log.v(LOG_TAG, "onMovieChangeSettingsChange ");
        updateMovieDb();
        restartCursor();
    }


    public void updateMovieDb(){
        Log.v(LOG_TAG, "updateMovieDb ");
        String setViewPref = Utility.getViewSettings(getContext());
        MovieSyncAdapter.syncImmediately(getActivity(), setViewPref);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        String viewSettings = args.getString(SYNC_PREFS);
        Uri contentUri;

        switch (viewSettings){
            case POPULAR:
                contentUri = MovieContract.PopularMovieEntry.CONTENT_URI;
                break;
            case RATED:
                contentUri = MovieContract.RatedMovieEntry.CONTENT_URI;
                break;
            case FAVORITE:
                contentUri = MovieContract.FavoriteMovieEntry.CONTENT_URI;
                break;
            default:
                contentUri = MovieContract.PopularMovieEntry.CONTENT_URI;
        }

        Log.v(LOG_TAG, "onCreateLoader: " + viewSettings + " " + contentUri.toString());

        return new CursorLoader(getActivity(),
                contentUri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){
        mMovieCursorAdapter.swapCursor(data);
        if (!data.moveToFirst()){
//            throw new UnsupportedOperationException("onLoadFinished cursor query is empty");
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        mMovieCursorAdapter.swapCursor(null);
    }



}
