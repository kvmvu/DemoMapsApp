package com.example.eric.demomapsapp;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.eric.demomapsapp.model.Ticket;

import java.util.List;

/**
 * Created by Chrispine on 1/19/2017.
 */

public class NewTicketFragment extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_ticket, container, false);
        getDialog().setTitle("New Ticket");
        return rootView;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View rootView = inflater.inflate(R.layout.new_ticket, null);
        builder.setView(rootView);

        builder.setPositiveButton("Add Ticket", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DbHelper db = new DbHelper(getActivity());
                EditText ticketName = (EditText) rootView.findViewById(R.id.ticketName);
                EditText ticketPrice = (EditText) rootView.findViewById(R.id.ticketPrice);
                EditText ticketDescription = (EditText) rootView.findViewById(R.id.ticketDescription);
                String name = String.valueOf(ticketName.getText());
                String price = String.valueOf(ticketPrice.getText());
                String description = String.valueOf(ticketDescription.getText());

                Ticket ticket = new Ticket(name, price, description);
                db.addTicket(ticket);
                List<Ticket> ticketList = db.getAllTickets();
                ((EventTickets)getActivity()).createList(ticketList);
                dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();
    }
}
