package com.arxera.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Raj on 13-05-2016.
 */
public class Login extends AppCompatActivity {
    JSONObject jsonResponse;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText etUsername = (EditText) findViewById(R.id.etemail);
        final EditText etPassword = (EditText) findViewById(R.id.etpassword);
        final Button bLogin = (Button) findViewById(R.id.btnLogin);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (!validateName(etUsername.getText().toString())) {
                    etUsername.setError("Enter Name");
                    etUsername.requestFocus();
                } else if (!validatePassword(etPassword.getText().toString())) {
                    etPassword.setError("Invalid Password");
                    etPassword.requestFocus();
                } else {
                    // Response received from the server
                    final Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {

                                    String name = jsonResponse.getString("name");
                                    String email = jsonResponse.getString("email");
                                    String mobile = jsonResponse.getString("mobile");


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

                                    //Starting profile activity
                                    Intent intent = new Intent(Login.this, Main.class);
                                    startActivity(intent);
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                    builder.setMessage("Login Failed")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    LoginRequest loginRequest = new LoginRequest(name, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Login.this);
                    queue.add(loginRequest);
                }
            }
        });
    }


    protected boolean validateName(String etUsername) {
        if (etUsername != null && etUsername.length() > 3) {
            return true;
        } else {
            return false;
        }
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
        finish();
    }

}
