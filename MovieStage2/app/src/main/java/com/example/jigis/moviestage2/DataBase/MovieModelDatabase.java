package com.example.jigis.moviestage2.DataBase;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.jigis.moviestage2.DataBase.TypeConverter.CastConverter;
import com.example.jigis.moviestage2.DataBase.TypeConverter.ReviewConverter;
import com.example.jigis.moviestage2.DataBase.TypeConverter.VideoConverter;
import com.example.jigis.moviestage2.Model.CastModel;
import com.example.jigis.moviestage2.Model.Reviews;
import com.example.jigis.moviestage2.Model.Video;

import java.util.List;

@Entity(tableName = "moviesTable")
public class MovieModelDatabase implements Parcelable {


    @PrimaryKey
    @ColumnInfo(name = "movieId")
    private int id;
    @ColumnInfo(name = "video")
    private boolean video;
    @ColumnInfo(name = "vote_average")
    private double vote_average;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "popularity")
    private double popularity;
    @ColumnInfo(name = "poster_path")
    private String poster_path;
    @ColumnInfo(name = "original_language")
    private String original_language;
    @ColumnInfo(name = "originalTitle")
    private String original_title;
    @ColumnInfo(name = "backdropPath")
    private String backdrop_path;
    @ColumnInfo(name = "adult")
    private boolean adult;
    @ColumnInfo(name = "overview")
    private String overview;
    @ColumnInfo(name = "releaseDate")
    private String release_date;
    //video
    @ColumnInfo(name = "trailerlist")
    @TypeConverters(VideoConverter.class)
    private List<Video> videos;

    //Review
    @ColumnInfo(name = "reviews")
    @TypeConverters(ReviewConverter.class)
    private  List<Reviews> reviews;

    //Cast
    @ColumnInfo(name = "CastChar")
    @TypeConverters(CastConverter.class)
    private  List<CastModel> castModelList;

    public MovieModelDatabase(int id, boolean video, double vote_average, String title, double popularity,
                              String poster_path, String original_language, String original_title,
                              String backdrop_path, boolean adult, String overview, String release_date,
                              List<Video> videos, List<Reviews> reviews, List<CastModel> castModelList) {
        this.id = id;
        this.video = video;
        this.vote_average = vote_average;
        this.title = title;
        this.popularity = popularity;
        this.poster_path = poster_path;
        this.original_language = original_language;
        this.original_title = original_title;
        this.backdrop_path = backdrop_path;
        this.adult = adult;
        this.overview = overview;
        this.release_date = release_date;
        this.videos = videos;
        this.reviews = reviews;
        this.castModelList = castModelList;
    }

    public List<CastModel> getCastModelList() {
        return castModelList;
    }

    protected MovieModelDatabase(Parcel in) {
        id = in.readInt();
        video = in.readByte() != 0;
        vote_average = in.readDouble();
        title = in.readString();
        popularity = in.readDouble();
        poster_path = in.readString();
        original_language = in.readString();
        original_title = in.readString();
        backdrop_path = in.readString();
        adult = in.readByte() != 0;
        overview = in.readString();
        release_date = in.readString();
        videos = in.createTypedArrayList(Video.CREATOR);
        reviews = in.createTypedArrayList(Reviews.CREATOR);
        castModelList=in.createTypedArrayList(CastModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeDouble(vote_average);
        dest.writeString(title);
        dest.writeDouble(popularity);
        dest.writeString(poster_path);
        dest.writeString(original_language);
        dest.writeString(original_title);
        dest.writeString(backdrop_path);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeTypedList(videos);
        dest.writeTypedList(reviews);
        dest.writeTypedList(castModelList);
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public static final Creator<MovieModelDatabase> CREATOR = new Creator<MovieModelDatabase>() {
        @Override
        public MovieModelDatabase createFromParcel(Parcel in) {
            return new MovieModelDatabase(in);
        }

        @Override
        public MovieModelDatabase[] newArray(int size) {
            return new MovieModelDatabase[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public List<Reviews> getReviews() {
        return reviews;
    }

    public void setReviews(List<Reviews> reviews) {
        this.reviews = reviews;
    }


    public void setCastModelList(List<CastModel> castModelList) {
        this.castModelList = castModelList;
    }

    public static Creator<MovieModelDatabase> getCREATOR() {
        return CREATOR;
    }


}
