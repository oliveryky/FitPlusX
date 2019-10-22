package com.app.fitplusx.project.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.app.fitplusx.project.Repository.RepositoryUserData;
import com.app.fitplusx.project.Repository.UserDataTable;

public class ViewModelEditProfile extends AndroidViewModel {
    private LiveData<UserDataTable> userData;
    private RepositoryUserData mRepositoryUserData;

    public ViewModelEditProfile(@NonNull Application application) {
        super(application);
        mRepositoryUserData = new RepositoryUserData(application);
        userData = mRepositoryUserData.getSelectedUserTable();
    }

    public LiveData<UserDataTable> getUserData(){
        return userData;
    }

    public void updateUserData(UserDataTable userData) {
        mRepositoryUserData.updateUserData(userData);
    }

    public void updateUserDataS3(final Context content, String userName) {
        mRepositoryUserData.uploadUserDBToS3(content, userName);
    }
}
