package com.app.tuskan.moviestage2.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.tuskan.moviestage2.BuildConfig;
import com.app.tuskan.moviestage2.R;
import com.app.tuskan.moviestage2.adapters.MovieAdapter;
import com.app.tuskan.moviestage2.models.MainViewModel;
import com.app.tuskan.moviestage2.models.Movie;
import com.app.tuskan.moviestage2.utilities.JsonUtils;
import com.app.tuskan.moviestage2.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    public final static String LIST_STATE_KEY = "recycler_list_state";
    Parcelable listState;

    private final static String MVDB_POPULAR_BASE_URL =
            "https://api.themoviedb.org/3/movie/popular?";

    private final static String MVDB_TOPRATED_BASE_URL =
            "https://api.themoviedb.org/3/movie/top_rated?";

    private String apiKey = BuildConfig.THE_GUARDIAN_API_KEY;

    @BindView(R.id.loading_indicator_progressbar)
    ProgressBar mLoadingIndicator;


    @BindView(R.id.movie_list_recycler_view)
    RecyclerView movieListRecyclerView;

    @BindView(R.id.sorting_layout)
    LinearLayout sortinglayout;

    @BindView(R.id.sort_by_popularity_button)
    Button popularityButton;

    @BindView(R.id.sort_by_rating_button)
    Button ratingButton;

    @BindView(R.id.show_favorites)
    Button showFavoritesButton;

    private static List<Movie> movieList;

    private MovieAdapter movieAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private void triggers() {
        popularityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeMvdbSearchQuery(MVDB_POPULAR_BASE_URL, apiKey);
                hideSortinglayout();
                Toast.makeText(MainActivity.this, getResources().getString(R.string.sorting_by_popularity), Toast.LENGTH_LONG).show();
            }
        });

        ratingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeMvdbSearchQuery(MVDB_TOPRATED_BASE_URL, apiKey);
                hideSortinglayout();
                Toast.makeText(MainActivity.this, getResources().getString(R.string.sorting_by_rating), Toast.LENGTH_LONG).show();
            }
        });
        showFavoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupViewModel();
                hideSortinglayout();
                Toast.makeText(MainActivity.this, getResources().getString(R.string.showing_favorites), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        triggers();

        if(savedInstanceState == null){
            makeMvdbSearchQuery(MVDB_POPULAR_BASE_URL, apiKey);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        listState = layoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, listState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null)
            listState = savedInstanceState.getParcelable(LIST_STATE_KEY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (listState != null) {
            initRecyclerView(movieList);
            movieListRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    private void setupViewModel() {
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getDatas().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                Log.d(TAG, "Updating list of movies from LiveData in ViewModel");
                movieList = movies;
                initRecyclerView(movies);
            }
        });
    }


    public class MVDBQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
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

            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (mvdbSearchResults != null && !mvdbSearchResults.equals("")) {
                try {
                    movieList = JsonUtils.parseSandwichJson(mvdbSearchResults);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initRecyclerView(movieList);

            }
        }
    }

    private void makeMvdbSearchQuery(String sortType, String apiKey) {
        URL mvdbSearchUrl = NetworkUtils.buildUrl(sortType, apiKey);
        new MVDBQueryTask().execute(mvdbSearchUrl);
    }

    private void initRecyclerView(List<Movie> list) {
        movieAdapter = new MovieAdapter(this, list);
        movieListRecyclerView.setAdapter(movieAdapter);
        layoutManager = new GridLayoutManager(this, numberOfColumns());
        movieListRecyclerView.setLayoutManager(layoutManager);
        movieListRecyclerView.setHasFixedSize(true);
        movieListRecyclerView.scrollToPosition(1);
        movieAdapter.notifyDataSetChanged();
    }


    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.sort_settings_menu:
                showSortingLayout();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSortingLayout() {
        sortinglayout.setVisibility(View.VISIBLE);
    }

    private void hideSortinglayout() {
        sortinglayout.setVisibility(View.GONE);
    }
}
