package com.example.android.popularmovies.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by Warren on 6/16/2016.
 */

@Database(version = MovieDatabase.VERSION)
public class MovieDatabase {
    private MovieDatabase(){}

    public static final int VERSION = 1;

    //create the movie database
    @Table(MovieColumns.class) public static final String MOVIES = "movies";
}
