package com.example.courierapp;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Locations extends Service {
    public static final int DEFAULT_UPDATE_INTERVAL = 30000;
    public static final int FASTEST_INTERVAL = 5000;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private final IBinder myBinder = new MyLocalBinder();
    private URL url;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private String longtitude;
    private String latitude;
    private String locations;
    StringBuilder sb=null, stringBuilder = new StringBuilder();

    public Locations() {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(DEFAULT_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onTaskRemoved(intent);
        Toast.makeText(getApplicationContext(), "This is a Service running in Background", Toast.LENGTH_SHORT).show();
        //while(true) {
        getMyLocation();
        //SystemClock.sleep(30000);
        //}
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
       Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return myBinder;
    }


    public void getMyLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    setLatitude(String.valueOf(location.getLatitude()));
                    Log.d("MESSAGE",String.valueOf(location.getLatitude()));
                    setLongtitude(String.valueOf(location.getLongitude()));
                    Log.d("MESSAGE",String.valueOf(location.getLongitude()));
                    //Log.d("MESSAGE",SharedPreferancesManager.getInstance(getApplicationContext()).getUser().getUserId());
                    //Log.d("MESSAGE",SharedPreferancesManager.getInstance(getApplicationContext()).getUser().getUserRole());
                    setLocations("userid="+SharedPreferancesManager.getInstance(getApplicationContext()).getUser().getUserId()+"&long="+ getLongtitude() +"&lat="+ getLatitude());


                    UPDLocations updLocations = new UPDLocations();
                    updLocations.execute(getLocations());
                }
            });
        }
        else{
            Log.d("Message","Dont have permission");
        }


    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLocations() {
        return locations;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }

    class UPDLocations extends AsyncTask<String, String, String> {
        private HttpURLConnection con;
        @Override
        protected String doInBackground(String... strings) {

                try {
                    url = new URL(URLS.URL_UPDLOCATION);
                    Log.d("MESSAGE", url.toString());
                    this.con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(1500000);
                    con.setConnectTimeout(150000000);
                    con.setRequestMethod("POST");
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    OutputStream os = con.getOutputStream();
                    Log.d("MESSAGE","opnened output stream");
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getLocations());
                    Log.d("MESSAGE",getLocations());
                    //writer.write(getPostDataString(postDataParams));
                    writer.flush();
                    writer.close();
                    os.close();
                    Log.d("MESSAGE","closed output stream");
                    // 2. Open InputStream to connection


                    InputStream in = con.getInputStream();
                    Log.d("MESSAGE","opnened input stream");


// 3. Get response
                    int responseCode = con.getResponseCode();

                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        Log.d("MESSAGE", "OK");
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                        sb = new StringBuilder();
                        String response;

                        while ((response = br.readLine()) != null) {
                            sb.append(response);
                        }
                        Log.d("MESSAGE", sb.toString());

                        try {
                            //converting response to json object
                            //JSONArray ar = new JSONArray(s);
                            Log.d("MESSAGE", sb.toString());
                            //JSONObject obj = new JSONObject(s);
                            JSONObject obj = new JSONObject(sb.toString());

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Log.d("MESSAGE", obj.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    else{

                        Log.d("MESSAGE","response code failed");
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    doInBackground();
                }


            return null;
        }
    }

public class MyLocalBinder extends Binder {
        Locations getService() {
            return Locations.this;
        }
    }
}
