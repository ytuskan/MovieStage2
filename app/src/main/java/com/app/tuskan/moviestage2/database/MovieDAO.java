package com.app.tuskan.moviestage2.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.app.tuskan.moviestage2.models.Movie;

import java.util.List;

/**
 * Created by Yakup on 3.7.2018.
 */
@Dao
public interface MovieDAO {

    @Query("SELECT * FROM movie ORDER BY id")
    LiveData<List<Movie>> loadAllTask();

    @Insert
    void insertMovie(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

}
