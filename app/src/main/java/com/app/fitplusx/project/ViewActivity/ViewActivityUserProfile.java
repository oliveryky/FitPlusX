package com.app.fitplusx.project.ViewActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.fitplusx.project.R;
import com.app.fitplusx.project.Repository.UserDataTable;
import com.app.fitplusx.project.Utils;
import com.app.fitplusx.project.ViewModel.ViewModelUserProfile;

public class ViewActivityUserProfile extends AppCompatActivity implements View.OnClickListener {

    private Button editInfo;
    private ViewModelUserProfile vmUserProfile;
    private UserDataTable userDataTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //Create the view model
        vmUserProfile = ViewModelProviders.of(this).get(ViewModelUserProfile.class);

        //Set the observer
        (vmUserProfile.getUserData()).observe(this, profileObserver);

        editInfo = findViewById(R.id.userProfileEditButton);
        editInfo.setOnClickListener(this);

        //TODO: this is where get image
//        Bitmap image;
//        if((image = Utils.getBitmap(userDataTable.getImageUri())) != null) {
//            ((ImageView) findViewById(R.id.userProfilePhoto)).setImageBitmap(image);
//        }
    }

    //create an observer that watches the LiveData<WeatherData> object
    final Observer<UserDataTable> profileObserver = new Observer<UserDataTable>() {
        @Override
        public void onChanged(@Nullable final UserDataTable userData) {
            // Update the UI if this data variable changes

            if(userData != null) {
                userDataTable = userData;
                ((TextView) findViewById(R.id.userProfileName)).setText(userDataTable.getName());
                ((TextView) findViewById(R.id.userProfileHeightVar)).setText(Utils.heightToString(userDataTable.getHeight()));
                ((TextView) findViewById(R.id.userProfileWeightVar)).setText(userDataTable.getWeight() + " lbs");
                ((TextView) findViewById(R.id.userProfileSexVar)).setText(userDataTable.getSex());
                ((TextView) findViewById(R.id.userProfileAgeVar)).setText(Utils.getAge(userDataTable.getBirthday()) + "");
                Bitmap image;
                if((image = Utils.getBitmap(userDataTable.getImageUri())) != null) {
                    ((ImageView) findViewById(R.id.userProfilePhoto)).setImageBitmap(image);
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userProfileEditButton:
                Intent intent = new Intent(this, ViewActivityEditProfile.class);
                this.startActivity(intent);
                break;

            default:
                break;
        }
    }
}
