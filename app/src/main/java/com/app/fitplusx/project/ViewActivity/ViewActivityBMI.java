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

import com.app.fitplusx.project.FitnessCalc;
import com.app.fitplusx.project.R;
import com.app.fitplusx.project.Repository.UserDataTable;
import com.app.fitplusx.project.ViewModel.ViewModelBMI;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import static java.lang.String.valueOf;

public class ViewActivityBMI extends AppCompatActivity {

    private TextView mBMIValue, mBMIRange;
    private ViewModelBMI mViewModelBMI;

    private SensorManager mSensorManager;
    private Sensor mRotationalSensor;
    private boolean pedometerOn, mNotFirstTime;
    private double last_z, now_z;
    private final double mThreshold = 1.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bmi);
//        ((TextView) findViewById(R.id.BMIValue)).setText(new DecimalFormat("#.#").format(FitnessCalc.getBMI(0.0, 0.0)) + "");

        mBMIValue = (TextView) findViewById(R.id.BMIValue);
        mViewModelBMI = ViewModelProviders.of(this).get(ViewModelBMI.class);
        mViewModelBMI.getUserTable().observe(this, bmiObserver);
        mBMIRange = (TextView) findViewById(R.id.BMIRange);

        // phone gesture stuff
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mRotationalSensor = (Sensor)  mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        pedometerOn = false;
    }

    /**
     * BMI changing function listener thing
     */
    final Observer<UserDataTable> bmiObserver  = new Observer<UserDataTable>() {
        @Override
        public void onChanged(@Nullable final UserDataTable userData) {
            // Update the UI if this data variable changes
            DecimalFormat df = new DecimalFormat("#.#");
            df.setRoundingMode(RoundingMode.CEILING);
            if(userData!=null) {

                double bmi = FitnessCalc.getBMI(userData.getWeight(), userData.getHeight());
                String BMI = valueOf(df.format(bmi));
                mBMIValue.setText(BMI);
                if (bmi < 18.5) {
                    mBMIRange.setText("Underweight");
                } else if (bmi <= 24.9) {
                    mBMIRange.setText("Normal");
                } else if (bmi <= 29.9) {
                    mBMIRange.setText("Overweight");
                } else if (bmi <= 40) {
                    mBMIRange.setText("Obese");
                } else {
                    mBMIRange.setText("Morbidly Obese");
                }

            } else {
                mBMIValue.setText("E R R O R");
            }
        }
    };

    /**
     * Gesture code
     */
    private SensorEventListener mListener = new SensorEventListener() {


        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            // Get rotation about the y axis
            now_z = sensorEvent.values[1];

            if(mNotFirstTime){
                double dy = Math.abs(last_z - now_z);

                //Check if the values of acceleration have changed on any pair of axes
                if(dy > mThreshold){

                    // Start and Stop the pedometer here

                    // REMOVE THIS CODE WHEN DONE TESTING!!!!!!!!!!!
                    mBMIValue.setText(valueOf(dy));

                    ////////////////////////////////////////////////


                }
            }
            last_z = now_z;
            mNotFirstTime = true;
        }


        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if(mRotationalSensor!=null){
            mSensorManager.registerListener(mListener,mRotationalSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mRotationalSensor!=null){
            mSensorManager.unregisterListener(mListener);
        }
    }

    /*    End of Gesture Code      */
}
