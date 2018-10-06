package com.edumaite.adam.edumiate_poc.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.edumaite.adam.edumiate_poc.models.App;

import java.util.List;

@Dao
public interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(App app);

    @Query("DELETE FROM apps")
    void deleteAll();

    @Query("SELECT * from apps ORDER BY name ASC")
    LiveData<List<App>> getAllApps();
}


