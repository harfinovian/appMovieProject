package com.example.harfi.appprojectmovie3.model.api;

import com.example.harfi.appprojectmovie3.BuildConfig;
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

    public static final String API_KEY= BuildConfig.API_KEY;

    @GET("3/movie/{category}?api_key="+API_KEY)
    Call<Report> getMovie(@Path("category") String category);

    @GET("3/movie/{movieId}?api_key="+API_KEY)
    Call<MovieDetails> getDetails(@Path("movieId") Long movieId);

    @GET("3/movie/{movieId}/videos?api_key=f"+API_KEY)
    Call<VideoList> getVideo(@Path("movieId") Long movieId);
}
