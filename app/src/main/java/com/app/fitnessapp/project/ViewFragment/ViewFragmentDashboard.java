package com.app.fitnessapp.project.ViewFragment;


import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.fitnessapp.project.R;
import com.app.fitnessapp.project.Repository.UserDataTable;
import com.app.fitnessapp.project.Utils;
import com.app.fitnessapp.project.ViewModel.ViewModelDashboard;

import org.json.JSONException;
import org.json.JSONObject;
//import org.w3c.dom.Text;

import java.text.DecimalFormat;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;


public class ViewFragmentDashboard extends Fragment {

    Button mChangeGoalButton;
    ImageView mWeatherIcon;
    TextView mWeatherText, mDashboardCalorieText, mGoalText;
    public RequestQueue queue;
//    String weatherUrl, location;
    private ViewModelDashboard mViewModelDashboard;
    // Required empty public constructor
    public ViewFragmentDashboard() {}
    private LocationManager locationManager;
    private View v;
    private Location currentLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home_dashboard, container, false);
        Utils.setDirPath(getActivity().getApplicationContext().getFilesDir().getPath());

        // get all the variables we need to change and stuff
        mWeatherIcon = (ImageView) v.findViewById(R.id.weatherImage);
        mWeatherText = (TextView) v.findViewById(R.id.weather);
        mGoalText = (TextView) v.findViewById(R.id.dashboardGoalText);
        mDashboardCalorieText = (TextView) v.findViewById(R.id.dashboardCalorieText);
        mChangeGoalButton = (Button) v.findViewById(R.id.changeGoalButton);
        // this is a comment
        mViewModelDashboard = ViewModelProviders.of(this).get(ViewModelDashboard.class);
        mViewModelDashboard.getUserTable().observe(this, nameObserver);

        /////// Handling the weather with the api call still //////////
        // get current location
        // https://androstock.com/tutorials/getting-current-location-latitude-longitude-country-android-android-studio.html
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        queue = Volley.newRequestQueue(v.getContext());


        // get location and set weather data
        statusCheck();
        getLocation();
        getCurrentWeather();
        ///////////////////////////////////////////////////////////////

        return v;
    }

    /**
     * Methods to check that the GPS is enabled
     */
    //// https://stackoverflow.com/questions/25175522/how-to-enable-location-access-programmatically-in-android
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    final Observer<UserDataTable> nameObserver  = new Observer<UserDataTable>() {
        @Override
        public void onChanged(@Nullable final UserDataTable userData) {
            // Update the UI if this data variable changes
            if(userData!=null) {

                String tmp = userData.getDailyCaloricIntake() + " calories daily";
                mDashboardCalorieText.setText(tmp);

                double goal = userData.getGoal();
                if(goal == 0.0) {
                    mGoalText.setText("Maintain current weight (" + userData.getWeight() + ") LBs");
                }else if(goal < 0.0) {
                    mGoalText.setText("Lose " + goal * -1 + " LBs per week");
                }else {
                    mGoalText.setText("Gain " + goal + " LBs per week");
                }

//                location = userData.getLocation();
            }
        }
    };

    /**
     * Method to get the locationManager
     */
    void getLocation() {
        try {
//            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager = (LocationManager) v.getContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(NETWORK_PROVIDER, 5000, 5, locationListener);
            currentLocation = locationManager.getLastKnownLocation(NETWORK_PROVIDER);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            currentLocation = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

        // function to get json object with weather conditions and populate the UI
    private void getCurrentWeather(){
        String url = "";
        if(currentLocation != null) {
            url = "http://api.openweathermap.org/data/2.5/weather?lat=" +
                    currentLocation.getLatitude() + "&lon=" + currentLocation.getLongitude()+"&APPID=c79b273bee701e4051a0a0c160435f7a";
        } else {
            url = "http://api.openweathermap.org/data/2.5/weather?q=salt+lake+city&APPID=c79b273bee701e4051a0a0c160435f7a";
        }
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            DecimalFormat oneDigit = new DecimalFormat("#,##0.0");

                            String description = (String) response
                                    .getJSONArray("weather")
                                    .getJSONObject(0)
                                    .get("main").toString();

                            // temp converted to F
                            double temperature = Float.parseFloat(response
                                    .getJSONObject("main")
                                    .getString("temp")) * 1.8 - 458.67;

                            double humidity = Float.parseFloat(response
                                    .getJSONObject("main")
                                    .getString("humidity"));

                            // multiply m/s to this to get mph
                            double WINDSPEEDCONVERTER = 2.2369;

                            double windspeed = Float.parseFloat(response
                                    .getJSONObject("wind")
                                    .getString("speed")) * WINDSPEEDCONVERTER;


                            String currentWeather = description + "\n"
                                    + oneDigit.format(temperature) + " F\n"
                                    + oneDigit.format(humidity) + "% humidity\n"
                                    + oneDigit.format(windspeed) + "mph wind";
                            mWeatherText.setText(currentWeather);
                            description = description.toLowerCase();
                            if(description.contains("rain") ||
                                    description.contains("rain") ||
                                    description.contains("thunder") ||
                                    description.contains("drizzle")){
                                mWeatherIcon.setImageResource(R.drawable.rain);
                            } else if(description.contains("snow")){
                                mWeatherIcon.setImageResource(R.drawable.snow);
                            } else if (description.contains("cloud")){
                                mWeatherIcon.setImageResource(R.drawable.clouds);
                            } else {
                                mWeatherIcon.setImageResource(R.drawable.sun);
                            }
                        } catch (JSONException e) {
                            mWeatherText.setText("JSON parse error: " + e);
                        }
//                                Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mWeatherText.setText(error.toString());
                    }
                }
        );
        this.queue.add(getRequest);
    }

}
