package com.example.movapp.networks;

import com.example.movapp.Movie.MovResponse;
import com.example.movapp.Movie.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiInterface {
    // https://api.themoviedb.org/3/movie/now_playing

    @GET("/3/movie/{category}")
    Call<MovieResponse> getMovie (
            @Path("category") String category,
            @Query("api_key") String api_key,
            @Query("language") String lang,
            @Query("page") int page
    );

    @GET("/3/search/movie/")
    Call<MovieResponse> getSearchMovie(
            @Query("api_key") String api_key,
            @Query("query") String query
    );

    @GET("/3/movie/{id}")
    Call<MovResponse> getMovieById(
            @Path("id") String id,
            @Query("api_key") String api_key
    );



}
