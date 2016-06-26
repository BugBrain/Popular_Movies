package com.example.android.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Warren on 6/22/2016.
 */
public class TrailersAdapter extends ArrayAdapter<TrailerObject> {

    //initialize ArrayAdapter
    public TrailersAdapter(Activity context, ArrayList<TrailerObject> trailerObjects){
        super(context, 0, trailerObjects);
    }

    //Grabs view for TrailersAdapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        TrailerObject trailerObject = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_trailers, parent, false);
        }

        TextView trailerDescription = (TextView) convertView.findViewById(R.id.trailer_description);

        trailerDescription.setText(trailerObject.trailerName);

        return convertView;
    }
}
