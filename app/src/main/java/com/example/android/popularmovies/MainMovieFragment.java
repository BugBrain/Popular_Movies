package com.example.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Warren on 5/22/2016.
 */
public class MainMovieFragment extends Fragment{

    public ImageAdapter mImageAdapter;

    private ArrayList<MovieObject> movieList;

    public MainMovieFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey("sort")) {
            movieList = new ArrayList<MovieObject>();
        }
        else {
            movieList = savedInstanceState.getParcelableArrayList("sort");
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("sort", movieList);
        super.onSaveInstanceState(outState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mImageAdapter = new ImageAdapter(getActivity(), movieList);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.movies_grid);
        gridView.setAdapter(mImageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MovieObject movieObject = mImageAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("selectedMovie", movieObject);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
        updateMovies();
    }

    private void updateMovies(){
        FetchMovieTask fetchMovieTask = new FetchMovieTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String setPref = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_default));
        fetchMovieTask.execute(setPref);

    }

    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<MovieObject>>{

        //a tag for logging purposes
        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        private ArrayList<MovieObject> getMovieDataFromJson(String movieJsonStr) throws JSONException{

            //names of JSON objects to extract
            final String OWM_RESULTS = "results";
            final String OWM_ID = "id";
            final String OWM_ORIGINAL_TITLE = "original_title";
            final String OWM_POSTER_PATH = "poster_path";
            final String OWM_PLOT_SYNOPSIS = "overview";
            final String OWM_USER_RATING = "vote_average";
            final String OWM_RELEASE_DATE = "release_date";

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieJsonArray = movieJson.getJSONArray(OWM_RESULTS);

            //initalizes movieobjects arraylist based on size from JSON array
            ArrayList<MovieObject> movieObjects = new ArrayList<>();



            for(int i = 0; i < movieJsonArray.length(); i++){
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


                    movieObjects.add(new MovieObject(originalTitle, id, posterPath, plotSynopsis,
                            voteAverage, releaseDate));

            }

            return movieObjects;

        }




        //params is the setting between popular and top rated
        @Override
        protected ArrayList<MovieObject> doInBackground(String... params){

            //Prepare url connections
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            //To contain JSON response
            String movieJsonStr = null;

            try{
                final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie";
                final String MOVIE_API_KEY = "api_key";
                final String MOVIE_API = getString(R.string.movie_api_key);

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendPath(params[0])
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
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = reader.readLine()) != null){
                    //Cleanup string for debugging readability
                    buffer.append(line +"\n");
                }

                if(buffer.length() == 0){
                    //checks for empty string
                    return  null;
                }
                movieJsonStr = buffer.toString();

            } catch (IOException ex){
                //provide error message
                Log.e(LOG_TAG, "Error ", ex);
                return null;
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

            try {

                return getMovieDataFromJson(movieJsonStr);
            } catch (JSONException ex) {
                Log.e(LOG_TAG, ex.getMessage(), ex);
                ex.printStackTrace();
            }



            return null;

        }

        @Override
        protected void onPostExecute(ArrayList<MovieObject> results)
        {
            if (results != null)
            {
                mImageAdapter.clear();
                for (MovieObject movieObject : results)
                {
                    mImageAdapter.add(movieObject);
                }
            }
        }
    }

}
