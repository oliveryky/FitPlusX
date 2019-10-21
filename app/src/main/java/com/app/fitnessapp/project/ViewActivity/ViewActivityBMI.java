package com.app.fitnessapp.project.ViewActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.app.fitnessapp.project.R;

import java.text.DecimalFormat;

public class ViewActivityBMI extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bmi);
//        ((TextView) findViewById(R.id.BMIValue)).setText(new DecimalFormat("#.#").format(FitnessCalc.getBMI(0.0, 0.0)) + "");
    }
}
