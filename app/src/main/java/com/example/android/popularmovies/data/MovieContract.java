package com.example.android.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Warren on 6/21/2016.
 */
public class MovieContract {

    public static final String LOG_TAG = MovieContract.class.getSimpleName();

    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_POPULAR = "popular";
    public static final String PATH_RATED = "rated";
    public static final String PATH_FAVORITE = "favorite";

    public static final String COLUMN_TITLE = "originalTitle";
    public static final String COLUMN_POSTER_PATH = "posterPath";
    public static final String COLUMN_PLOT = "plot";
    public static final String COLUMN_VOTE_AVERAGE = "voteAverage";
    public static final String COLUMN_RELEASE_DATE = "releaseDate";
    public static final String COLUMN_TRAILERS_AND_REVIEWS = "trailersAndReviews";

    public static final class PopularMovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon().appendPath(PATH_POPULAR).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULAR;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULAR;

        public static final String TABLE_NAME = "popular";

        public static Uri buildMoviesUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMoviesByIdUri(String popularMovieId){
            return CONTENT_URI.buildUpon().appendPath(popularMovieId).build();
        }

        public static String getMovieIdFromUri(Uri uri){
            Log.v(LOG_TAG, uri.getPath());
            Log.v(LOG_TAG, uri.getPathSegments().get(1));
            return uri.getPathSegments().get(1);
        }
    }

    public static final class RatedMovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon().appendPath(PATH_RATED).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RATED;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RATED;

        public static final String TABLE_NAME = "rated";

        public static Uri buildMoviesUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMoviesByIdUri(String ratedMovieId){
            return CONTENT_URI.buildUpon().appendPath(ratedMovieId).build();
        }

        public static String getMovieIdFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }
    }

    public static final class FavoriteMovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon().appendPath(PATH_FAVORITE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE;

        public static final String TABLE_NAME = "favorite";

        public static Uri buildMoviesUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMoviesByIdUri(String favoriteMovieId){
            return CONTENT_URI.buildUpon().appendPath(favoriteMovieId).build();
        }

        public static String getMovieIdFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }
    }

}
