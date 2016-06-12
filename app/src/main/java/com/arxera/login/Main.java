package com.arxera.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Main extends AppCompatActivity {
    private Button log_out;
    // GPSTracker class
    GPSTracker gps;
    //Textview to show currently logged in user
    private TextView tvname,tvmobile,tvemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tvname = (TextView) findViewById(R.id.username);
        tvemail = (TextView) findViewById(R.id.email);
        tvmobile = (TextView) findViewById(R.id.mobile);

        //Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_MULTI_PROCESS);
        String name = sharedPreferences.getString(Config.NAME_SHARED_PREF,"Not Available");
        String mobile  = sharedPreferences.getString(Config.MOBILE_SHARED_PREF,"Not Available");
        String email  = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");


        // Display user details
        tvname.setText(name);
        tvemail.setText(email);
        tvmobile.setText(mobile);

    }

    public void logout(View view) {
        //Logout function
            //Creating an alert dialog to confirm logout
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to logout?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            //Getting out sharedpreferences
                            SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                            //Getting editor
                            SharedPreferences.Editor editor = preferences.edit();

                            //Puting the value false for loggedin
                            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
                            editor.putBoolean(Config.SUCCESS, false);

                            // Clearing all data from Shared Preferences
                            editor.clear();
                            editor.commit();

                            //Starting login activity
                            Intent intent = new Intent(Main.this, Login.class);
                            startActivity(intent);
                        }
                    });

            alertDialogBuilder.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });

            //Showing the alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

    }

    public void mapa(View view) {
        // create class object
        gps = new GPSTracker(Main.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            String lat =  String.valueOf(latitude);
            String longi =  String.valueOf(longitude);

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            //Creating a shared preference
            final SharedPreferences sharedPreferences = Main.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_MULTI_PROCESS);

            //Creating editor to store values to shared preferences
            final SharedPreferences.Editor editor = sharedPreferences.edit();

            //getting username
            String name = sharedPreferences.getString(Config.NAME_SHARED_PREF,"Not Available");

            //Adding values to editor
            editor.putString(Config.LATITUDE, lat);
            editor.putString(Config.LONGITUDE, longi);

            //Saving values to editor
            editor.commit();

            BackgroundTask bt = new BackgroundTask();
            bt.execute(lat,longi,name);
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    //adding lat n long values to server
    class BackgroundTask extends AsyncTask<String, Void, String> {
        String add_info_url;

        @Override
        protected void onPreExecute() {
            add_info_url = "http://arx-era.com/latlong.php";
        }

        @Override
        protected String doInBackground(String... args) {
            String name, lat, longi;
            lat = args[0];
            longi = args[1];
            name = args[2];

            try {
                URL url = new URL(add_info_url);
                HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                huc.setRequestMethod("POST");
                huc.setDoOutput(true);
                OutputStream os = huc.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data_string = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                        URLEncoder.encode("lat", "UTF-8") + "=" + URLEncoder.encode(lat, "UTF-8") + "&" +
                        URLEncoder.encode("longi", "UTF-8") + "=" + URLEncoder.encode(longi, "UTF-8");
                bw.write(data_string);
                bw.flush();
                bw.close();
                os.close();
                InputStream is = huc.getInputStream();
                is.close();
                huc.disconnect();
                return "Active";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
