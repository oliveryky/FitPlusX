package com.app.fitplusx.project.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import com.app.fitplusx.project.Repository.RepositoryUserData;
import com.app.fitplusx.project.Repository.UserDataTable;

public class ViewModelPedometer extends AndroidViewModel {
    private RepositoryUserData mRepositoryUserData;

    public ViewModelPedometer(@NonNull Application application) {
        super(application);
        mRepositoryUserData = new RepositoryUserData(application);
    }

    public LiveData<UserDataTable> getUserTable(){
        return mRepositoryUserData.getSelectedUserTable();
    }

    public void updateUserData(UserDataTable userData) {
        mRepositoryUserData.updateUserData(userData);
    }
    public void updateUserDataS3(final Context content, String userName) {
        mRepositoryUserData.uploadUserDBToS3(content, userName);
    }
}
