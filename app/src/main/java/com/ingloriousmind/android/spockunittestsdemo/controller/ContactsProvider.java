package com.ingloriousmind.android.spockunittestsdemo.controller;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ingloriousmind.android.spockunittestsdemo.model.Contacts;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpRequest;
import com.koushikdutta.async.http.AsyncHttpResponse;

/**
 * contact provider
 *
 * @author lavong.soysavanh
 */
public class ContactsProvider {

    private final String contactsEndpoint;
    private final Gson gson;

    public interface ContactProviderListener {
        void onContacts(Contacts contacts);

        void onError(Throwable throwable);
    }

    public ContactsProvider(String contactsEndpoint) {
        this.contactsEndpoint = contactsEndpoint;
        this.gson = new Gson();
    }

    public void fetchContacts(final ContactProviderListener callback) {
        createClient().executeString(createGetRequest(), new AsyncHttpClient.StringCallback() {
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse asyncHttpResponse, String result) {
                if (result != null) {
                    try {
                        callback.onContacts(gson.fromJson(result, Contacts.class));
                    } catch (JsonSyntaxException jse) {
                        callback.onError(jse);
                    }
                } else {
                    callback.onError(e);
                }
            }
        });
    }

    AsyncHttpClient createClient() {
        return AsyncHttpClient.getDefaultInstance();
    }

    AsyncHttpRequest createGetRequest() {
        return new AsyncHttpGet(createEndpointUri());
    }

    Uri createEndpointUri() {
        return Uri.parse(contactsEndpoint);
    }


}
