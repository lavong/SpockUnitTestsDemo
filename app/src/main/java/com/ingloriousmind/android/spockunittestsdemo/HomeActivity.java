package com.ingloriousmind.android.spockunittestsdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.ingloriousmind.android.spockunittestsdemo.controller.ContactsProvider;
import com.ingloriousmind.android.spockunittestsdemo.model.Contact;
import com.ingloriousmind.android.spockunittestsdemo.model.Contacts;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "SpockGroovyDemo";
    private ContactsProvider contactsProvider;
    private TextView contactsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        contactsProvider = new ContactsProvider(getString(R.string.contacts_json_endpoint));
        contactsView = (TextView) findViewById(R.id.contacts);
    }

    @Override
    protected void onResume() {
        super.onResume();
        contactsProvider.fetchContacts(new ContactsProvider.ContactProviderListener() {
            @Override
            public void onContacts(Contacts contacts) {
                Log.d(TAG, String.format("fetched and parsed %d contacts: ", contacts.getContacts().size()));
                updateContacts(contacts);
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e(TAG, "failed fetching json data", throwable);
            }
        });
    }

    private void updateContacts(final Contacts contacts) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Contact c : contacts.getContacts()) {
                    contactsView.append(c.toString() + "\n");
                }
            }
        });
    }
}
