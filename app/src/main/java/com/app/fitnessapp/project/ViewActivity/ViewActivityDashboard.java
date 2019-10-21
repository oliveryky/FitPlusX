package com.app.fitnessapp.project.ViewActivity;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;

        import com.app.fitnessapp.project.R;

public class ViewActivityDashboard extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        findViewById(R.id.changeGoalButton).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changeGoalButton:
                Intent intent = new Intent(this, ViewActivityEditRegimen.class);
                this.startActivity(intent);
                break;

            default:
                break;
        }
    }

}