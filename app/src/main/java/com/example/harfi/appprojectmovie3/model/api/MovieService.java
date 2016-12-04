package com.example.harfi.appprojectmovie3.model.api;

import com.example.harfi.appprojectmovie3.model.MovieDetails;
import com.example.harfi.appprojectmovie3.model.Report;
import com.example.harfi.appprojectmovie3.model.VideoList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by harfi on 11/13/2016.
 */

public interface MovieService {
    @GET("3/movie/{category}?api_key=f3d6c212736aa31051a3ec995ec96151")
    Call<Report> getMovie(@Path("category") String category);

    @GET("3/movie/{movieId}?api_key=f3d6c212736aa31051a3ec995ec96151")
    Call<MovieDetails> getDetails(@Path("movieId") Long movieId);

    @GET("3/movie/{movieId}/videos?api_key=f3d6c212736aa31051a3ec995ec96151")
    Call<VideoList> getVideo(@Path("movieId") Long movieId);
}
