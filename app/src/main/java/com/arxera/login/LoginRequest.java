package com.arxera.login;

/**
 * Created by User on 16-05-2016.
 */
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "http://arx-era.com/Login.php";
    private Map<String, String> params;

    public LoginRequest(String name2, String password, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("name", name2);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}