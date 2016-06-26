package com.example.android.popularmovies;

/**
 * Created by Warren on 5/22/2016.
 */
public class MovieObject {

    private int objectType;
    private String originalTitle;
    private String plotSynopsis;
    private String ratings;
    private String releaseDate;
    private String posterPath;
    private String trailerName;
    private String youTubeUrl;
    private String reviewsAuthorName;
    private String reviewsContent;
    private String reviewsUrl;
    private String movieId;

    public String getPosterPath() {
        return posterPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public String getRatings() {
        return ratings;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getObjectType() {

        return objectType;
    }

    public MovieObject(int objectType, String trailerName, String youTubeUrl) {
        this.objectType = objectType;
        this.trailerName = trailerName;
        this.youTubeUrl = youTubeUrl;
    }

    public MovieObject(int objectType, String reviewsAuthorName, String reviewsContent, String reviewsUrl) {

        this.objectType = objectType;
        this.reviewsAuthorName = reviewsAuthorName;
        this.reviewsContent = reviewsContent;
        this.reviewsUrl = reviewsUrl;
    }

    public String getMovieId() {
        return movieId;
    }

    public MovieObject(int objectType, String movieId, String originalTitle, String plotSynopsis, String ratings, String releaseDate, String posterPath) {

        this.objectType = objectType;
        this.movieId = movieId;
        this.originalTitle = originalTitle;
        this.plotSynopsis = plotSynopsis;
        this.ratings = ratings;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
    }

    public String getReviewsAuthorName() {
        return reviewsAuthorName;
    }

    public void setReviewsAuthorName(String reviewsAuthorName) {
        this.reviewsAuthorName = reviewsAuthorName;
    }

    public String getReviewsContent() {
        return reviewsContent;
    }

    public void setReviewsContent(String reviewsContent) {
        this.reviewsContent = reviewsContent;
    }

    public String getReviewsUrl() {
        return reviewsUrl;
    }

    public void setReviewsUrl(String reviewsUrl) {
        this.reviewsUrl = reviewsUrl;
    }

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
}
