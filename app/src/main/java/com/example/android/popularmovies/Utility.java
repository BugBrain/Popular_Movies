package com.example.android.popularmovies;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Warren on 6/19/2016.
 */
public class Utility {


    public static final String LOG_TAG = Utility.class.getSimpleName();

    public static String getViewSettings(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_sort_key),
                context.getString(R.string.pref_sort_default));
    }

    public static void getTrailerObjects(
            String trailerJsonString, ArrayList<MovieObject> movieObjectArrayList) throws JSONException{

        //names of JSON objects to extract
        final String OWM_TRAILERS = "trailers";
        final String OWM_TRAILERS_YOUTUBE_ARRAY = "youtube";
        final String OWM_TRAILER_NAME = "name";
        final String OWM_TRAILER_YOUTUBE_KEY = "source";


        try {

            JSONObject movieJson = new JSONObject(trailerJsonString);

            //first get Trailer information
            JSONObject trailerJsonObject = movieJson.getJSONObject(OWM_TRAILERS);

            //get second array from array since it always represents youtube arrays
            JSONArray youTubeJsonArray = trailerJsonObject.getJSONArray(OWM_TRAILERS_YOUTUBE_ARRAY);


            for (int i = 0; i < youTubeJsonArray.length(); i++) {
                String trailerName;
                String youTubeUrl;

                //getting JSON object representing the trailer
                JSONObject trailer = youTubeJsonArray.getJSONObject(i);

                trailerName = trailer.getString(OWM_TRAILER_NAME);

                //create trailer url to youtube
                youTubeUrl = trailer.getString(OWM_TRAILER_YOUTUBE_KEY);

                MovieObject movieObject = new MovieObject(
                        DetailActivityFragment.MOVIE_OBJECT_TYPE_TRAILERS, trailerName, youTubeUrl);

                movieObjectArrayList.add(movieObject);
            }

        } catch (JSONException e){
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }

    public static void getReviewObjects(
            String reviewJsonString, ArrayList<MovieObject> movieObjectArrayList) throws JSONException{

        final String OWM_REVIEWS = "reviews";
        final String OWM_REVIEWS_RESULTS = "results";
        final String OWM_REVIEWS_AUTHOR = "author";
        final String OWM_REVIEWS_CONTENT = "content";
        final String OWM_REVIEWS_URL = "url";

        JSONObject movieJson = new JSONObject(reviewJsonString);

        JSONObject reviewsJsonObject = movieJson.getJSONObject(OWM_REVIEWS);


        JSONArray reviewsResultsJsonArray = reviewsJsonObject.getJSONArray(OWM_REVIEWS_RESULTS);

        ArrayList<ContentProviderOperation> reviewsBatchOperations = new ArrayList<ContentProviderOperation>(reviewsResultsJsonArray.length());

        for (int i = 0; i < reviewsResultsJsonArray.length(); i++) {
            String reviewsAuthorName;
            String reviewsContent;
            String reviewsUrl;

            //getting JSON object representing the review
            JSONObject review = reviewsResultsJsonArray.getJSONObject(i);

            reviewsAuthorName = review.getString(OWM_REVIEWS_AUTHOR) + " says: ";
            reviewsContent = review.getString(OWM_REVIEWS_CONTENT);
            reviewsUrl = review.getString(OWM_REVIEWS_URL);

            MovieObject movieObject = new MovieObject(
                    DetailActivityFragment.MOVIE_OBJECT_TYPE_REVIEWS,
                    reviewsAuthorName, reviewsContent, reviewsUrl);

            movieObjectArrayList.add(movieObject);
        }
    }
}
