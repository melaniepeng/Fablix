package com.example.melaniepeng.project_4;

//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends ActionBarActivity {

    private EditText username, password;
    //private boolean success = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void connectToTomcat(View view) {
        username = findViewById(R.id.editText1);
        password = findViewById(R.id.editText2);
        // no user is logged in, so we must connect to the server

        // Use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        final StringRequest loginRequest = new StringRequest(Request.Method.POST, "https://3.18.108.153:8443/project4/api/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(username.getText().toString() + "\t" + password.getText().toString());
                        System.out.println(response.substring(11,15).equalsIgnoreCase("fail"));
                        if(response.substring(11,15).equalsIgnoreCase("fail"))
                        {
                            Log.d("login.not success", response);
                            ((TextView) findViewById(R.id.textView5)).setText(response.substring(28,45));
                        }
                        else
                        {
                            Log.d("login.success", response);
                            Intent intent = new Intent(MainActivity.this, SearchBoxActivity.class);
                            intent.putExtra("Login", "successful");
                            startActivity(intent);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("login.error", error.toString());
                        ((TextView) findViewById(R.id.textView5)).setText(error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                // Post request form data
                final Map<String, String> params = new HashMap<String, String>();
                params.put("username", username.getText().toString());
                params.put("password", password.getText().toString());

                return params;
            }
        };

        // !important: queue.add is where the login request is actually sent
        queue.add(loginRequest);
    }

}