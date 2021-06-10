package com.example.movapp.Movie;

import com.example.movapp.Movie.ResultMovie;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResponse {
    @SerializedName("page")
    private int page;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("results")
    private List<ResultMovie> resultMovies;

    public List<ResultMovie> getResultMovies() {
        return resultMovies;
    }

    public int getTotalPages() { return totalPages; }

    public int getTotalResults() { return totalResults; }

    public int getPage() { return page; }
}
