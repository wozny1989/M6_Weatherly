package com.pksroczynski.weatherly;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Objects;

public class WeatherlyController extends AppCompatActivity implements LocationListener {

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 10 * 1;
    Location location; // location
    private double lon;
    private double lat;
    Double temp;
    Integer humidity;
    String description;
    String name;
    String icon;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weatherly_controller);
        getWeatherForCurrentLocation();

        LocationManager locationManager = (LocationManager)
                getSystemService(LOCATION_SERVICE);

        Objects.requireNonNull(locationManager).requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        if (locationManager != null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                lat = location.getLatitude();
                lon = location.getLongitude();
            }
        }
    }

    private void getWeatherForCurrentLocation() {
        String url = "http://api.openweathermap.org/data/2.5/weather?lat=30.266666&lon=-97.733330&appid=41b4d7daa90de9add192e36d9ef71463";
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

// Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonWeatherArray = response.getJSONArray("weather");
                            JSONObject jsonTempObject = response.getJSONObject("main");
                            temp = jsonTempObject.getDouble("temp");
                            name = response.getString("name");
                            for (int i = 0; i< jsonWeatherArray.length(); i++) {
                                JSONObject jsonObject = jsonWeatherArray.getJSONObject(i);
                                description = jsonObject.getString("description");
                                icon = jsonObject.getString("icon");
                            }

                            TextView cityLabel = (TextView) findViewById(R.id.locationTextView);
                            cityLabel.setText(name);
                            TextView temperatureLabel = (TextView) findViewById(R.id.tempTextView);
                            temperatureLabel.setText(String.valueOf(temp));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

        queue.add(jsonObjectRequest);


    }

    @Override
    public void onLocationChanged(Location location) {
        getWeatherForCurrentLocation();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    // TODO: Add requestWeatherData(RequestParams params)


    // TODO: Add updateGUI()


}
