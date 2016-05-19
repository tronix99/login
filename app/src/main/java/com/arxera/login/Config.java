package com.arxera.login;

/**
 * Created by User on 19-05-2016.
 */
public class Config {

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "login";

    //This would be used to store the name of current logged in user
    public static final String NAME_SHARED_PREF = "name";
    //This would be used to store the mobile of current logged in user
    public static final String MOBILE_SHARED_PREF = "mobile";
    //This would be used to store the email of current logged in user
    public static final String EMAIL_SHARED_PREF = "email";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "logged";

    public static final String SUCCESS = "success";
}