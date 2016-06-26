package com.example.android.popularmovies;

/**
 * Created by Warren on 6/22/2016.
 */
public class TrailerObject {

    public String trailerName;
    public String youTubeUrl;

    public String getTrailerName() {
        return trailerName;
    }

    public void setTrailerName(String trailerName) {
        this.trailerName = trailerName;
    }

    public String getYouTubeUrl() {
        return youTubeUrl;
    }

    public void setYouTubeUrl(String youTubeUrl) {
        this.youTubeUrl = youTubeUrl;
    }

    public TrailerObject(String trailerName, String youTubeUrl) {

        this.trailerName = trailerName;
        this.youTubeUrl = youTubeUrl;
    }
}
