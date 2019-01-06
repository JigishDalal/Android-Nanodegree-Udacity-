package com.example.jigis.moviestage2.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MovieResponse implements Serializable {
   //page
    @SerializedName("page")
    private int Page;
    //result total
    @SerializedName("total_results")
    private  int total_results;


    //total page
    @SerializedName("total_pages")
    private  int total_pages;
    //movie list results
    @SerializedName("results")
    private ArrayList<Movie> results = null;

    public int getPage() {
        return Page;
    }

    public void setPage(int page) {
        Page = page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public ArrayList<Movie> getResults() {
        return results;
    }

    public void setResults(ArrayList<Movie> results) {
        this.results = results;
    }
}
