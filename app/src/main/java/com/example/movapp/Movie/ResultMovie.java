package com.example.movapp.Movie;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultMovie {
    private String id;
    private String title;
    private String overview;

    @SerializedName("vote_average")
    private String voteAverage;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("poster_path")
    private String imgUrl;

    @SerializedName("backdrop_path")
    private String imgBack;

    public ResultMovie() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getBackdropPath() {return imgBack;}

    public void setImgBack(String imgBack) { this.imgBack = imgBack; }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

}