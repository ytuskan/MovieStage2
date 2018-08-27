package com.app.tuskan.moviestage2.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.app.tuskan.moviestage2.database.AppDatabase;

import java.util.List;

/**
 * Created by Yakup on 5.7.2018.
 */

public class MainViewModel extends AndroidViewModel {

    private static final String LOG_TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Movie>> datas;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(this.getApplication());
        Log.d(LOG_TAG, "Actively retrieving the movies from the database");
        datas = appDatabase.movieDAO().loadAllTask();
    }

    public LiveData<List<Movie>> getDatas(){
        return datas;
    }


}
