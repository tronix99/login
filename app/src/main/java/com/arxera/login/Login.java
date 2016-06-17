package com.arxera.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raj on 13-05-2016.
 */
public class Login extends AppCompatActivity {
    private EditText user, pass;
    private Button bLogin;
    // Progress Dialog
    private ProgressDialog pDialog;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL = "http://arx-era.com/login.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_MOBILE = "mobile";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user = (EditText) findViewById(R.id.etemail);
        pass = (EditText) findViewById(R.id.etpassword);
        final Button bLogin = (Button) findViewById(R.id.btnLogin);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateName(user.getText().toString())) {
                    user.setError("Enter Name");
                    user.requestFocus();
                } else if (!validatePassword(pass.getText().toString())) {
                    pass.setError("Invalid Password");
                    pass.requestFocus();
                } else {
                    new AttemptLogin().execute();
                }
            }

        });
    }

    class AttemptLogin extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Attempting for login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // here Check for success tag
            int success;
            String username = user.getText().toString();
            String password = pass.getText().toString();
            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));

                Log.d("request!", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                // checking  log for json response
                Log.d("Login attempt", json.toString());

                // success tag for json
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Successfully Login!", json.toString());

                    String name = json.getString(TAG_NAME);
                    String email = json.getString(TAG_EMAIL);
                    String mobile = json.getString(TAG_MOBILE);

                    //Creating a shared preference
                    final SharedPreferences sharedPreferences = Login.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_MULTI_PROCESS);

                    //Creating editor to store values to shared preferences
                    final SharedPreferences.Editor editor = sharedPreferences.edit();

                    //Adding values to editor
                    editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                    editor.putString(Config.NAME_SHARED_PREF, name);
                    editor.putString(Config.MOBILE_SHARED_PREF, mobile);
                    editor.putString(Config.EMAIL_SHARED_PREF, email);

                    //Saving values to editor
                    editor.commit();
                    Intent ii = new Intent(Login.this,Main.class);
                    finish();
                    // this finish() method is used to tell andro id os that we are done with current //activity now! Moving to other activity
                    startActivity(ii);
                    return json.getString(TAG_MESSAGE);
                }else{

                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        /**
         * Once the background process is done we need to  Dismiss the progress dialog asap
         * **/
    protected void onPostExecute(String message) {
        pDialog.dismiss();
        if (message != null){
            Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();
        }
    }
}



    protected boolean validateName(String etUsername) {
        if (etUsername != null && etUsername.length() > 3) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //Return true if password is valid and false if password is invalid
    protected boolean validatePassword(String etPassword) {
        if (etPassword != null && etPassword.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void signup(View view) {
        startActivity(new Intent(this, Signup.class));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

}
