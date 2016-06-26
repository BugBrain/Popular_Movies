package com.example.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.popularmovies.sync.MovieSyncAdapter;


public class MainActivity extends AppCompatActivity implements MainMovieFragment.Callback{

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DETAIL_ACTIVITY_FRAGMENT_TAG = "DFTAG";

    private String mViewSetting;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "onCreate");
        MovieSyncAdapter.syncImmediately(this, Utility.getViewSettings(this));
        mViewSetting = Utility.getViewSettings(this);


        setContentView(R.layout.activity_main);
        if(findViewById(R.id.movie_detail_container) != null){
            mTwoPane = true;
            if(savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailActivityFragment(),
                                DETAIL_ACTIVITY_FRAGMENT_TAG).commit();
            }
            else {
                mTwoPane = false;
                getSupportActionBar().setElevation(0f);
            }
        }

        MainMovieFragment mainMovieFragment = (MainMovieFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_movie_grid);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume(){
        super.onResume();

        Log.v("Main Activity", "onResume triggered");

        String viewSelection = Utility.getViewSettings( this );
        Log.v("Main Activity", "onResume viewSelection: " + viewSelection + " mViewSetting: " + mViewSetting);

//         update the location in our second pane using the fragment manager
        if (!viewSelection.equals(mViewSetting)) {
            Log.v(LOG_TAG, "onResume: mViewSetting != viewSelection");
            MainMovieFragment mainMovieFragment =
                    (MainMovieFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.fragment_main);
            if (null != mainMovieFragment) {
                mainMovieFragment.onMovieChangeSettingsChange();
                Log.v(LOG_TAG, "onResume: mainMovieFragment != null ");
            }
            DetailActivityFragment detailActivityFragment =
                    (DetailActivityFragment) getSupportFragmentManager()
                            .findFragmentByTag(DETAIL_ACTIVITY_FRAGMENT_TAG);
            if(null != detailActivityFragment){
                detailActivityFragment.onMovieChanged(viewSelection);
            }
            mViewSetting = viewSelection;
        }

    }

    @Override
    public void onItemSelected(Uri contentUri){
        if(mTwoPane){
            Bundle args = new Bundle();
            args.putParcelable(DetailActivityFragment.DETAIL_URI, contentUri);

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAIL_ACTIVITY_FRAGMENT_TAG)
                    .commit();
        }else {
            Intent intent = new Intent(this, DetailActivity.class).setData(contentUri);
            startActivity(intent);
        }

    }


}
