package com.arxera.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.arxera.login.map.MapsActivity;

public class Main extends AppCompatActivity {
    private Button log_out;


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
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String name = sharedPreferences.getString(Config.NAME_SHARED_PREF,"Not Available");
        String mobile = sharedPreferences.getString(Config.MOBILE_SHARED_PREF,"Not Available");
        String email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");


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

    public void map(View view) {
        startActivity(new Intent(this, MapsActivity.class));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
