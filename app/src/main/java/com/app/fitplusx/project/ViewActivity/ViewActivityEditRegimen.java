package com.app.fitplusx.project.ViewActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.app.fitplusx.project.FitnessCalc;
import com.app.fitplusx.project.R;
import com.app.fitplusx.project.Repository.UserDataTable;
import com.app.fitplusx.project.ViewModel.ViewModelEditRegimen;
import com.app.fitplusx.project.ViewModel.ViewNavigationPhone;
import com.app.fitplusx.project.ViewModel.ViewNavigationTablet;


public class ViewActivityEditRegimen extends AppCompatActivity implements android.widget.CompoundButton.OnCheckedChangeListener,
        View.OnClickListener {
    ToggleButton lose, maintain, gain, sedentary, active;
    EditText weightGoal;
    TextView weightGoalPrefix;
    private boolean temp = false;
    private ViewModelEditRegimen mViewModelEditRegimen;
    private UserDataTable userDataTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_regimen);

        //Create the view model
        mViewModelEditRegimen = ViewModelProviders.of(this).get(ViewModelEditRegimen.class);
        //Set the observer
        mViewModelEditRegimen.getUserTable().observe(this,userDataObserver);

        weightGoal = findViewById(R.id.weightGoal);
        weightGoalPrefix = findViewById(R.id.weightGoalPrefix);
        lose = (ToggleButton) findViewById(R.id.loseToggle);
        maintain = (ToggleButton) findViewById(R.id.maintainToggle);
        gain = (ToggleButton) findViewById(R.id.gainToggle);
        sedentary = (ToggleButton) findViewById(R.id.sedentaryToggle);
        active = (ToggleButton) findViewById(R.id.activeToggle);
        lose.setOnCheckedChangeListener(this);
        maintain.setOnCheckedChangeListener(this);
        gain.setOnCheckedChangeListener(this);
        sedentary.setOnCheckedChangeListener(this);
        active.setOnCheckedChangeListener(this);

        findViewById(R.id.createRegimeButton).setOnClickListener(this);
        weightGoal.setOnClickListener(this);

        Intent intent = getIntent();
        if(intent.hasExtra("newUser")) {
            temp = true;
            //TODO implement logic to change submit button text and navigate to ViewActivityDashboard activity when creating a new regimen for the first time.
        } else {
            if(isTablet()){
                ViewNavigationTablet nav = new ViewNavigationTablet();
                FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
                fTrans.replace(R.id.editRegimenNav, nav, "bottom_nav_frag");
                fTrans.commit();
            }else {
                //Create the fragment
                ViewNavigationPhone viewNavigationPhoneFragment = new ViewNavigationPhone();

                //Replace the fragment container
                FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
                fTrans.replace(R.id.editRegimenBottomNav, viewNavigationPhoneFragment, "bottom_nav_frag");
                fTrans.commit();
            }

        }
    }

    //create an observer that watches the LiveData<UserDataTable> object
    final Observer<UserDataTable> userDataObserver  = new Observer<UserDataTable>() {
        @Override
        public void onChanged(@Nullable final UserDataTable userData) {
            // Update the UI if this data variable changes
            if(userData != null) {
                userDataTable = userData;
                double goalVal = userDataTable.getGoal();
                weightGoal.setText(Math.abs(userDataTable.getGoal()) + "", TextView.BufferType.EDITABLE);
                if(goalVal < 0) {
                    weightGoalPrefix.setText("Lose");
                    lose.setChecked(true);
                }else if(goalVal == 0.0) {
                    weightGoalPrefix.setText("Maintain");
                    maintain.setChecked(true);
                }else {
                    weightGoalPrefix.setText("Gain");
                    gain.setChecked(true);
                }

                double activityLvl = userDataTable.getActivityLevel();
                if(activityLvl < 1.3) {
                    sedentary.setChecked(true);
                }else {
                    active.setChecked(true);
                }
            }
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.loseToggle:
                if (isChecked){
                    weightGoalPrefix.setText("Lose");
                    gain.setChecked(false);
                    maintain.setChecked(false);
                    lose.setChecked(true);
                } else {
                    lose.setChecked(false);
                }

                break;
            case R.id.maintainToggle:
                if (isChecked) {
                    weightGoalPrefix.setText("Maintain");
                    weightGoal.setText(0 + "", TextView.BufferType.EDITABLE);
                    gain.setChecked(false);
                    lose.setChecked(false);
                    maintain.setChecked(true);
                } else {
                    maintain.setChecked(false);
                }
                break;
            case R.id.gainToggle:
                if (isChecked) {
                    weightGoalPrefix.setText("Gain");
                    lose.setChecked(false);
                    maintain.setChecked(false);
                    gain.setChecked(true);
                } else {
                    gain.setChecked(false);
                }
                break;
            case R.id.sedentaryToggle:
                if (isChecked) {
                    active.setChecked(false);
                    sedentary.setChecked(true);
                } else {
                    sedentary.setChecked(false);
                }
                break;
            case R.id.activeToggle:
                if (isChecked) {
                    sedentary.setChecked(false);
                    active.setChecked(true);
                } else {
                    active.setChecked(false);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.createRegimeButton:
                //TODO change navigation based on intent.
                double goal = Double.parseDouble(this.weightGoal.getText().toString());
                if(goal > 2){
                    String warning ="Your goal is potentially dangerous.\nYour calorie intake may be capped.";
                    Toast toast = Toast.makeText(getApplicationContext(), warning, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                userDataTable.setGoal(this.lose.isChecked() ? goal * -1 : goal);
                if (!lose.isChecked() && !maintain.isChecked() && !gain.isChecked()) {
                    String warning = "Please select a goal type";
                    Toast toast = Toast.makeText(getApplicationContext(), warning, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    break;
                } else if (!sedentary.isChecked() && !active.isChecked()) {
                    String warning ="Please select an activity level";
                    Toast toast = Toast.makeText(getApplicationContext(), warning, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    break;
                } else if(sedentary.isChecked()) {
                    userDataTable.setActivityLevel(1.2);
                } else if(active.isChecked()) {
                    userDataTable.setActivityLevel(1.725);
                }

                int calories = FitnessCalc.calculateDailyCalorieIntake(userDataTable.getGoal(), userDataTable.getSex(),
                        userDataTable.getWeight(), userDataTable.getHeight(), userDataTable.getAge(), userDataTable.getActivityLevel());
                if (goal > 2 && calories > 4000)
                    calories = 4000;
                userDataTable.setDailyCaloricIntake(calories);

                Intent intent = new Intent(this, ViewActivityDashboard.class);
                if(temp) {
                    intent.putExtra("complete", true);
                }
                mViewModelEditRegimen.updateUserData(userDataTable);
                this.startActivity(intent);
                break;
            default:
                break;
        }
    }

    boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }
}