package com.example.android.popularmovies;

/**
 * Created by Warren on 6/22/2016.
 */
public class ReviewObject {

    public String reviewsAuthorName;
    public String reviewsContent;
    public String reviewsUrl;

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

    public ReviewObject(String reviewsAuthorName, String reviewsContent, String reviewsUrl) {

        this.reviewsAuthorName = reviewsAuthorName;
        this.reviewsContent = reviewsContent;
        this.reviewsUrl = reviewsUrl;
    }
}
