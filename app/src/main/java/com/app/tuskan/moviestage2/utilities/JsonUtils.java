package com.app.tuskan.moviestage2.utilities;

import com.app.tuskan.moviestage2.models.Comment;
import com.app.tuskan.moviestage2.models.Movie;
import com.app.tuskan.moviestage2.models.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Yakup on 29.6.2018.
 */

public class JsonUtils {
    private final static String RESULTS = "results";
    private final static String ID = "id";
    private final static String TITLE = "title";
    private final static String RELEASE_DATE = "release_date";
    private final static String POSTER_PATH = "poster_path";
    private final static String BACKDROP_PATH = "backdrop_path";
    private final static String VOTE_AVARAGE = "vote_average";
    private final static String PLOT_SYNOPSIS = "overview";
    private final static String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    private final static String KEY = "key";
    private final static String NAME = "name";

    private final static String AUTHOR = "author";
    private final static String CONTENT = "content";




    public static ArrayList<Movie> parseSandwichJson(String json) throws JSONException {

        ArrayList<Movie> movies = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonResultArray = jsonObject.optJSONArray(RESULTS);

        for(int i = 0; i < jsonResultArray.length(); i++){

            JSONObject jsonValueObject = jsonResultArray.getJSONObject(i);
            int id = jsonValueObject.optInt(ID);
            String title = jsonValueObject.optString(TITLE);
            String releaseDate = jsonValueObject.optString(RELEASE_DATE);
            String moviePoster = IMAGE_BASE_URL + jsonValueObject.optString(POSTER_PATH);
            String backdropPath = IMAGE_BASE_URL + jsonValueObject.optString(BACKDROP_PATH);
            String voteAvarage = jsonValueObject.optString(VOTE_AVARAGE);
            String plotSynopsis = jsonValueObject.optString(PLOT_SYNOPSIS);

            Movie movie = new Movie(id, title, releaseDate, moviePoster, backdropPath, voteAvarage, plotSynopsis);

            movies.add(movie);
        }

        return movies;
    }

    public static ArrayList<Video> parseVideoResultJson(String json) throws JSONException {
        ArrayList<Video> videos = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.optJSONArray(RESULTS);

        for(int i = 0; i < jsonArray.length(); i++){
            Video video = new Video();
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            video.setId(jsonObject.optInt(ID));
            video.setVideoTitle(jsonObject1.optString(NAME));
            video.setVideoKey(jsonObject1.optString(KEY));
            videos.add(video);
        }

        return videos;
    }

    public static ArrayList<Comment> parseCommentResultJson(String json) throws JSONException {
        ArrayList<Comment> comments = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.optJSONArray(RESULTS);

        for(int i = 0; i < jsonArray.length(); i++){
            Comment comment = new Comment();
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            comment.setName(jsonObject1.optString(AUTHOR));
            comment.setComment(jsonObject1.optString(CONTENT));
            comments.add(comment);
        }

        return comments;
    }

}
