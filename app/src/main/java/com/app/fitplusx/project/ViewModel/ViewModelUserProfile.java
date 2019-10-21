package com.app.fitplusx.project.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.app.fitplusx.project.Repository.RepositoryUserData;
import com.app.fitplusx.project.Repository.UserDataTable;

public class ViewModelUserProfile extends AndroidViewModel {
    private LiveData<UserDataTable> userData;
    private RepositoryUserData mRepositoryUserData;

    public ViewModelUserProfile(@NonNull Application application) {
        super(application);
        mRepositoryUserData = new RepositoryUserData(application);
        userData = mRepositoryUserData.getSelectedUserTable();
    }

    public LiveData<UserDataTable> getUserData(){
        return userData;
    }
}
