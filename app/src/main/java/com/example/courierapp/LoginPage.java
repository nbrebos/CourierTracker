package com.example.courierapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.courierapp.databinding.ActivityMainBinding;

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

public class LoginPage extends AppCompatActivity {

    private String userPass;
    private String userName;
    private ActivityMainBinding loginBinding;

    public void setUserPass(String userpass)
    {
        this.userPass = userpass;
    }

    public String getUserPass()
    {
        return this.userPass;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return this.userName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = loginBinding.getRoot();
        setContentView(view);
        //if (SharedPreferancesManager.getInstance(this).isLoggedIn()) {
          //  finish();
          //  startActivity(new Intent(this, Menu.class));
          //  return;
       // }
    }

    public void logIn(View view)
    {
        //if (SharedPreferancesManager.getInstance(this).isLoggedIn()) {
            //finish();
            //startActivity(new Intent(this, Menu.class));
           // return;
        //}
        Log.d("MESSAGE","started");
        Log.d("MESSAGE",loginBinding.userPassword.getText().toString());
        setUserName(loginBinding.userCode.getText().toString());
        setUserPass(loginBinding.userPassword.getText().toString());
        LoginBackground loginback = new LoginBackground();
        loginback.execute("user="+getUserName()+"&pass="+getUserPass());




    }
    class LoginBackground extends AsyncTask<String, String, String> {
        TextView tv;

        protected String doInBackground(String... textViews) {
            StringBuilder sb=null, stringBuilder = new StringBuilder();
            String credentials = textViews[0];
            URL url;
            try {
                url = new URL(URLS.URL_LOGIN);
                Log.d("MESSAGE", url.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //conn.connect();
                Log.d("MESSAGE", "connected");
                conn.setReadTimeout(1500000);
                conn.setConnectTimeout(1500000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                Log.d("MESSAGE","started sending credentials");
//Send credentials
                OutputStream os = conn.getOutputStream();
                Log.d("MESSAGE","opnened output stream");

                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(credentials);
                Log.d("MESSAGE",credentials);
                //writer.write(getPostDataString(postDataParams));
                writer.flush();
                writer.close();
                os.close();
                // 2. Open InputStream to connection

                Log.d("MESSAGE","opnened input stream");
                InputStream in = conn.getInputStream();



// 3. Get response
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Log.d("MESSAGE","OK");
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    sb = new StringBuilder();
                    String response;

                    while ((response = br.readLine()) != null) {
                        sb.append(response );
                    }
                    Log.d("MESSAGE",sb.toString());
                    Log.d("MESSAGE","disconnected");
                    conn.disconnect();
                }
                else{

                    Log.d("MESSAGE","connetction failed");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return (sb.toString());
            // tv = textViews[0];
            //  nestedCall();
            //  return null;
        }


        protected void onProgressUpdate(String... values) {
            //tv.setText(values[0]);

        }

        protected void onPostExecute(String s) {
            // This method is executed in the UIThread
            // with access to the result of the long running task
            // DO SOMETHING WITH STRING RESPONSE

            super.onPostExecute(s);
            //hiding the progressbar after completion
            //progressBar.setVisibility(View.GONE);

            try {
                //converting response to json object
                //JSONArray ar = new JSONArray(s);
                Log.d("MESSAGE",s);
                //JSONObject obj = new JSONObject(s);
                JSONObject obj = new JSONObject(s);

                //if no error in response
                if (!obj.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                    //getting the user from the response
                    JSONObject userJson = obj.getJSONObject("user");

                    //creating a new user object
                    UserInformation user = new UserInformation(
                            userJson.getString("U_Id"),
                            userJson.getString("U_Role")
                    );

                    //storing the user in shared preferences
                    SharedPreferancesManager.getInstance(getApplicationContext()).userLogin(user);

                    //starting the profile activity
                    finish();
                    startActivity(new Intent(getApplicationContext(), Menu.class));
                    UserInformation userlogged = SharedPreferancesManager.getInstance(getApplicationContext()).getUser();
                    Log.d("MESSAGE",userlogged.getUserRole());

                    //σχολιάστε τις επόμενες ττρεις γραμμες για να μην επιβαρύνει τους πόρους η τοποθεσία ώστε να τρέξει ομαλά για της υπόλοιπες λειτουργίες η εφαρμογή
                    if(userlogged.getUserRole().equals("courier")) {
                       startService(new Intent(getApplicationContext(), Locations.class));
                    }
                    else {
                        Log.d("MESSAGE", "Service not started user distr center");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // tv.setText(s);
        }
    }


}