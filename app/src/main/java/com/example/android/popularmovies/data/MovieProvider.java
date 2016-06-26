package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Warren on 6/16/2016.
 */


public final class MovieProvider extends ContentProvider{

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;
    private final String LOG_TAG = MovieProvider.class.getSimpleName();

    static final int POPULAR = 100;
    static final int POPULAR_WITH_ID = 101;
    static final int RATED = 200;
    static final int RATED_WITH_ID = 201;
    static final int FAVORITE = 300;
    static final int FAVORITE_WITH_ID = 301;


    private static final String sPopularIdSelection = MovieContract.PopularMovieEntry.TABLE_NAME +
            "." + MovieContract.PopularMovieEntry._ID + " = ? ";

    private static final String sRatedIdSelection = MovieContract.RatedMovieEntry.TABLE_NAME +
            "." + MovieContract.RatedMovieEntry._ID + " = ? ";

    private static final String sFavoriteIdSelection = MovieContract.FavoriteMovieEntry.TABLE_NAME +
            "." + MovieContract.RatedMovieEntry._ID + " = ? ";


    private Cursor getPopularMovieById(Uri uri, String[] projection, String sortOrder){
        String movieSelection = MovieContract.PopularMovieEntry.getMovieIdFromUri(uri);
        Log.v(LOG_TAG, movieSelection);

        String selection = sPopularIdSelection;
        Log.v(LOG_TAG, sPopularIdSelection);

        String[] selectionArgs = new String[]{movieSelection};

        Cursor testCursor = mOpenHelper.getReadableDatabase().query(
                MovieContract.PopularMovieEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        return testCursor;
    }

    private Cursor getRatedMovieById(Uri uri, String[] projection, String sortOrder){
        String movieSelection = MovieContract.RatedMovieEntry.getMovieIdFromUri(uri);

        String selection = sRatedIdSelection;
        String[] selectionArgs = {movieSelection};

        return mOpenHelper.getReadableDatabase().query(MovieContract.RatedMovieEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getFavoriteMovieById(Uri uri, String[] projection, String sortOrder){
        String movieSelection = MovieContract.FavoriteMovieEntry.getMovieIdFromUri(uri);

        String selection = sFavoriteIdSelection;
        String[] selectionArgs = {movieSelection};

        return mOpenHelper.getReadableDatabase().query(MovieContract.FavoriteMovieEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher(){

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_POPULAR, POPULAR);
        matcher.addURI(authority, MovieContract.PATH_POPULAR + "/*", POPULAR_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_RATED, RATED);
        matcher.addURI(authority, MovieContract.PATH_RATED + "/*", RATED_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_FAVORITE, FAVORITE);
        matcher.addURI(authority, MovieContract.PATH_FAVORITE + "/*", FAVORITE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate(){
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri){

        final int match = sUriMatcher.match(uri);

        switch(match){
            case POPULAR:
                return MovieContract.PopularMovieEntry.CONTENT_TYPE;
            case POPULAR_WITH_ID:
                return MovieContract.PopularMovieEntry.CONTENT_ITEM_TYPE;
            case RATED:
                return MovieContract.RatedMovieEntry.CONTENT_TYPE;
            case RATED_WITH_ID:
                return MovieContract.RatedMovieEntry.CONTENT_ITEM_TYPE;
            case FAVORITE:
                return MovieContract.FavoriteMovieEntry.CONTENT_TYPE;
            case FAVORITE_WITH_ID:
                return MovieContract.FavoriteMovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){

        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case POPULAR: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.PopularMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case POPULAR_WITH_ID:{
                retCursor = getPopularMovieById(uri, projection, sortOrder);
                break;
            }
            case RATED: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.RatedMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case RATED_WITH_ID:{
                retCursor = getRatedMovieById(uri, projection, sortOrder);
                break;
            }
            case FAVORITE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case FAVORITE_WITH_ID:{
                retCursor = getFavoriteMovieById(uri, projection, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case POPULAR:
            case POPULAR_WITH_ID:{
                long _id = db.insert(MovieContract.PopularMovieEntry.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri = MovieContract.PopularMovieEntry.buildMoviesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case RATED:
            case RATED_WITH_ID:{
                long _id = db.insert(MovieContract.RatedMovieEntry.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri = MovieContract.RatedMovieEntry.buildMoviesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case FAVORITE:
            case FAVORITE_WITH_ID:{
                long _id = db.insert(MovieContract.FavoriteMovieEntry.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri = MovieContract.FavoriteMovieEntry.buildMoviesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if(null == selection) selection = "1";
        switch (match){
            case POPULAR:
                rowsDeleted = db.delete(
                        MovieContract.PopularMovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case RATED:
                rowsDeleted = db.delete(
                        MovieContract.RatedMovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVORITE:
                rowsDeleted = db.delete(
                        MovieContract.FavoriteMovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match){
            case POPULAR:
                rowsUpdated = db.update(MovieContract.PopularMovieEntry.TABLE_NAME,
                        values, selection, selectionArgs);
            break;
            case POPULAR_WITH_ID:
                rowsUpdated = db.update(MovieContract.PopularMovieEntry.TABLE_NAME, values,
                    sPopularIdSelection, selectionArgs);
            break;
            case RATED:
                rowsUpdated = db.update(MovieContract.RatedMovieEntry.TABLE_NAME,
                        values, selection, selectionArgs);
                break;
            case RATED_WITH_ID:
                rowsUpdated = db.update(MovieContract.RatedMovieEntry.TABLE_NAME, values,
                        sRatedIdSelection, selectionArgs);
                break;
            case FAVORITE:
                rowsUpdated = db.update(MovieContract.FavoriteMovieEntry.TABLE_NAME,
                        values, selection, selectionArgs);
                break;
            case FAVORITE_WITH_ID:
                rowsUpdated = db.update(MovieContract.FavoriteMovieEntry.TABLE_NAME, values,
                        sFavoriteIdSelection, selectionArgs);
                break;
        default:
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;
        switch (match){
            case POPULAR:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.PopularMovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                    }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            case RATED:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.RatedMovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            case FAVORITE:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.FavoriteMovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            default:
                return super.bulkInsert(uri, values);

        }
    }

}
