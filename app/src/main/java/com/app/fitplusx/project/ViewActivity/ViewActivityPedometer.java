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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);

//        final GestureDetector gd = new GestureDetector(this.getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
//
//            //here is the method for double tap
//            @Override
//            public boolean onDoubleTap(MotionEvent e) {
//                showToast("Pedometer is " + (running ? "OFF" : "ON"));
//                running = !running;
//                if(!running) counter = -1;
//                return true;
//            }
//
//            @Override
//            public void onLongPress(MotionEvent e) {
//                super.onLongPress(e);
//
//            }
//
//            @Override
//            public boolean onDoubleTapEvent(MotionEvent e) {
//                return true;
//            }
//
//            @Override
//            public boolean onDown(MotionEvent e) {
//                return true;
//            }

//        });

        //Create the view model
        vmPedometer = ViewModelProviders.of(this).get(ViewModelPedometer.class);

        //Set the observer
        (vmPedometer.getUserTable()).observe(this, stepObserver);
        pedometerValue = findViewById(R.id.PedometerValue);
        pedometerValue.setText("0");
//        pedometerValue.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                return gd.onTouchEvent(event);
//            }
//        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        gestureSensor = (Sensor) sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
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

    private boolean firstTime = true;
    private double last_z, now_z;
    private final double mThreshold = 0.5;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GAME_ROTATION_VECTOR) {
            now_z = event.values[1];
            if (!firstTime) {
                double dy = Math.abs(last_z - now_z);

                //Check if the values of acceleration have changed on any pair of axes
                if (dy > mThreshold) {

                    // Start and Stop the pedometer here

                    // REMOVE THIS CODE WHEN DONE TESTING!!!!!!!!!!!
                    pedometerValue.setText(valueOf(dy));
//                showToast("Pedometer is ON");
                    Toast.makeText(this, "AHHHHHHHHHHHH", Toast.LENGTH_LONG).show();
                    running = true;
                    ////////////////////////////////////////////////

                }
            }
            last_z = now_z;
            firstTime = false;
        }

        if (running) {
            if (counter == -1) {
                counter = (long) event.values[0];
            }
            pedometerValue.setText(((long) event.values[0] - counter) + "");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
