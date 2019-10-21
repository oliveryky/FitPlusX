package com.app.fitplusx.project.Repository;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface UserDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserDataTable userDataTable);

    @Update
    void updateUser(UserDataTable userDataTable);

    @Query("DELETE FROM user_data_table")
    void deleteAll();

    @Query("SELECT * FROM user_data_table ORDER BY userName ASC")
    LiveData<List<UserDataTable>> getAll();

    @Query("SELECT * from user_data_table WHERE userName == :userName")
    LiveData<UserDataTable> getUserByUserName(String userName);

    // TODO: method to update a user's information
}
