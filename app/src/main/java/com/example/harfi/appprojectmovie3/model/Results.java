package com.example.harfi.appprojectmovie3.model;

import io.realm.RealmObject;

/**
 * Created by harfi on 11/13/2016.
 */

public class Results extends RealmObject{

    private String BASE_URLIMG = "http://image.tmdb.org/t/p/w500";
    private String poster_path;
    private Long id;
    private String category;

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getPoster_path() {return poster_path;}
    public void setPoster_path(String poster_path) {this.poster_path = BASE_URLIMG + poster_path;}
    public String getCategory() {return category;}
    public void setCategory(String category) {this.category = category;}

}

