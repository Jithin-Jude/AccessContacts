package com.jithinjude.accesscontacts;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ContactsRecyclerViewAdapter contactsRecyclerViewAdapter;

    public static ArrayList <Contacts> contactList;

    RecyclerView contactsRecyclerView;

    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 2;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactsRecyclerView = (RecyclerView) findViewById(R.id.rv_contacts);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        checkPermission();
    }

    private  void checkPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            new loadContacts().execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    new loadContacts().execute();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Contact permission denied.",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private class loadContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            contactList = readContacts();// Get contacts array list from this
            // method
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            // If array list is not null and is contains value
            if (contactList != null && contactList.size() > 0) {

                // then set total contacts to subtitle
                contactsRecyclerViewAdapter = null;
                if (contactsRecyclerViewAdapter == null) {
                    contactsRecyclerViewAdapter = new ContactsRecyclerViewAdapter(getApplicationContext(), contactList);
                    contactsRecyclerView.setAdapter(contactsRecyclerViewAdapter);// set adapter
                }
                contactsRecyclerViewAdapter.notifyDataSetChanged();
            } else {

                // If adapter is null then show toast
                Toast.makeText(MainActivity.this, "There are no contacts.",
                        Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();

        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            // Show Dialog
            progressDialog = ProgressDialog.show(MainActivity.this, "Loading Contacts",
                    "Please Wait...");
        }

    }

    private ArrayList<Contacts> readContacts() {
        ArrayList <Contacts> contactList = new ArrayList <Contacts>();

        Uri uri = ContactsContract.Contacts.CONTENT_URI; // Contact URI
        Cursor contactsCursor = getContentResolver().query(uri, null, null,
                null, ContactsContract.Contacts.DISPLAY_NAME + " ASC ");

        if (contactsCursor.moveToFirst()) {
            do {
                long contctId = contactsCursor.getLong(contactsCursor
                        .getColumnIndex("_ID")); // Get contact ID
                Uri dataUri = ContactsContract.Data.CONTENT_URI; // URI to get
                // data of
                // contacts
                Cursor dataCursor = getContentResolver().query(dataUri, null,
                        ContactsContract.Data.CONTACT_ID + " = " + contctId,
                        null, null);// Retrun data cusror represntative to
                // contact ID

                // Strings to get all details
                String displayName = "";

                // Now start the cusrsor
                if (dataCursor.moveToFirst()) {
                    displayName = dataCursor
                            .getString(dataCursor
                                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));// get

                    if(displayName != null){
                        contactList.add(new Contacts(displayName));
                    }
                }

            } while (contactsCursor.moveToNext());
        }
        return contactList;
    }
}
