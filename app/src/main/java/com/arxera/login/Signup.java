package com.arxera.login;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup extends Activity {
    EditText Name, Email, Mobile, Password;
    String name, email, mobile, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Name = (EditText) findViewById(R.id.input_name);
        Email = (EditText) findViewById(R.id.input_email);
        Mobile = (EditText) findViewById(R.id.input_mobile);
        Password = (EditText) findViewById(R.id.input_password);
    }

    public void saveinfo(View view) {


        if (!validateName(Name.getText().toString())) {
            Name.setError("Enter Name");
            Name.requestFocus();
        } else if (!validateEmail(Email.getText().toString())) {
            Email.setError("Invalid Email");
            Email.requestFocus();
        } else if (!validatePassword(Password.getText().toString())) {
            Password.setError("Invalid Password");
            Password.requestFocus();
        } else if (!validateMobile(Mobile.getText().toString())) {
            Mobile.setError("Invalid Mobile Number");
            Mobile.requestFocus();
        } else {
            name = Name.getText().toString();
            email = Email.getText().toString();
            mobile = Mobile.getText().toString();
            password = Password.getText().toString();

            BackgroundTask bt = new BackgroundTask();
            bt.execute(name, email, mobile, password);
            finish();
        }

    }

    protected boolean validateName(String name) {
        if (Name != null && Name.length() > 3) {
            return true;
        } else {
            return false;
        }
    }

    //Return true if password is valid and false if password is invalid
    protected boolean validatePassword(String password) {
        if (password != null && password.length() > 9) {
            return true;
        } else {
            return false;
        }
    }

    //Return true if Mobile Number is valid and false if password is invalid
    protected boolean validateMobile(String mobile) {
        if (mobile != null && mobile.length() == 10) {
            return true;
        } else {
            return false;
        }
    }

    //Return true if email is valid and false if email is invalid
    protected boolean validateEmail(String email) {
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();

    }


    class BackgroundTask extends AsyncTask<String, Void, String> {
        String add_info_url;

        @Override
        protected void onPreExecute() {
            add_info_url = "http://arx-era.com/add_info.php";
        }

        @Override
        protected String doInBackground(String... args) {
            String name, email, mobile, pass;
            name = args[0];
            email = args[1];
            mobile = args[2];
            pass = args[3];

            try {
                URL url = new URL(add_info_url);
                HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                huc.setRequestMethod("POST");
                huc.setDoOutput(true);
                OutputStream os = huc.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data_string = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("mobile", "UTF-8") + "=" + URLEncoder.encode(mobile, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bw.write(data_string);
                bw.flush();
                bw.close();
                os.close();
                InputStream is = huc.getInputStream();
                is.close();
                huc.disconnect();
                return "SignUp Successfull";

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

    public void login(View view){
        startActivity(new Intent(this,Login.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
