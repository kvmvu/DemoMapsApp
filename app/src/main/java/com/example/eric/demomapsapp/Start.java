package com.example.eric.demomapsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.cast.framework.SessionManager;

import static android.view.View.GONE;

public class Start extends AppCompatActivity {

    Session session;
    RelativeLayout rl1, rl2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        session = new Session();

        rl1 = (RelativeLayout) findViewById(R.id.ll3);
        rl2 = (RelativeLayout) findViewById(R.id.ll4);

        String status = session.getPreferences(Start.this, "status");
        Log.d("status", status);
        if(status.equals("1")){
            rl1.setVisibility(View.INVISIBLE);
            rl2.setVisibility(View.INVISIBLE);
        }
    }

    public void onBtnSignUp(View v){
        Intent intent = new Intent(v.getContext(), Login.class);
        startActivity(intent);
    }

    public void onBtnFindGig(View v){
        Intent intent = new Intent(Start.this, MapsActivity.class);
        intent.setAction(Intent.ACTION_SEARCH);
        startActivity(intent);
    }

    public void onBtnPostGig(View v){
        Intent intent = new Intent(Start.this, PostGig.class);
        startActivity(intent);
    }
}
