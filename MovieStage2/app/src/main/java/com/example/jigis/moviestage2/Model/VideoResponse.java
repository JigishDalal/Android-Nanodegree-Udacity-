package com.example.jigis.moviestage2.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoResponse {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("results")
        @Expose
        private List<Video> video = null;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public List<Video> getResults() {
            return video;
        }

        public void setResults(List<Video> results) {
            this.video = results;
        }

    }
