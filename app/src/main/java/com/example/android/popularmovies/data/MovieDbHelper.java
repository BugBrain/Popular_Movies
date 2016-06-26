package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Warren on 6/21/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movies.db";

    public  MovieDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){

        final String SQL_CREATE_POPULAR_TABLE = "CREATE TABLE " +
                MovieContract.PopularMovieEntry.TABLE_NAME + " (" +
                MovieContract.PopularMovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieContract.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieContract.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_PLOT + " TEXT NOT NULL, " +
                MovieContract.COLUMN_TRAILERS_AND_REVIEWS + " TEXT " +
                " );";

        final String SQL_CREATE_RATED_TABLE = "CREATE TABLE " +
                MovieContract.RatedMovieEntry.TABLE_NAME + " (" +
                MovieContract.RatedMovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieContract.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieContract.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_PLOT + " TEXT NOT NULL, " +
                MovieContract.COLUMN_TRAILERS_AND_REVIEWS + " TEXT " +
                " );";

        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " +
                MovieContract.FavoriteMovieEntry.TABLE_NAME + " (" +
                MovieContract.FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieContract.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieContract.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_PLOT + " TEXT NOT NULL, " +
                MovieContract.COLUMN_TRAILERS_AND_REVIEWS + " TEXT " +
                " );";


        sqLiteDatabase.execSQL(SQL_CREATE_POPULAR_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_RATED_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion){

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.PopularMovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.RatedMovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.FavoriteMovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
