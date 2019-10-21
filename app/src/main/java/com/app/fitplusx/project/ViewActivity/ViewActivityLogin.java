package com.app.fitplusx.project.ViewActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fitplusx.project.R;
import com.app.fitplusx.project.Repository.UserDataTable;
import com.app.fitplusx.project.ViewModel.ViewModelLogin;

//TODO implement logic to remove toolbar and bottom nav when creating a new user for the first time.
//TODO implement logic to change submit button text and navigate to edit regimen activity when creating a new use for the first time.
public class ViewActivityLogin extends AppCompatActivity implements
        View.OnClickListener, AdapterView.OnItemSelectedListener {
    private boolean isSignUp, signedUp;
    private Button button;
    private EditText username, password;
    private ViewModelLogin vmLogin;
    private UserDataTable userDataTable;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isSignUp = false;
        signedUp = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button = findViewById(R.id.loginButton);
        button.setOnClickListener(this);

        //Create the view model
        vmLogin = ViewModelProviders.of(this).get(ViewModelLogin.class);

        //Set the observer
//        (vmLogin.getUserData()).observe(this, profileObserver);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        Intent intent = getIntent();
        if ((isSignUp = intent.hasExtra("signup"))) {
            ((TextView) findViewById(R.id.loginTitle)).setText("Sign Up");
            button.setText("Continue");
        }else {

        }
    }

    final Observer<UserDataTable> profileObserver = new Observer<UserDataTable>() {
        @Override
        public void onChanged(@Nullable final UserDataTable userData) {
            // Update the UI if this data variable changes

            if(userData != null) {
                userDataTable = userData;
            }
            login();
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                if(username.getText().toString().isEmpty()) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Username is empty!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    break;
                }
                if(username.getText().toString().length() > 100) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Username too long!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    break;
                }

                if(password.getText().toString().isEmpty()) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Password is empty!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    break;
                }

                (vmLogin.getUserData(username.getText().toString())).observe(this, profileObserver);
                break;
            default:
                break;
        }
    }

    private void login() {
        if(isSignUp) {
            if(userDataTable == null) {
                signedUp = true;
                vmLogin.addNewUser(new UserDataTable(username.getText().toString(), getHash(password.getText().toString())));
                vmLogin.setUserData(username.getText().toString());
                Intent intent = new Intent(this, ViewActivityEditProfile.class);
                intent.putExtra("newUser", this.getLocalClassName());
                this.startActivity(intent);

            } else if (!signedUp) {
                Toast toast = Toast.makeText(getApplicationContext(), "Username taken!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                userDataTable = null;
            }
            return;
        }

        if(userDataTable == null) {
            Toast toast = Toast.makeText(getApplicationContext(), "Nonexistent user!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        if(!userDataTable.getPassword().equals(getHash(password.getText().toString()))) {
            Toast toast = Toast.makeText(getApplicationContext(), "Wrong Password!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        Intent intent = new Intent(this, ViewActivityDashboard.class);
        intent.putExtra("newUser", this.getLocalClassName());
        vmLogin.setUserData(username.getText().toString());
        this.startActivity(intent);
    }

    //TODO: implement hash
    private String getHash(String str) {
        return str;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // TODO What do we want to do when the user selects a sex?
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO What do we want to do when the user doesn't select any sex?
    }

    boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }
}
