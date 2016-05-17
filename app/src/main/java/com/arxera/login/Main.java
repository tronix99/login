package com.arxera.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String mobile = intent.getStringExtra("mobile");

        TextView tvname = (TextView) findViewById(R.id.username);
        TextView tvemail = (TextView) findViewById(R.id.email);
        TextView tvmobile = (TextView) findViewById(R.id.mobile);

        // Display user details
        tvname.setText(name);
        tvemail.setText(email);
        tvmobile.setText(mobile);
    }
}
