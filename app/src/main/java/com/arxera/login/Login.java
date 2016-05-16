package com.arxera.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import com.android.volley.RequestQueue;
import com.android.volley.Response;

import android.app.AlertDialog;

import org.json.JSONObject;


/**
 * Created by Raj on 13-05-2016.
 */
public class Login extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText name1 = (EditText) findViewById(R.id.email);
        final EditText password1 = (EditText) findViewById(R.id.password);
        final Button bLogin = (Button) findViewById(R.id.btnLogin);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name2 = name1.getText().toString();
                final String password = password1.getText().toString();

                // Response received from the server
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                String name = jsonResponse.getString("name");
                                String mobile = jsonResponse.getString("mobile");
                                String email = jsonResponse.getString("email");

                                Intent intent = new Intent(Login.this, Main.class);
                                intent.putExtra("name", name);
                                intent.putExtra("mobile", mobile);
                                intent.putExtra("email", email);
                                Login.this.startActivity(intent);
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

                LoginRequest loginRequest = new LoginRequest(name2, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Login.this);
                queue.add(loginRequest);
            }
        });

    }

    public void signup(View view) {
        startActivity(new Intent(this, Signup.class));
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
