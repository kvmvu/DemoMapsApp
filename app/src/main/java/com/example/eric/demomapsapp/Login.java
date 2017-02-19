package com.example.eric.demomapsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private TextView login;
    private EditText etUserName, etPass, etCpass, etEmail;
    private DbHelper dbHelper;
    Session session;
    public final String TAG = this.getClass().getName();
    String username, email, password;

    String URL = "http://savtech.co.ke/demomapsapp/index.php";


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
    }

    //on sign up
    @Override
    public void onClick(View view) {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("textUsername", etUserName.getText().toString());
        data.put("textEmail", etEmail.getText().toString());
        data.put("textPassword", etPass.getText().toString());
        data.put("textPasswordConfirm", etCpass.getText().toString());

        if(!etUserName.getText().toString().matches("") && !etEmail.getText().toString().matches("")
                && !etPass.getText().toString().matches("") && !etCpass.getText().toString().matches("")){
            if(etPass.getText().toString().matches(etCpass.getText().toString())){

                //userReg(view);
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Creating Account...");
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        "http://savtech.co.ke/demomapsapp/addUsers.php", new Response.Listener<String>() {
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

                    }
                }){
                    protected Map<String, String> getParams(){
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("textUsername", etUserName.getText().toString());
                        params.put("textEmail", etEmail.getText().toString());
                        params.put("textPassword", etPass.getText().toString());
                        //params.put("textPasswordConfirm", etCpass.getText().toString());
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);

                //store user in sqlitedb
                dbHelper.addUser(String.valueOf(etUserName), String.valueOf(etEmail),
                        String.valueOf(etPass));

                //create session
                session.setPreferences(Login.this, "status", "1");
                String status = session.getPreferences(Login.this,"status");
                Log.d("status", status);
                Intent intent = new Intent(Login.this, Start.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Confirm correct password", Toast.LENGTH_SHORT).show();
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

//    public void userReg(View view){
//        username = etUserName.getText().toString().trim();
//        email = etEmail.getText().toString().trim();
//        password = etPass.getText().toString().trim();
//
//        String method = "register";
//        BackgroundTask backgroundTask = new BackgroundTask(this);
//        backgroundTask.execute(method, username, email, password);
//    }
}
