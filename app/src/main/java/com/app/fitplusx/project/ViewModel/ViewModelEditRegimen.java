package com.app.fitplusx.project.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.app.fitplusx.project.Repository.RepositoryUserData;
import com.app.fitplusx.project.Repository.UserDataTable;

public class ViewModelEditRegimen extends AndroidViewModel {
    private RepositoryUserData mRepositoryUserData;
    private static final int CALORIE_LB_RATIO = 3500;
    private static final int DAYS_PER_WEEK = 7;

    public ViewModelEditRegimen(@NonNull Application application) {
        super(application);
        mRepositoryUserData = new RepositoryUserData(application);
    }

    public LiveData<UserDataTable> getUserTable(){
        return mRepositoryUserData.getSelectedUserTable();
    }

    public void updateUserData(UserDataTable userData) {
        mRepositoryUserData.updateUserData(userData);
    }

}
