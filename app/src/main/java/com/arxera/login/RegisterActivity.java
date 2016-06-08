package com.arxera.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class RegisterActivity extends AppCompatActivity {
    Button B1, B2;
    TextView tv;
    private CallbackManager callbackManager;
    private LoginButton fbLoginButton;

    //boolean variable to check user is logged in or not
    //initially it is false
    private boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();


        //You need this method to be used only once to configure
        //your key hash in your App Console at
        // developers.facebook.com/apps

        getFbKeyHash("com.arxera.login");
        setContentView(R.layout.activity_register);

        B1 = (Button) findViewById(R.id.signup);
        B2 = (Button) findViewById(R.id.login);

        tv = (TextView) findViewById(R.id.text);
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if (ni != null && ni.isConnected()) {
            tv.setVisibility(View.INVISIBLE);
        } else {
            B1.setEnabled(false);
            B2.setEnabled(false);
        }

        fbLoginButton = (LoginButton) findViewById(R.id.fb_login_button);

        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                System.out.println("Facebook Login Successful!");
                System.out.println("Logged in user Details : ");
                System.out.println("--------------------------");
                System.out.println("User ID  : " + loginResult.getAccessToken().getUserId());
                System.out.println("Authentication Token : " + loginResult.getAccessToken().getToken());
                Toast.makeText(RegisterActivity.this, "Login Successful!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(RegisterActivity.this, "Login cancelled by user!", Toast.LENGTH_LONG).show();
                System.out.println("Facebook Login failed!!");

            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(RegisterActivity.this, "Login unsuccessful!", Toast.LENGTH_LONG).show();
                System.out.println("Facebook Login failed!!");
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);

        //If we will get true
        if(loggedIn){
            //We will start the Profile Activity
            Intent intent = new Intent(RegisterActivity.this, Main.class);
            startActivity(intent);
        }
    }
    public void signup(View view) {
        startActivity(new Intent(this, Signup.class));
    }

    public void login(View view) {
        startActivity(new Intent(this, Login.class));
    }


    public void getFbKeyHash(String packageName) {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("YourKeyHash :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                System.out.println("YourKeyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent i) {
        callbackManager.onActivityResult(reqCode, resCode, i);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
