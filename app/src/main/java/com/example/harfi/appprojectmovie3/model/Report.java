package com.example.harfi.appprojectmovie3.model;


import java.util.List;

/**
 * Created by harfi on 11/13/2016.
 */

public class Report{

    private List<Results> results;

    public Report(List<Results> Result) {
        this.results = Result;
    }

    public List<Results> getResults() {return results;}
    public void setResults(List<Results> results) {this.results = results;}

}
