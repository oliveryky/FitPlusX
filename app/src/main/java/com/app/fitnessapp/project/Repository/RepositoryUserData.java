package com.app.fitnessapp.project.Repository;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class RepositoryUserData {
    //TODO How do we want to manage the ModelUserData vs UserData Table? There is no way to convert the UserDataTable to a ModelUserData.
    private static LiveData<UserDataTable> mSelectedUserData;
    private static LiveData<UserDataTable> mSelectedUserDataSync;
    private UserDataDao mUserDataDao;
    private static LiveData<List<UserDataTable>>allUsers;

    public RepositoryUserData(Application application){
        UserDataRoomDatabase db = UserDataRoomDatabase.getDatabase(application);
        mUserDataDao = db.userDataDao();
        //implement additional items to execute needed queries and initializes the member variables.
        allUsers = mUserDataDao.getAll();
    }

    /**
     * Oliver You will want to use this method when checking if a user exists while logging in.
     * If the user has input the right password use setSelectedUserData() to set that user's info in
     * the repo.
     *
     * @param userName - The name of the user. This class is used to check if a eser exists.
     * @return a LiveData object containing the userDataTable object else null;
     */
    public LiveData<UserDataTable> getUserDataByUserName(String userName) {
        new getUserAsyncTask(mUserDataDao).execute(userName);
        // This fixes the issues when double pressing the sign-in button. Although I hate it.
        while (mSelectedUserDataSync == null) { }
        return mSelectedUserDataSync;
    }

    /**
     * Sets the user's data for the repo, allowing use to call this method to get the user's data once
     * they are logged in.
     *
     * @param userName - Sets the data of the selected user data, Allowing other to access the same user data
     */
    public void setSelectedUserData(String userName) { mSelectedUserData = mUserDataDao.getUserByUserName(userName); }

    /**
     *
     * @return The selected user's data once the user logs in.
     */
    public LiveData<UserDataTable> getSelectedUserTable() { return mSelectedUserData; }

    /**
     * This will update the user's data in the database, assuming their name has not changed.
     * @param userData UserDataTable object with the updated user data.
     */
    public void updateUserData(UserDataTable userData) {
        new updateUserAsyncTask(mUserDataDao).execute(userData);
    }

    /**
     * This should be used to insert a new user, but if the same name is used it will overwrite
     * the previous user. We will want to check this and be careful here.
     * @param newUser - UserDataTable object of the new user's data.
     */
    public void insertNewUser(UserDataTable newUser){
        // TODO can we just pass the model? @Tristan & @Oliver?
        new insertAsyncTask(mUserDataDao).execute(newUser);
    }

    public LiveData<List<UserDataTable>> getAllUsers(){
        return allUsers;
    }

    private static class insertAsyncTask extends AsyncTask<UserDataTable,Void,Void>{
        private UserDataDao mAsyncTaskDao;
        insertAsyncTask(UserDataDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(UserDataTable... userDataTables) {
            mAsyncTaskDao.insert(userDataTables[0]);
            return null;
        }
    }

    private static class updateUserAsyncTask extends AsyncTask<UserDataTable,Void,Void>{
        private UserDataDao mAsyncTaskDao;
        updateUserAsyncTask(UserDataDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(UserDataTable... userDataTables) {
            mAsyncTaskDao.updateUser(userDataTables[0]);
            return null;
        }
    }

    private static class getUserAsyncTask extends AsyncTask<String,Void,Void>{
        private UserDataDao mAsyncTaskDao;
        getUserAsyncTask(UserDataDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            mSelectedUserDataSync = mAsyncTaskDao.getUserByUserName(strings[0]);
            return null;
        }
    }
}