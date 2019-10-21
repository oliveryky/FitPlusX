package com.app.fitnessapp.project.ViewModel;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.app.fitnessapp.project.R;
import com.app.fitnessapp.project.ViewActivity.ViewActivityBMI;
import com.app.fitnessapp.project.ViewActivity.ViewActivityDashboard;
import com.app.fitnessapp.project.ViewActivity.ViewActivityUserProfile;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewNavigationTablet extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    public ViewNavigationTablet() {
        // Required empty public constructor
    }

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
        }
        return -1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_navigation_tablet, container, false);
        NavigationView leftNav = view.findViewById(R.id.tabletLeftNav);
        leftNav.setNavigationItemSelectedListener(this);
        Menu menu = leftNav.getMenu();

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
