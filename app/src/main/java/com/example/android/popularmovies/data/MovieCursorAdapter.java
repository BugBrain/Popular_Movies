package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Warren on 6/17/2016.
 */
public class MovieCursorAdapter extends CursorAdapter{

    public static class ViewHolder{
        public ImageView posterView;

        public ViewHolder(View view){
            posterView = (ImageView) view.findViewById(R.id.movie_image);
        }
    }


    public MovieCursorAdapter(Context context, Cursor cursor, int flags){
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        int layoutId = R.layout.image_item;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        Picasso.with(context).load(cursor.getString(MovieColumns.COL_POSTER_PATH))
                .into(viewHolder.posterView);

    }


}
