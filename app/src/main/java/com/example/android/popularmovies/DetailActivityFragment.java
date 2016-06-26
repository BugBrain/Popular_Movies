package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Warren on 5/24/2016.
 */
public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    static final String DETAIL_URI = "URI";
    private Uri mUri;
    private Context mContext;
    private static final int CURSOR_LOADER_ID_MOVIES = 0;

    public static Cursor movieIdCursor;

    public static final int COL_MOVIE_ID = 0;
    public static final int COL_TITLE = 1;
    public static final int COL_POSTER_PATH = 2;
    public static final int COL_RELEASE_DATE = 3;
    public static final int COL_VOTE_AVERAGE = 4;
    public static final int COL_MOVIE_PLOT = 5;
    public static final int COL_TRAILERS_AND_REVIEWS = 6;

    public static final int MOVIE_OBJECT_TYPE_DETAILS = 0;
    public static final int MOVIE_OBJECT_TYPE_TRAILERS = 1;
    public static final int MOVIE_OBJECT_TYPE_REVIEWS= 2;

    public ArrayList<MovieObject> mMovieObjectArrayList;

    public MovieDetailsAdapter mMovieDetailsAdapter;

    public ListView mListView;

    private MovieObject movieObject;

    public DetailActivityFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null){
            mUri = arguments.getParcelable(DetailActivityFragment.DETAIL_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mListView = (ListView) rootView.findViewById(R.id.movie_detail_listview);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        int cursorLoaderId;
        String viewSettings = Utility.getViewSettings(getContext());

        cursorLoaderId = CURSOR_LOADER_ID_MOVIES;
        getLoaderManager().initLoader(cursorLoaderId, null, this);


        super.onActivityCreated(savedInstanceState);
    }

    void onMovieChanged( String viewSelection ) {
        // replace the uri, since the location has changed
        Uri uri = mUri;
        if (null != uri) {
            Bundle arguments = getArguments();
            if (arguments != null){
                mUri = arguments.getParcelable(DetailActivityFragment.DETAIL_URI);
            }
            getLoaderManager().restartLoader(CURSOR_LOADER_ID_MOVIES, null, this);
        }
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        if(mUri != null) {
            return new CursorLoader(
                    mContext,
                    mUri,
                    null,
                    null,
                    null,
                    null
            );

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.moveToFirst()){


            movieIdCursor = data;
            mMovieObjectArrayList = new ArrayList<MovieObject>();

            movieObject = new MovieObject(
                    MOVIE_OBJECT_TYPE_DETAILS,
                    data.getString(COL_MOVIE_ID),
                    data.getString(COL_TITLE),
                    data.getString(COL_MOVIE_PLOT),
                    data.getString(COL_VOTE_AVERAGE),
                    data.getString(COL_RELEASE_DATE),
                    data.getString(COL_POSTER_PATH));
            mMovieObjectArrayList.add(movieObject);
            try {
                Utility.getTrailerObjects(
                        data.getString(COL_TRAILERS_AND_REVIEWS), mMovieObjectArrayList);
                Utility.getReviewObjects(
                        data.getString(COL_TRAILERS_AND_REVIEWS), mMovieObjectArrayList);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            mMovieDetailsAdapter = new MovieDetailsAdapter(getActivity(), mMovieObjectArrayList);
            mListView.setAdapter(mMovieDetailsAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}
