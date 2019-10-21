package com.app.fitplusx.project.ViewFragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import com.app.fitplusx.project.ViewModel.ViewModelPedometer;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import static java.lang.String.valueOf;

public class ViewFragmentPedometer extends Fragment {

    public ViewFragmentPedometer() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pedometer, container, false);
    }
}
