package com.app.tuskan.moviestage2.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tuskan.moviestage2.BuildConfig;
import com.app.tuskan.moviestage2.R;
import com.app.tuskan.moviestage2.adapters.CommentAdapter;
import com.app.tuskan.moviestage2.adapters.MovieAdapter;
import com.app.tuskan.moviestage2.adapters.VideoAdapter;
import com.app.tuskan.moviestage2.database.AppDatabase;
import com.app.tuskan.moviestage2.models.Comment;
import com.app.tuskan.moviestage2.models.MainViewModel;
import com.app.tuskan.moviestage2.models.Movie;
import com.app.tuskan.moviestage2.models.Video;
import com.app.tuskan.moviestage2.utilities.JsonUtils;
import com.app.tuskan.moviestage2.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";

    private String apiKey = BuildConfig.THE_GUARDIAN_API_KEY;

    @BindView(R.id.detail_poster_image_view)
    ImageView posterImageView;

    @BindView(R.id.detail_title_textview)
    TextView titleTextview;

    @BindView(R.id.detail_rating_textview)
    TextView ratingTextView;

    @BindView(R.id.detail_date_textview)
    TextView dateTextView;

    @BindView(R.id.detail_summary_textview)
    TextView summaryTextView;

    @BindView(R.id.detail_videos_recyclerview)
    RecyclerView videosRecyclerView;

    @BindView(R.id.detail_comment_recyclerview)
    RecyclerView commentRecyclerView;

    @BindView(R.id.detail_favorite_imagebutton)
    ImageButton favoriteImageButton;

    private ArrayList<Video> videos = new ArrayList<>();
    private ArrayList<Comment> comments = new ArrayList<>();
    private VideoAdapter videoAdapter;
    private CommentAdapter commentAdapter;
    private RecyclerView.LayoutManager videoLayoutManager;
    private RecyclerView.LayoutManager commentLayoutManager;

    private Movie movie;
    private List<Movie> movieList;
    private AppDatabase appDatabase;

    private void setViewValues() {
        Intent intent = getIntent();
        Bundle parcelable = intent.getExtras();
        movie = parcelable.getParcelable(MovieAdapter.INTENT_KEY);
        Picasso.with(this)
                .load(movie.getBackdrop_path())
                .error(R.mipmap.ic_launcher)
                .into(posterImageView);

        titleTextview.setText(movie.getTitle());
        ratingTextView.setText(movie.getVoteAvarage());
        dateTextView.setText(movie.getReleaseDate());
        summaryTextView.setText(movie.getPlotSynopsis());

    }

    private void setTriggers() {
        favoriteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkId(movieList, movie.getId())){
                    appDatabase.movieDAO().deleteMovie(movie);
                } else{
                    Movie dbMovie = new Movie(
                            movie.getId(),
                            movie.getTitle(),
                            movie.getReleaseDate(),
                            movie.getMoviePoster(),
                            movie.getBackdrop_path(),
                            movie.getVoteAvarage(),
                            movie.getPlotSynopsis()
                    );
                    appDatabase.movieDAO().insertMovie(dbMovie);
                }
            }
        });
    }

    private boolean checkId(List<Movie> movies, int id) {
        for (int i = 0; i < movies.size(); i++) {
            int mid = movies.get(i).getId();
            if (mid == id) {
                return true;
            }
        }
        return false;
    }
    private void setStarColor(List<Movie> favorites){

        if(checkId(favorites, movie.getId())){
            favoriteImageButton.setImageResource(R.mipmap.ic_favorite);
        }else{
            favoriteImageButton.setImageResource(R.mipmap.ic_not_favorite);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appDatabase = AppDatabase.getInstance(getApplicationContext());
        ButterKnife.bind(this);
        setViewValues();
        setTriggers();
        makeMvdbVideoSearchQuery(BASE_URL + movie.getId() + "/videos?", apiKey);
        makeMvdbCommentSearchQuery(BASE_URL + movie.getId() + "/reviews?", apiKey);
        retrieveTasks();
    }

    private void retrieveTasks() {
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getDatas().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                Log.d(TAG, "Updating list of movies from LiveData in ViewModel");
                setStarColor(movies);
                movieList = movies;

            }
        });
    }

    public class MVDBVideoQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String mvdbSearchResults = null;
            try {
                mvdbSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mvdbSearchResults;
        }

        @Override
        protected void onPostExecute(String mvdbSearchResults) {

            if (mvdbSearchResults != null && !mvdbSearchResults.equals("")) {

                try {
                    videos = JsonUtils.parseVideoResultJson(mvdbSearchResults);

                    initVideoRecyclerView(videos);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    public class MVDBCommentQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String mvdbSearchResults = null;
            try {
                mvdbSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mvdbSearchResults;
        }

        @Override
        protected void onPostExecute(String mvdbSearchResults) {

            if (mvdbSearchResults != null && !mvdbSearchResults.equals("")) {

                try {
                    comments = JsonUtils.parseCommentResultJson(mvdbSearchResults);

                    initCommentRecyclerView(comments);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private void initVideoRecyclerView(ArrayList<Video> list) {
        videoAdapter = new VideoAdapter(this, list);
        videosRecyclerView.setAdapter(videoAdapter);
        videoLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        videosRecyclerView.setLayoutManager(videoLayoutManager);
        videosRecyclerView.setHasFixedSize(true);
        videosRecyclerView.scrollToPosition(1);
    }

    private void initCommentRecyclerView(ArrayList<Comment> list) {
        commentAdapter = new CommentAdapter(this, list);
        commentRecyclerView.setAdapter(commentAdapter);
        commentLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        commentRecyclerView.setLayoutManager(commentLayoutManager);
        commentRecyclerView.setHasFixedSize(true);
        commentRecyclerView.scrollToPosition(1);
    }

    private void makeMvdbVideoSearchQuery(String sortType, String apiKey) {
        URL mvdbSearchUrl = NetworkUtils.buildUrl(sortType, apiKey);
        new MVDBVideoQueryTask().execute(mvdbSearchUrl);
    }

    private void makeMvdbCommentSearchQuery(String sortType, String apiKey) {
        URL mvdbSearchUrl = NetworkUtils.buildUrl(sortType, apiKey);
        new MVDBCommentQueryTask().execute(mvdbSearchUrl);
    }


}
