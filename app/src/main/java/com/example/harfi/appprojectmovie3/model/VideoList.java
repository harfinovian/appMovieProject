package com.example.harfi.appprojectmovie3.model;

import java.util.List;

/**
 * Created by harfi on 11/18/2016.
 */

public class VideoList{

    private Long id;
    private List<VideoDetails> results;

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public List<VideoDetails> getResults() {return results;}
    public void setResults(List<VideoDetails> results) {this.results = results;}

}
