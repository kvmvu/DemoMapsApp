package com.example.eric.demomapsapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.eric.demomapsapp.R;
import com.example.eric.demomapsapp.model.Ticket;

import java.util.List;

/**
 * Created by Chrispine on 1/19/2017.
 */

public class TicketAdapater extends BaseAdapter {
    Activity activity;
    LayoutInflater inflater;
    List<Ticket> ticketList;

    public TicketAdapater(Activity activity, List<Ticket> ticketList){
        this.activity = activity;
        this.ticketList = ticketList;

    }
    @Override
    public int getCount() {
        return ticketList.size();
    }

    @Override
    public Object getItem(int i) {
        return ticketList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null)
            view = inflater.inflate(R.layout.ticket_list_item, null);

        TextView ticketName = (TextView) view.findViewById(R.id.item_ticket_name);
        TextView ticketPrice = (TextView) view.findViewById(R.id.item_ticket_price);
        TextView ticketDescription = (TextView) view.findViewById(R.id.item_ticket_description);
        TextView ticketID = (TextView) view.findViewById(R.id.item_ticket_id);

        Ticket ticket = ticketList.get(i);

        ticketName.setText(ticket.get_ticket_name());
        ticketPrice.setText("Ksh. " + ticket.get_ticket_price());
        ticketDescription.setText(ticket.get_ticket_description());
        ticketID.setText(String.valueOf(ticket.get_id()));

        return view;
    }
}
