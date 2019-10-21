package com.app.fitplusx.project.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.app.fitplusx.project.Repository.RepositoryUserData;
import com.app.fitplusx.project.Repository.UserDataTable;

//TODO THIS CLASS IS ONLY FOR TESTING THE DATABASE AND WILL BE REMOVED.
public class ViewModelWelcome extends AndroidViewModel {
    private RepositoryUserData mRepositoryUserData;
    public UserDataTable mud;
    public ViewModelWelcome(@NonNull Application application) {
        super(application);
        mRepositoryUserData = new RepositoryUserData(application);


        // code to automatically create a user
        // uncomment if you want to put something in the db
        mud = new UserDataTable();
        mud.setUserName("Marshal");
        mud.setName("Marshal");
        mud.setBirthday("Marshal");
        mud.setSex("Marshal");
        mud.setLocation("Marshal");
        mud.setImageUri("Marshal");
        mud.setHeight(10);
        mud.setWeight(100);
        mud.setActivityLevel(9000);
        mud.setGoal(99);
        mud.setDailyCaloricIntake(11);
        mud.setAge(27);
        mud.setPassword("toor");
//        mRepositoryUserData.insertNewUser(mud);
//        System.out.println("\n\n\n\n\n\n\nhererererererererererererererererererere\n\n\n\n\n\n\n\n");
        //code to automatically set the user to Marshal
//        mRepositoryUserData.setSelectedUserData("Marshal");
    }
}
