package com.app.fitplusx.project.ViewFragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.fitplusx.project.R;
import com.app.fitplusx.project.ViewActivity.ViewActivityBMI;
import com.app.fitplusx.project.ViewActivity.ViewActivityDashboard;
import com.app.fitplusx.project.ViewActivity.ViewActivityPedometer;
import com.app.fitplusx.project.ViewActivity.ViewActivityUserProfile;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewNavigationPhone extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ImageView home;

    public ViewNavigationPhone() {
        // Required empty public constructor
    }

    /**
     * Used to select the correct menu item when pressing a bottom nav icon. This will result in a new activity bring open.
     * @return
     */
    private int getMenuIndex() {
        String activityName = getActivity().getLocalClassName();
        switch(activityName){
            case "ViewActivityDashboard":
                return 0;
            case "findHike":
                return 1;
            case "ViewActivityBMI":
                return 2;
            case "ViewActivityUserProfile":
                return 3;
            case "ViewActivityPedometer":
                return 4;
        }
        return -1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_navigation_phone, container, false);
        BottomNavigationView bottomNav = view.findViewById(R.id.bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(this);
        Menu menu = bottomNav.getMenu();

        int selectedMenuindex = getMenuIndex();
        if (selectedMenuindex == -1)
            menu.setGroupCheckable(0, false, true);
        else
            menu.getItem(selectedMenuindex ).setChecked(true);
        return view;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.nav_home:
                this.startActivity(new Intent(getActivity(), ViewActivityDashboard.class));
                return true;
            case R.id.nav_BMI:
                this.startActivity(new Intent(getActivity(), ViewActivityBMI.class));
                return true;
            case R.id.toolbarAvatar:
                this.startActivity(new Intent(getActivity(), ViewActivityUserProfile.class));
                return true;
            case R.id.nav_Pedometer:
                this.startActivity(new Intent(getActivity(), ViewActivityPedometer.class));
                return true;
            case R.id.nav_hike:
                // Finds hikes near current location with google maps
                // in the future we might add a "hikes at X location"
                // When we do, just modify this string to add additional parameters to the query
//                String hikeLocation = "";
//                String request = "geo:?q=" + hikeLocation + "hikes";
                Uri searchUri = Uri.parse("geo:?q=hikes");
                this.startActivity(new Intent(Intent.ACTION_VIEW, searchUri));
        }
        return false;
    }
}
