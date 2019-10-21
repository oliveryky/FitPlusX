package com.app.fitplusx.project.ViewActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.app.fitplusx.project.R;

public class ViewActivityBMI extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bmi);
//        ((TextView) findViewById(R.id.BMIValue)).setText(new DecimalFormat("#.#").format(FitnessCalc.getBMI(0.0, 0.0)) + "");
    }
}
