package com.example.eric.demomapsapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eric.demomapsapp.app.AppConfig;
import com.example.eric.demomapsapp.app.AppController;
import com.kosalgeek.android.md5simply.MD5;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private TextView login;
    private EditText etUserName, etPass, etCpass, etEmail;
    private DbHelper dbHelper;
    Session session;
    public final String TAG = this.getClass().getName();
    ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle("Sign Up");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        etUserName = (EditText) findViewById(R.id.txtUsername);
        etEmail = (EditText) findViewById(R.id.txtEmail);
        etPass = (EditText) findViewById(R.id.txtPassword);
        etCpass = (EditText) findViewById(R.id.txtPasswordConfirm);
        login = (TextView)findViewById(R.id.btnLogin);

        login.setOnClickListener(this);

        dbHelper = new DbHelper(this);
        session = new Session();

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
    }

    //on sign up
    @Override
    public void onClick(View view) {
        String username = etUserName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPass.getText().toString().trim();

        if(!etUserName.getText().toString().matches("") && !etEmail.getText().toString().matches("")
                && !etPass.getText().toString().matches("") && !etCpass.getText().toString().matches("")){
            if(etPass.getText().toString().matches(etCpass.getText().toString())){
                registerUser(username, email, password);
            } else {
                Toast.makeText(getApplicationContext(), "Confirm correct password", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please ensure all fields are filled out before signing up",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void registerUser(final String username, final String password, final String email){
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        dbHelper.addUser(name, email, uid, created_at);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Welcome " + name, Toast.LENGTH_LONG).show();

                        // Create session and Launch start activity
                        session.setPreferences(Login.this, "status", "1");
                        String status = session.getPreferences(Login.this,"status");
                        Log.d("status", status);
                        Intent intent = new Intent(Login.this, Start.class);
                        startActivity(intent);
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", username);
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
