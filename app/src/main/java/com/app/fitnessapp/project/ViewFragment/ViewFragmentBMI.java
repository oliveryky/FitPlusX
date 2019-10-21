package com.app.fitnessapp.project.ViewFragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.fitnessapp.project.FitnessCalc;
import com.app.fitnessapp.project.R;
import com.app.fitnessapp.project.Repository.UserDataTable;
import com.app.fitnessapp.project.ViewModel.ViewModelBMI;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import static java.lang.String.valueOf;


public class ViewFragmentBMI extends Fragment {
    TextView mBMIValue, mBMIRange;
    View v;
    ViewModelBMI mViewModelBMI;

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

        return v;
    }

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

}
