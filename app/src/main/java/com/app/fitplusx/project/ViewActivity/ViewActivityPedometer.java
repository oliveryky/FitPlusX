package com.app.fitplusx.project.ViewActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.fitplusx.project.R;
import com.app.fitplusx.project.Repository.UserDataTable;
import com.app.fitplusx.project.Utils;
import com.app.fitplusx.project.ViewFragment.ViewFragmentPedometer;
import com.app.fitplusx.project.ViewModel.ViewModelPedometer;
import com.app.fitplusx.project.ViewModel.ViewModelUserProfile;

public class ViewActivityPedometer extends AppCompatActivity {

    private ViewModelPedometer vmPedometer;
    private UserDataTable userDataTable;
    private TextView pedometerValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);
        ((TextView) findViewById(R.id.PedometerValue)).setText("0");

        //Create the view model
        vmPedometer = ViewModelProviders.of(this).get(ViewModelPedometer.class);

        //Set the observer
        (vmPedometer.getUserTable()).observe(this, stepObserver);
    }

    //create an observer that watches the LiveData<WeatherData> object
    final Observer<UserDataTable> stepObserver = new Observer<UserDataTable>() {
        @Override
        public void onChanged(@Nullable final UserDataTable userData) {
            // Update the UI if this data variable changes

            if(userData != null) {
                userDataTable = userData;

            }
        }
    };
}
