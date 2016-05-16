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
        String mobile = intent.getStringExtra("mobile");
        String email = intent.getStringExtra("email");

        TextView nm = (TextView) findViewById(R.id.username);
        TextView mob = (TextView) findViewById(R.id.mobile);
        TextView em = (TextView) findViewById(R.id.email);

        nm.setText(name);
        mob.setText(mobile);
        em.setText(email);

    }
}
