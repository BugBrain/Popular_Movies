package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Warren on 5/22/2016.
 */
public class MovieObject implements Parcelable{
    String originalTitle, id, posterPath, plotSynopsis,voteAverage, releaseDate;

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public MovieObject(String originalTitle, String id, String posterPath, String plotSynopsis,
                       String voteAverage, String releaseDate){
        this.originalTitle = originalTitle;
        this.id = id;
        this.posterPath = posterPath;
        this.plotSynopsis = plotSynopsis;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    private MovieObject(Parcel in){
        originalTitle = in.readString();
        id = in.readString();
        posterPath = in.readString();
        plotSynopsis = in.readString();
        voteAverage = in.readString();
        releaseDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(originalTitle);
        parcel.writeString(id);
        parcel.writeString(posterPath);
        parcel.writeString(plotSynopsis);
        parcel.writeString(voteAverage);
        parcel.writeString(releaseDate);
    }

    public static final Parcelable.Creator<MovieObject> CREATOR = new Parcelable.Creator<MovieObject>() {
        @Override
        public MovieObject createFromParcel(Parcel parcel) {
            return new MovieObject(parcel);
        }

        @Override
        public MovieObject[] newArray(int i) {
            return new MovieObject[i];
        }

    };
}
