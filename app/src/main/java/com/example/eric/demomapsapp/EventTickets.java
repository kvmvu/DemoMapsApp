package com.example.eric.demomapsapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eric.demomapsapp.adapter.TicketAdapater;
import com.example.eric.demomapsapp.model.Ticket;

import java.util.List;

public class EventTickets extends AppCompatActivity {
    DbHelper db = new DbHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_tickets);

        Toolbar toolbar = (Toolbar) findViewById(R.id.add_event_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Event Tickets");

        List<Ticket> ticketList = db.getAllTickets();
        createList(ticketList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_tickets_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_add:
                FragmentManager fm = getFragmentManager();
                NewTicketFragment dialogFragment = new NewTicketFragment();
                dialogFragment.show(fm, "New Ticket");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createList(List<Ticket> ticketList){
        TextView textView = (TextView) findViewById(R.id.ticketsCount);
        ListView listView = (ListView) findViewById(R.id.ticketList);

        int ticketCount = db.getTicketCount();
        textView.setText(String.valueOf(ticketCount));
        TicketAdapater ticketAdapater = new TicketAdapater(EventTickets.this, ticketList);
        listView.setAdapter(ticketAdapater);
    }

    public void goToQrScanner(View view) {
        Intent intent = new Intent(EventTickets.this, QRticketScanner.class);
        startActivity(intent);
    }
}
