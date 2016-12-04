package com.example.harfi.appprojectmovie3.model;

import io.realm.RealmObject;

/**
 * Created by harfi on 11/18/2016.
 */

public class VideoDetails extends RealmObject{

    private Long movieId;
    private String key;

    public Long getMovieId() {return movieId;}
    public void setMovieId(Long movieId) {this.movieId = movieId;}
    public String getKey() {return key;}
    public void setKey(String key) {this.key = key;}

}
