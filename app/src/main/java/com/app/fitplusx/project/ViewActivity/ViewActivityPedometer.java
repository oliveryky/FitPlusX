package com.app.fitplusx.project.ViewActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fitplusx.project.R;
import com.app.fitplusx.project.Repository.UserDataTable;
import com.app.fitplusx.project.ViewModel.ViewModelPedometer;

public class ViewActivityPedometer extends AppCompatActivity implements SensorEventListener{

    private ViewModelPedometer vmPedometer;
    private UserDataTable userDataTable;
    private TextView pedometerValue;
    private long counter = -1;

    private SensorManager sensorManager;
    private Sensor sensor;
    private boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);

        //Create the view model
        vmPedometer = ViewModelProviders.of(this).get(ViewModelPedometer.class);

        //Set the observer
        (vmPedometer.getUserTable()).observe(this, stepObserver);
        pedometerValue = findViewById(R.id.PedometerValue);
        pedometerValue.setText("0");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }

    //create an observer that watches the LiveData<WeatherData> object
    final Observer<UserDataTable> stepObserver = new Observer<UserDataTable>() {
        @Override
        public void onChanged(@Nullable final UserDataTable userData) {
            // Update the UI if this data variable changes

            if (userData != null) {
                userDataTable = userData;
//                pedometerValue.setText(0);

            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(sensor != null) {
            sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_UI);

        }else {
            Toast.makeText(this, "Sensor wtf", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(running) {
            if(counter == -1) {
                counter = (long) event.values[0];
            }
            pedometerValue.setText(((long) event.values[0] - counter) + "");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
