package com.app.tuskan.moviestage2.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yakup on 29.6.2018.
 */
@Entity (tableName = "movie")
public class Movie implements Parcelable {

    @PrimaryKey
    public int id;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo (name = "release_date")
    public String releaseDate;
    @ColumnInfo (name = "movie_poster")
    public String moviePoster;
    @ColumnInfo (name = "backdrop_path")
    public String backdropPath;
    @ColumnInfo (name = "vote_avarage")
    public String voteAvarage;
    @ColumnInfo (name = "plot_synopsis")
    public String plotSynopsis;

    public Movie(int id, String title, String releaseDate, String moviePoster, String backdropPath, String voteAvarage, String plotSynopsis) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.moviePoster = moviePoster;
        this.backdropPath = backdropPath;
        this.voteAvarage = voteAvarage;
        this.plotSynopsis = plotSynopsis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getBackdrop_path() {
        return backdropPath;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdropPath = backdrop_path;
    }

    public String getVoteAvarage() {
        return voteAvarage;
    }

    public void setVoteAvarage(String voteAvarage) {
        this.voteAvarage = voteAvarage;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.releaseDate);
        dest.writeString(this.moviePoster);
        dest.writeString(this.backdropPath);
        dest.writeString(this.voteAvarage);
        dest.writeString(this.plotSynopsis);
    }

    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.releaseDate = in.readString();
        this.moviePoster = in.readString();
        this.backdropPath = in.readString();
        this.voteAvarage = in.readString();
        this.plotSynopsis = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public static Creator<Movie> getCREATOR() {
        return CREATOR;
    }
}
