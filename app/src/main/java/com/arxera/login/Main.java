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
    GPSTracker gps;
    private TextView tvname, tvmobile, tvemail;
    String name, mobile, email;
    private boolean ACTIVE = false;
    Button act;
    SharedPreferences get,active,getactive,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvname = (TextView) findViewById(R.id.username);
        tvemail = (TextView) findViewById(R.id.email);
        tvmobile = (TextView) findViewById(R.id.mobile);
        get = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_MULTI_PROCESS);
        name = get.getString(Config.NAME_SHARED_PREF, "Not Available");
        mobile = get.getString(Config.MOBILE_SHARED_PREF, "Not Available");
        email = get.getString(Config.EMAIL_SHARED_PREF, "Not Available");
        act = (Button) findViewById(R.id.userad);
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
                        logout = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = logout.edit();
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
                        editor.putBoolean(Config.SUCCESS, false);
                        editor.clear();
                        editor.commit();
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
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public void userad(View view) {
        gps = new GPSTracker(Main.this);

        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            String lat = String.valueOf(latitude);
            String longi = String.valueOf(longitude);
            final SharedPreferences active = Main.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_MULTI_PROCESS);
            final SharedPreferences.Editor editor = active.edit();
            editor.putBoolean(Config.ACTIVE, true);
            editor.putString(Config.LATITUDE, (Double.toString(latitude)));
            editor.putString(Config.LONGITUDE, (Double.toString(longitude)));
            editor.commit();
            Toast.makeText(getApplicationContext(), lat + longi, Toast.LENGTH_SHORT).show();
            ActiveBackgroundTask bt = new ActiveBackgroundTask();
            bt.execute(lat, longi, name);
        } else {
            gps.showSettingsAlert();
        }
    }

    class ActiveBackgroundTask extends AsyncTask<String, Void, String> {
        String latlong_info_url;

        @Override
        protected void onPreExecute() {
            latlong_info_url = "http://arx-era.com/activelatlong.php";
        }

        @Override
        protected String doInBackground(String... args) {
            String lat, longi;
            lat = args[0];
            longi = args[1];
            try {
                URL url = new URL(latlong_info_url);
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

    public void de(View view) {
        final SharedPreferences deactive = Main.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_MULTI_PROCESS);
        final SharedPreferences.Editor editor = deactive.edit();
        editor.putBoolean(Config.ACTIVE, false);
        editor.commit();
        DeactiveBackgroundTask bt = new DeactiveBackgroundTask();
        bt.execute(name);
    }

    //Deactive user
    class DeactiveBackgroundTask extends AsyncTask<String, Void, String> {
        String deactive_info_url;

        @Override
        protected void onPreExecute() {
            deactive_info_url = "http://arx-era.com/deactivelatlong.php";
        }

        @Override
        protected String doInBackground(String... args) {
            String name;
            name = args[0];
            try {
                URL url = new URL(deactive_info_url);
                HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                huc.setRequestMethod("POST");
                huc.setDoOutput(true);
                OutputStream os = huc.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data_string = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
                bw.write(data_string);
                bw.flush();
                bw.close();
                os.close();
                InputStream is = huc.getInputStream();
                is.close();
                huc.disconnect();
                return "Deactive";
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

    public void nearu(View view) {
        Intent near = new Intent(Main.this,ActiveUsers.class);
        startActivity(near);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
