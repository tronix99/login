package com.arxera.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Raj on 13-05-2016.
 */
public class Login  extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void signup(View view){
        startActivity(new Intent(this,Signup.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
