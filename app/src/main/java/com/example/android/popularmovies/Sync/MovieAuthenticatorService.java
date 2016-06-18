package com.example.android.popularmovies.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Warren on 6/18/2016.
 */
public class MovieAuthenticatorService extends Service {

    private MovieAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new MovieAuthenticator(this);


    }

    @Override
    public IBinder onBind(Intent intent){
        return mAuthenticator.getIBinder();
    }
}
