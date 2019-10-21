package com.app.fitnessapp.project.ViewActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.app.fitnessapp.project.R;

public class ViewActivityWelcome extends AppCompatActivity implements View.OnClickListener {
    // uncomment to populate db with "Marshal"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        findViewById(R.id.welcomeLoginButton).setOnClickListener(this);
        findViewById(R.id.welcomeSignUpButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, ViewActivityLogin.class);
        switch (v.getId()) {
            case R.id.welcomeLoginButton:
                //TODO add functionality that takes the user to the signin/up page if this is not enough
                // Testing accessing data
                this.startActivity(intent);
                break;
            case R.id.welcomeSignUpButton:
                intent.putExtra("signup", this.getLocalClassName());
                this.startActivity(intent);
                break;
            default:
                break;
        }
    }
}
