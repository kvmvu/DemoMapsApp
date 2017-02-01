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
import com.kosalgeek.android.md5simply.MD5;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private TextView login;
    private EditText etUserName, etPass, etCpass, etEmail;
    private DbHelper dbHelper;
    private Session session;
    public final String TAG = this.getClass().getName();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


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
        session = new Session(this);

        //to store login info in shared preferences
        sharedPreferences = getSharedPreferences("Sign Up", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        /*
        if(session.loggedIn()){
            startActivity(new Intent(Login.this, Start.class));
            finish();
        }*/
    }

    //on sign up
    @Override
    public void onClick(View view) {

        if(!etUserName.getText().toString().matches("") && !etEmail.getText().toString().matches("") && !etPass.getText().toString().matches("") && !etCpass.getText().toString().matches("")){
            if (etPass.getText().toString().matches(etCpass.getText().toString())) {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Creating Account...");
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://savtech.co.ke/demomapsapp/register.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hide();
                        Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Start.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("textUsername", etUserName.getText().toString());
                        params.put("textEmail", etEmail.getText().toString());
                        params.put("textPassword", etPass.getText().toString());
                        params.put("textPasswordConfirm", etCpass.getText().toString());
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            }else{
                Toast.makeText(this, "Passwords don't match", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please ensure all fields are filled out before signing up",
                    Toast.LENGTH_SHORT).show();
        }
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
