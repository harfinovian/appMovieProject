package com.example.harfi.appprojectmovie3.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by harfi on 11/18/2016.
 */

public class MovieDetails extends RealmObject{
    @PrimaryKey
    private Long id;
    private String poster_path;
    private String original_title;
    private String overview;
    private String release_date;
    private Integer runtime;
    private Float vote_average;
    private String BASE_URLIMG = "http://image.tmdb.org/t/p/w500";

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getPoster_path() {return poster_path;}
    public void setPoster_path(String poster_path) {this.poster_path = BASE_URLIMG + poster_path;}
    public String getOriginal_title() {return original_title;}
    public void setOriginal_title(String original_title) {this.original_title = original_title;}
    public String getOverview() {return overview;}
    public void setOverview(String overview) {this.overview = overview;}
    public String getRelease_date() {return release_date;}
    public void setRelease_date(String release_date) {this.release_date = release_date;}
    public Integer getRuntime() {return runtime;}
    public void setRuntime(Integer runtime) {this.runtime = runtime;}
    public Float getVote_average() {return vote_average;}
    public void setVote_average(Float vote_average) {this.vote_average = vote_average;}

}
