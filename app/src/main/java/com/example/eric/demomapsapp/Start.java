package com.example.eric.demomapsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

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
