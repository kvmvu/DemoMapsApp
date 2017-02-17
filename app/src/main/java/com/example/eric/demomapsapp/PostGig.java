package com.example.eric.demomapsapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PostGig extends AppCompatActivity implements View.OnClickListener{

    EditText etFromDate;
    EditText etToDate;
    EditText etTimeFrom;
    EditText etTimeTo;
    EditText etEventName;
    EditText etEventDescription;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;
    private TimePickerDialog fromTimePickerDialog;
    private TimePickerDialog toTimePickerDialog;

    String pName, eventLat, eventLng, eventName, eventDescription, fromDate, toDate, fromTime,
            toTime;
    LatLng platlng;
    DbHelper db;

    int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_gig);

        db = new DbHelper(this);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        timeFormatter = new SimpleDateFormat("hh:mm", Locale.ENGLISH);

        findViewById();
        setDateTimeField();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void findViewById(){
        etFromDate = (EditText) findViewById(R.id.etFromDate);
        etFromDate.setInputType(InputType.TYPE_NULL);
        etFromDate.requestFocus();

        etToDate = (EditText) findViewById(R.id.etToDate);
        etToDate.setInputType(InputType.TYPE_NULL);

        etTimeFrom = (EditText) findViewById(R.id.etTimeFrom);
        etTimeFrom.setInputType(InputType.TYPE_NULL);
        etTimeTo = (EditText) findViewById(R.id.etTimeTo);
        etTimeTo.setInputType(InputType.TYPE_NULL);

        etEventName = (EditText)findViewById(R.id.etGigName);
        etEventDescription = (EditText)findViewById(R.id.etDescription);
    }

    protected void setDateTimeField(){
        etFromDate.setOnClickListener(this);
        etToDate.setOnClickListener(this);
        etTimeFrom.setOnClickListener(this);
        etTimeTo.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, day);
                etFromDate.setText(dateFormatter.format(newDate.getTime()));
                fromDate = dateFormatter.format(newDate.getTime());
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, day);
                etToDate.setText(dateFormatter.format(newDate.getTime()));
                toDate = dateFormatter.format(newDate.getTime());
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        fromTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Calendar newTime = Calendar.getInstance();
                newTime.set(hour, minute);
                etTimeFrom.setText(timeFormatter.format(newTime.getTime()));
                fromTime = timeFormatter.format(newTime.getTime());
            }
        }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);

        toTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Calendar newTime = Calendar.getInstance();
                newTime.set(hour, minute);
                etTimeTo.setText(timeFormatter.format(newTime.getTime()));
                toTime = timeFormatter.format(newTime.getTime());
            }
        }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);
    }

    public void onClick(View view){
        if(view == etFromDate){
            fromDatePickerDialog.show();
        } else if(view == etToDate){
            toDatePickerDialog.show();
        } else if(view == etTimeFrom){
            fromTimePickerDialog.show();
        } else if (view == etTimeTo){
            toTimePickerDialog.show();
        }
    }

    //autocomplete feature - google places api
    PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
            getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

    public PlaceAutocompleteFragment getAutocompleteFragment() {
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.d("Place selected: ", String.valueOf(place));

                pName = place.getName().toString();
                platlng = place.getLatLng();
                double lat = platlng.latitude;
                double lng = platlng.longitude;
                eventLat = String.valueOf(lat);
                eventLng = String.valueOf(lng);
            }

            @Override
            public void onError(Status status) {

            }
        });
        return autocompleteFragment;
    }

    public void onTvProceed(View view){
        eventName = etEventName.getText().toString();
        eventDescription = etEventDescription.getText().toString();

        //db.addEvent();
        startActivity(new Intent(PostGig.this, ImageUpload.class));
    }

    public void onTvBack(View view) {
        finish();
    }
}
