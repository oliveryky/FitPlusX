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
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fitplusx.project.R;
import com.app.fitplusx.project.Repository.UserDataTable;
import com.app.fitplusx.project.ViewModel.ViewModelPedometer;

import static java.lang.String.valueOf;

public class ViewActivityPedometer extends AppCompatActivity implements SensorEventListener {

    private ViewModelPedometer vmPedometer;
    private UserDataTable userDataTable;
    private TextView pedometerValue;
    private long counter = -1;

    private SensorManager sensorManager;
    private Sensor stepSensor, gestureSensor;
    private boolean running = false;

    // gesture variables
    private boolean firstTime;
    private double last_z, now_z;
    private final double mThreshold = 1.0;
    private long lastTime;


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

        // setup sensor stuff
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        gestureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        lastTime = 0;
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

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    // re-register the sensors
    @Override
    protected void onResume() {
        super.onResume();
        gestureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        if (gestureSensor != null) {
            sensorManager.registerListener(this, gestureSensor, SensorManager.SENSOR_DELAY_UI);
        }
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (stepSensor != null) sensorManager.unregisterListener(this, stepSensor);
        if(gestureSensor != null) sensorManager.unregisterListener(this, gestureSensor);
    }



    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_GAME_ROTATION_VECTOR) {
            now_z = event.values[1];

            if (firstTime) {
                double dy = Math.abs(last_z - now_z);
                long curTime = System.currentTimeMillis();
                //Check if the values of acceleration have changed on any pair of axes
                if (dy > mThreshold) {
                    if ((curTime - lastTime) > 1000 && dy > mThreshold) {
                        lastTime = curTime;

                        if (!running) {
                            running = true;
//                            pedometerValue.setText(valueOf(dy));
                            showToast("Pedometer is ON");
                        } else {
                            running = false;
                            showToast("Pedometer is OFF");
                        }
                    }
                }
            }
            last_z = now_z;
            firstTime = true;
        }

        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER && running) {
            if (counter == -1) {
                counter = (long) event.values[0];
            }
            String value = ((long) event.values[0] - counter) + "";
            pedometerValue.setText(value);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
