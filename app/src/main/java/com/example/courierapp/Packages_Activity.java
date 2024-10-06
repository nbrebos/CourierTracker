package com.example.courierapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.courierapp.databinding.ActivityPackageslistBinding;

import org.json.JSONArray;
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
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class Packages_Activity extends AppCompatActivity {

    ActivityPackageslistBinding binding;
    ArrayList<PackageInfo> packageInfoArrayList;
    ArrayList<String> description = new ArrayList<String>();
    ArrayList<String> cost= new ArrayList<String>();
    ArrayList<String> address= new ArrayList<String>();
    ArrayList<String> status= new ArrayList<String>();
    ArrayList<String> weight= new ArrayList<String>();
    ArrayList<String> fragile= new ArrayList<String>();
    ArrayList<Integer> imageId = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPackageslistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        GETPackages updPackages  = new GETPackages();
        updPackages.execute("userId="+SharedPreferancesManager.getInstance(getApplicationContext()).getUser().getUserId()+"&role=courier");
        //Πίνακες με τα δεδομένα που θα χρειαστούν για να γεμίσουν τα παιδία καθώς και την εικόνα
        //int[] imageId = {R.drawable.package_image,R.drawable.package_image,R.drawable.package_image, R.drawable.package_image,R.drawable.package_image, R.drawable.package_image,R.drawable.package_image,R.drawable.package_image,R.drawable.package_image};
        //String[] description = {"Προσοχή κατά τη μεταφορά","Προσοχή κατά τη μεταφορά","Προσοχή κατά τη μεταφορά","Προσοχή κατά τη μεταφορά","Προσοχή κατά τη μεταφορά","Προσοχή κατά τη μεταφορά","Προσοχή κατά τη μεταφορά","Προσοχή κατά τη μεταφορά","Προσοχή κατά τη μεταφορά"};
        //String[] cost = {"30€","30€","30€","30€","30€","30€","30€","30€","30€"};
        //String[] address = {"Σωρού 73","Τραλλέων 33","Τραλλέων 34","Αλεξάνδου 24","Ευσταθίου 8","Χαλάνδρη 73","Αθηνάς 12","Χαλάνδρη 42","Σωρού 11"};
        //String[] status = {"Εκκρεμεί","Εκκρεμεί","Παραδώθηκε","Παραδώθηκε","Παραδώθηκε","Παραδώθηκε","Εκκρεμεί","Παραδώθηκε","Παραδώθηκε"};
        //String[] weight = {"20kg","20kg","20kg","20kg","20kg","20kg","20kg","20kg","20kg"};
        //String[] fragile = {"Ναι","Ναι","Ναι","Ναι","Ναι","Ναι","Ναι","Ναι","Ναι"};

        //ArrayList<PackageInfo> packageInfoArrayList = new ArrayList<>();

        //for(int i = 0;i< imageId.length;i++){
           // PackageInfo packageInfo = new PackageInfo(description[i],status[i],address[i],imageId[i]);
          //  packageInfoArrayList.add(packageInfo);
       // }
        //Ο ListAdapter θα ενσωματώσει τα δεδομένα που ορίστηκαν παραπάνω στα παιδία



    }
    class GETPackages extends AsyncTask<String, String, String> {
        TextView tv;

        protected String doInBackground(String... textViews) {
            StringBuilder sb = null, stringBuilder = new StringBuilder();
            String credentials = textViews[0];
            URL url;
            try {
                url = new URL(URLS.URL_GETMYPACKAGES);
                Log.d("MESSAGE", url.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //conn.connect();
                Log.d("MESSAGE", "connected");
                conn.setReadTimeout(1500000);
                conn.setConnectTimeout(1500000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                Log.d("MESSAGE", "started sending credentials");
//Send credentials
                OutputStream os = conn.getOutputStream();
                Log.d("MESSAGE", "opnened output stream");

                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(credentials);
                Log.d("MESSAGE", credentials);
                //writer.write(getPostDataString(postDataParams));
                writer.flush();
                writer.close();
                os.close();
                // 2. Open InputStream to connection

                Log.d("MESSAGE", "opnened input stream");
                InputStream in = conn.getInputStream();


// 3. Get response
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Log.d("MESSAGE", "OK");
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    sb = new StringBuilder();
                    String response;

                    while ((response = br.readLine()) != null) {
                        sb.append(response);
                    }
                    Log.d("MESSAGE", sb.toString());
                    Log.d("MESSAGE", "disconnected");
                    conn.disconnect();
                } else {

                    Log.d("MESSAGE", "connetction failed");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return (sb.toString());
            // tv = textViews[0];
            //  nestedCall();
            //  return null;
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
                Log.d("MESSAGE", s);
                JSONObject obj = new JSONObject(s);
                //JSONArray obj = new JSONArray(s);

                JSONObject pkg = obj.getJSONObject("packages");
                JSONArray description_ = pkg.getJSONArray("P_Description");
                JSONArray status_ = pkg.getJSONArray("P_Status");
                JSONArray address_ = pkg.getJSONArray("P_Address");
                JSONArray cost_ = pkg.getJSONArray("P_Cost");
                JSONArray weight_ = pkg.getJSONArray("P_Weight");
                JSONArray fragile_ =pkg.getJSONArray("P_Fragile");

                //int[] imageId = {R.drawable.package_image,R.drawable.package_image,R.drawable.package_image, R.drawable.package_image,R.drawable.package_image, R.drawable.package_image,R.drawable.package_image,R.drawable.package_image,R.drawable.package_image};
                packageInfoArrayList = new ArrayList<>();

                for(int i = 0;i< description_.length();i++){
                   // PackageInfo packageInfo = new PackageInfo(description.getString(i),status.getString(i),address.getString(i),imageId[i]);
                    //packageInfoArrayList.add(packageInfo);
                    description.add(description_.getString(i));
                    //Λόγω τελευταίας αλλαγής η μεταβλητή cost προσδιορίζει το τηλέφωνο
                     cost.add(cost_.getString(i));
                   address.add(address_.getString(i));
                    status.add(status_.getString(i));
                    weight.add(weight_.getString(i));
                    fragile.add(fragile_.getString(i));
                    imageId.add(R.drawable.package_image);
                    PackageInfo packageInfo = new PackageInfo(description.get(i),status.get(i),address.get(i),imageId.get(i));
                    packageInfoArrayList.add(packageInfo);
                }
                //ArrayList<PackageInfo> packageInfoArrayList = new ArrayList<>();

                //for(int i = 0;i< imageId.length;i++){
                //
                //  packageInfoArrayList.add(packageInfo);
                // }
                ListAdapter listAdapter = new ListAdapter(Packages_Activity.this, packageInfoArrayList);
                binding.listview.setAdapter(listAdapter);
                binding.listview.setClickable(true);
                binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent i = new Intent(Packages_Activity.this, PackageActivity.class);
                        i.putExtra("description",description.get(position));
                        i.putExtra("cost",cost.get(position));
                        i.putExtra("address",address.get(position));
                        i.putExtra("status",status.get(position));
                        i.putExtra("weight",weight.get(position));
                        i.putExtra("fragile",fragile.get(position));
                        startActivity(i);
                    }
                });


                //if no error in response
                //if (!obj.getBoolean("error")) {
                    //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                    //getting the packages from the response
                    //JSONObject packages = obj.getJSONObject(Integer.parseInt("packages"));
                    //JSONObject description = packages.getJSONObject("P_Description");
                    //JSONObject fragile = packages.getJSONObject("P_Fragile");
                    //JSONObject weight = packages.getJSONObject("P_Weight");
                    //JSONObject cost = packages.getJSONObject("P_Cost");




                    //creating a new user object


                    //storing the user in shared preferences


                    //starting the profile activity

                    String userlogged = SharedPreferancesManager.getInstance(getApplicationContext()).getUser().getUserId();

                //} else {
                    Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
              //  }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // tv.setText(s);
        }

    }


}