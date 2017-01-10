package com.example.eric.demomapsapp;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link inviteFriends.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link inviteFriends#newInstance} factory method to
 * create an instance of this fragment.
 */
public class inviteFriends extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String namedir="";//initial string for storage of names for iteration
    String phonedir="";

    String name_array[];//for exploded result from namedir
    String phone_array[];

    ListView contactList;

    public inviteFriends() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment inviteFriends.
     */
    // TODO: Rename and change types and number of parameters
    public static inviteFriends newInstance(String param1, String param2) {
        inviteFriends fragment = new inviteFriends();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*List view to display contact names and number on click*/
        contactList = (ListView) contactList.findViewById(R.id.listOfContacts);

        /*Must have FOUR null values after*/
        Cursor phones = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        //Cursor object (Object that holds result from ContactsContract)
        //Provides random read-write access to the result set returned by a database query.

        while(phones.moveToNext()){
            //Common Data Kinds - Container for definitions of common data types stored in the ContactsContract.Data table
            //Contact name
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            //Contact number
            String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            //Addition to dir string
            if(name != null){
                namedir += name + ",";
                phonedir += number + ",";
            }
            //completed addition of numbers to dir string
        }
        //closing of cursor
        phones.close();

        name_array = namedir.split(",");
        phone_array = phonedir.split(",");

        //creation of array adapter and addition of values to it
        //allows array to be written into a list
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,android.R.id.text1,name_array);
        //binding of adapter to existent list view declared above
        contactList.setAdapter(adapter);
        //setting onclick listener for toast with number
        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String msg = phone_array[position];
                Toast.makeText(view.getContext(), msg,Toast.LENGTH_SHORT).show();
            }
        });

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_invite_friends, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
    }
}
