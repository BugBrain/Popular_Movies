package com.example.android.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Warren on 5/22/2016.
 */
public class ImageAdapter extends ArrayAdapter<MovieObject> {



    //initialize ArrayAdapter
    public ImageAdapter(Activity context, List<MovieObject> movieObjects){
        super(context, 0, movieObjects);

    }

    //Grabs view for ImageAdapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        
        //grabs the proper MovieObject from Array
        MovieObject movieObject = getItem(position);
        
        
        if(convertView == null){
            //inflates layout if a new view
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.image_item, parent, false);
        }
        
        
        //locates proper ImageView to place movie posters
        ImageView posterView = (ImageView) convertView.findViewById(R.id.movie_image);
        //sets the movie poster to the ImageView
//        Picasso.with(getContext()).load(movieObject.getPosterPath()).into(posterView);



        //returns view
        return convertView;

    }
}
