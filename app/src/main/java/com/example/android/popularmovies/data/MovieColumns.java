package com.example.android.popularmovies.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by Warren on 6/15/2016.
 */
public class MovieColumns {

    static final int COL_ID = 0;
    static final int COL_TITLE = 1;
    static final int COL_MOVIE_ID = 2;
    static final int COL_POSTER_PATH = 3;
    static final int COL_PLOT = 4;
    static final int COL_VOTE_AVERAGE = 5;
    static final int COL_RELEASE_DATE = 6;

    @DataType(DataType.Type.INTEGER) @PrimaryKey @AutoIncrement
    public static final String _ID = "_id";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String TITLE = "originialTitle";

    @DataType(DataType.Type.TEXT) @NotNull
            public static final String MOVIE_ID = "id";

    @DataType(DataType.Type.TEXT) @NotNull
            public static final String POSTER_PATH = "posterPath";

    @DataType(DataType.Type.TEXT) @NotNull
            public static final String MOVIE_PLOT = "plot";

    @DataType(DataType.Type.TEXT) @NotNull
            public static final String VOTE_AVERAGE = "voteAverage";

    @DataType(DataType.Type.TEXT) @NotNull
            public static final String RELEASE_DATE = "releaseDate";
}
