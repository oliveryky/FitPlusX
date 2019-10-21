package com.app.fitplusx.project.ViewFragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.fitplusx.project.FitnessCalc;
import com.app.fitplusx.project.R;
import com.app.fitplusx.project.Repository.UserDataTable;
import com.app.fitplusx.project.ViewModel.ViewModelBMI;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import static java.lang.String.valueOf;


public class ViewFragmentBMI extends Fragment {
    private TextView mBMIValue, mBMIRange;
    private View v;
    private ViewModelBMI mViewModelBMI;

    private SensorManager mSensorManager;
    private Sensor mRotationalSensor;
    private boolean pedometerOn, mNotFirstTime;
    private double last_z, now_z;
    private final double mThreshold = 1.0;

    public ViewFragmentBMI() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_bmi, container, false);
        mBMIValue = (TextView) v.findViewById(R.id.BMIValue);
        mViewModelBMI = ViewModelProviders.of(this).get(ViewModelBMI.class);
        mViewModelBMI.getUserTable().observe(this, nameObserver);
        mBMIRange = (TextView) v.findViewById(R.id.BMIRange);

        // phone gesture stuff
        mSensorManager = (SensorManager) v.getContext().getSystemService(Context.SENSOR_SERVICE);
        mRotationalSensor = (Sensor)  mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        pedometerOn = false;

        return v;
    }

    /**
     * BMI changing function listener thing
     */
    final Observer<UserDataTable> nameObserver  = new Observer<UserDataTable>() {
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
