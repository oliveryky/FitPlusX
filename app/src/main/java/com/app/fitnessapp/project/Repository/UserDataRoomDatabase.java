package com.app.fitnessapp.project.Repository;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


@Database(entities = {UserDataTable.class}, version = 1, exportSchema = false)
public abstract class UserDataRoomDatabase extends RoomDatabase {

    private static volatile UserDataRoomDatabase mInstance;
    public abstract UserDataDao userDataDao();


    static synchronized UserDataRoomDatabase getDatabase(final Context context){
        if(mInstance==null) {
            mInstance = Room.databaseBuilder(context.getApplicationContext(),
                    UserDataRoomDatabase.class, "userData.db").build();
        }
        return mInstance;
    }
}
