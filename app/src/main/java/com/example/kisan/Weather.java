package com.example.kisan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentSender;
import android.location.Location;

import android.os.Bundle;
import android.util.Log;
import android.view.textclassifier.TextLinks;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Weather extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
{
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double latitude,longitude;
    LinearLayout linlay;



   // api.openweathermap.org/data/2.5/forecast?lat=22.79&lon=86.23&appid=24284d14559ad98bccec5df75802230b



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        linlay=findViewById(R.id.linlay);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        getWeatherUpdates();


    }
    public void getWeatherUpdates()
    {
        final RequestQueue requestQueue= Volley.newRequestQueue(this);

        String url="https://api.openweathermap.org/data/2.5/forecast?lat="+latitude+"&lon="+longitude+"&appid=24284d14559ad98bccec5df75802230b";
       //String url="https://api.openweathermap.org/data/2.5/forecast?lat=23.17&lon=83.23&appid=24284d14559ad98bccec5df75802230b";
        final JsonObjectRequest req=new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("latlong",latitude+" "+longitude);
                try {

                    JSONArray  array=response.getJSONArray("list");

                    for(int i=0;i<array.length();i++) {
                        JSONObject object=array.getJSONObject(i);
                        JSONObject main=object.getJSONObject("main");
                        JSONArray weather=object.getJSONArray("weather");
                        String description=weather.getJSONObject(0).getString("description");
                        String date=object.getString("dt_txt");
                        String temp=String.valueOf( Math.round(Float.valueOf(main.getString("temp"))-272.15f));
                        String temp_min=String.valueOf(Math.round(Float.valueOf(main.getString("temp_min"))-272.15f));
                        String temp_max=String.valueOf(Math.round(Float.valueOf(main.getString("temp_max"))-272.15f));
                        String humidity=main.getString("humidity");
                        JSONObject wind=object.getJSONObject("wind");
                        String windspeed=wind.getString("speed");


                        TextView tdate=new TextView(Weather.this);
                        tdate.setTextSize(20);
                        tdate.setText("Date & Time "+date);

                        TextView desc=new TextView(getApplicationContext());
                        desc.setTextSize(20);
                        desc.setText( "description "+  description);

                        TextView ttemp_min=new TextView(getApplicationContext());
                        ttemp_min.setTextSize(20);
                        ttemp_min.setText("min temp "+temp_min+"C");

                        TextView ttemp_max=new TextView(getApplicationContext());
                        ttemp_max.setTextSize(20);
                        ttemp_max.setText("max temp "+temp_max+"C");

                        TextView twind=new TextView(getApplicationContext());
                        twind.setTextSize(20);
                        twind.setText("windspeed "+windspeed+"m/s");

                        TextView empty=new TextView(getApplicationContext());
                        empty.setText("\n");



                        linlay.addView(tdate);
                        linlay.addView(desc);
                        linlay.addView(ttemp_min);
                        linlay.addView(ttemp_max);
                        linlay.addView(twind);
                        linlay.addView(empty);










                    }

                }
                catch (Exception e) {
                    Log.d("error2","catch");
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",error.toString());
                Toast.makeText(Weather.this,"no Internet 2.0", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(req);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }


    }

    /**
     * If connected get lat and long
     *
     */
    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            latitude = location.getLatitude();
            longitude = location.getLongitude();


        }
    }


    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        }
        else {

        }
    }


    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

}

