package com.example.android.popularmovies.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.MovieColumns;
import com.example.android.popularmovies.data.MovieProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by Warren on 6/18/2016.
 */
public class MovieSyncAdapter extends AbstractThreadedSyncAdapter {

    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;

    public static final String SYNC_PREFS = "prefs";

    public final String LOG_TAG = MovieSyncAdapter.class.getSimpleName();

    public MovieSyncAdapter(Context context, boolean autoInitialize){
        super(context,autoInitialize);
    }

    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult){
        Log.d(LOG_TAG, "Starting sync");

        //Prepare url connections
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        //To contain JSON response
        String movieJsonStr = null;

        String params = extras.getString(SYNC_PREFS, "popular");

        try{
            final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie";
            final String MOVIE_API_KEY = "api_key";
            final String MOVIE_API = getContext().getString(R.string.movie_api_key);

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendPath(params)
                    .appendQueryParameter(MOVIE_API_KEY, MOVIE_API)
                    .build();

            URL url = new URL(builtUri.toString());

            //creates request and opens connection to Mdb
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //reads input stream into string
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if(inputStream == null){
                //if input stream is empty
                return;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while((line = reader.readLine()) != null){
                //Cleanup string for debugging readability
                buffer.append(line +"\n");
            }

            if(buffer.length() == 0){
                //checks for empty string
                return;
            }
            movieJsonStr = buffer.toString();
            getMovieDataFromJson(movieJsonStr);


        } catch (IOException ex){
            //provide error message
            Log.e(LOG_TAG, "Error ", ex);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(reader != null){
                try{
                    reader.close();
                }catch (final IOException ex){
                    Log.e(LOG_TAG, "Error closing stream", ex);
                }
            }
        }

    }

    private void getMovieDataFromJson(String movieJsonStr) throws JSONException{

        //names of JSON objects to extract
        final String OWM_RESULTS = "results";
        final String OWM_ID = "id";
        final String OWM_ORIGINAL_TITLE = "original_title";
        final String OWM_POSTER_PATH = "poster_path";
        final String OWM_PLOT_SYNOPSIS = "overview";
        final String OWM_USER_RATING = "vote_average";
        final String OWM_RELEASE_DATE = "release_date";

        try {

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieJsonArray = movieJson.getJSONArray(OWM_RESULTS);

            Vector<ContentValues> cVVector = new Vector<ContentValues>(movieJsonArray.length());



            for (int i = 0; i < movieJsonArray.length(); i++) {
                String originalTitle;
                String id;
                String posterPath;
                String plotSynopsis;
                String voteAverage;
                String releaseDate;

                //getting JSON object representing the movie
                JSONObject movie = movieJsonArray.getJSONObject(i);

                originalTitle = movie.getString(OWM_ORIGINAL_TITLE);
                id = movie.getString(OWM_ID);

                posterPath = Uri.parse("http://image.tmdb.org/t/p/w185").buildUpon()
                        .appendEncodedPath(movie.getString(OWM_POSTER_PATH)).build().toString();

                plotSynopsis = movie.getString(OWM_PLOT_SYNOPSIS);
                voteAverage = movie.getString(OWM_USER_RATING);
                releaseDate = movie.getString(OWM_RELEASE_DATE);


                ContentValues movieValues = new ContentValues();

                movieValues.put(MovieColumns.TITLE, originalTitle);
                movieValues.put(MovieColumns.MOVIE_ID, id);
                movieValues.put(MovieColumns.POSTER_PATH, posterPath);
                movieValues.put(MovieColumns.MOVIE_PLOT, plotSynopsis);
                movieValues.put(MovieColumns.VOTE_AVERAGE, voteAverage);
                movieValues.put(MovieColumns.RELEASE_DATE, releaseDate);

                cVVector.add(movieValues);
            }


            if(cVVector.size() > 0){
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                getContext().getContentResolver().bulkInsert(MovieProvider.Movies.Content_URI, cvArray);
            }

            Log.d(LOG_TAG, "Sync Complete. " + cVVector.size() + " Inserted");
        } catch (JSONException e){
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

    }

    public static void syncImmediately(Context context, String setViewPref) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putString(SYNC_PREFS, setViewPref);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        MovieSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}
