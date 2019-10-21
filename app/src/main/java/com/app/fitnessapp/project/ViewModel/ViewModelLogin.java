package com.app.fitnessapp.project.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.app.fitnessapp.project.Repository.RepositoryUserData;
import com.app.fitnessapp.project.Repository.UserDataTable;

import java.util.List;

public class ViewModelLogin extends AndroidViewModel {
    private LiveData<UserDataTable> userData;
    private RepositoryUserData mRepositoryUserData;

    public ViewModelLogin(@NonNull Application application) {
        super(application);
        mRepositoryUserData = new RepositoryUserData(application);
        userData = mRepositoryUserData.getSelectedUserTable();
    }

    public LiveData<UserDataTable> getUserData() {
        return userData;
    }

    public LiveData<UserDataTable> getUserData(String userName) {
        return mRepositoryUserData.getUserDataByUserName(userName);
    }

    public void addNewUser(UserDataTable userTable) {
        mRepositoryUserData.insertNewUser(userTable);
    }

    public void setUserData(String userName) {
        mRepositoryUserData.setSelectedUserData(userName);
    }

    public LiveData<List<UserDataTable>> getAllUsers() {
        return mRepositoryUserData.getAllUsers();
    }
}
