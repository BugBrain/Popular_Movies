package com.example.android.popularmovies;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.data.MovieDbHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Warren on 6/22/2016.
 */
public class MovieDetailsAdapter extends ArrayAdapter<MovieObject> {

    //initialize ArrayAdapter
    public MovieDetailsAdapter(Activity context, ArrayList<MovieObject> movieObjects){
        super(context, 0, movieObjects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final MovieObject movieObject = getItem(position);
        int objectType = movieObject.getObjectType();

        switch (objectType){
            case DetailActivityFragment.MOVIE_OBJECT_TYPE_DETAILS: {
                if(convertView == null){
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_movie_details, parent, false);
                }
                TextView movieTitle = (TextView) convertView.findViewById(R.id.detail_title);
                movieTitle.setText(movieObject.getOriginalTitle());
                TextView movieReleaseDate = (TextView) convertView.findViewById(R.id.detail_release_date);
                movieReleaseDate.setText(movieObject.getReleaseDate());
                RatingBar ratingBar =(RatingBar) convertView.findViewById(R.id.detail_rating_bar);
                ratingBar.setRating(Float.valueOf(movieObject.getRatings()) / 2);
                TextView moviePlot = (TextView) convertView.findViewById(R.id.detail_plot);
                moviePlot.setText(movieObject.getPlotSynopsis());
                ImageView moviePosterPath = (ImageView) convertView.findViewById(R.id.detail_poster);
                Picasso.with(getContext()).load(movieObject.getPosterPath()).into(moviePosterPath);
                CheckBox favoriteStar = (CheckBox) convertView.findViewById(R.id.favorite_checkbox);

                Cursor cursor = getContext().getContentResolver()
                        .query(MovieContract.FavoriteMovieEntry.buildMoviesByIdUri(movieObject.getMovieId()),
                                new String[]{MovieContract.FavoriteMovieEntry._ID},
                                null,
                                null,
                                null);
                if (cursor != null && cursor.moveToFirst()){
                    favoriteStar.setChecked(true);
                } else {
                    favoriteStar.setChecked(false);
                }

                favoriteStar.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        boolean checked = ((CheckBox)v).isChecked();
                        MovieDbHelper mMovieDbHelper = new MovieDbHelper(getContext());
                        Cursor movieIdCursor = DetailActivityFragment.movieIdCursor;

                        if (checked){
                            SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();

                            values.put(MovieContract.FavoriteMovieEntry._ID,
                                    movieIdCursor.getString(DetailActivityFragment.COL_MOVIE_ID));
                            values.put(MovieContract.COLUMN_TITLE,
                                    movieIdCursor.getString((DetailActivityFragment.COL_TITLE)));
                            values.put(MovieContract.COLUMN_POSTER_PATH,
                                    movieIdCursor.getString((DetailActivityFragment.COL_POSTER_PATH)));
                            values.put(MovieContract.COLUMN_PLOT,
                                    movieIdCursor.getString((DetailActivityFragment.COL_MOVIE_PLOT)));
                            values.put(MovieContract.COLUMN_VOTE_AVERAGE,
                                    movieIdCursor.getString((DetailActivityFragment.COL_VOTE_AVERAGE)));
                            values.put(MovieContract.COLUMN_RELEASE_DATE,
                                    movieIdCursor.getString((DetailActivityFragment.COL_RELEASE_DATE)));
                            values.put(MovieContract.COLUMN_TRAILERS_AND_REVIEWS,
                                    movieIdCursor.getString((DetailActivityFragment.COL_TRAILERS_AND_REVIEWS)));

                            long newRowId;
                            newRowId = db.insert(
                                    MovieContract.FavoriteMovieEntry.TABLE_NAME,
                                    null,
                                    values
                            );

                        } else{
                            SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
                            String selection = MovieContract.FavoriteMovieEntry._ID + " = ?";
                            String[] selectionArgs = {movieIdCursor.getString((DetailActivityFragment.COL_MOVIE_ID))};
                            db.delete(MovieContract.FavoriteMovieEntry.TABLE_NAME, selection, selectionArgs);

                        }
                    }
                });

                break;
            }
            case DetailActivityFragment.MOVIE_OBJECT_TYPE_TRAILERS: {
                if(convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_trailers, parent, false);
                }
                TextView trailerTitle = (TextView) convertView.findViewById(R.id.trailer_description);
                trailerTitle.setText(movieObject.getTrailerName());
                ImageButton trailerButton = (ImageButton) convertView.findViewById(R.id.trailer_button);
                trailerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("vnd.youtube://" + movieObject.getYouTubeUrl()));
                        getContext().startActivity(intent);
                    }
                });
                break;
            }
            case DetailActivityFragment.MOVIE_OBJECT_TYPE_REVIEWS: {
                if (convertView == null){
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_reviews, parent, false);
                }
                TextView reviewAuthor = (TextView) convertView.findViewById(R.id.author);
                reviewAuthor.setText(movieObject.getReviewsAuthorName());
                TextView reviewContent = (TextView) convertView.findViewById(R.id.reviews);
                reviewContent.setText(movieObject.getReviewsContent());
                break;
            }
        }

        return convertView;
    }

    @Override
    public int getViewTypeCount(){
        return 3;
    }

    @Override
    public int getItemViewType(int position){
        MovieObject movieObject = getItem(position);
        return movieObject.getObjectType();
    }
}
