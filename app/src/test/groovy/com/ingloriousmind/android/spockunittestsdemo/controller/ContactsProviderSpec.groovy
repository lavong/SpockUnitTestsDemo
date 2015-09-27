package com.ingloriousmind.android.spockunittestsdemo.controller

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.ingloriousmind.android.spockunittestsdemo.model.Contact
import com.ingloriousmind.android.spockunittestsdemo.model.Contacts
import com.koushikdutta.async.http.AsyncHttpClient
import com.koushikdutta.async.http.AsyncHttpGet
import spock.lang.Specification

class ContactsProviderSpec extends Specification {

    def provider
    def callback = Mock(ContactsProvider.ContactProviderListener)

    def setup() {
        provider = Spy(ContactsProvider, constructorArgs: ["some endpoint url"])
        provider.createEndpointUri() >> Uri.parse("http://localmockhost/")
        provider.createGetRequest() >> Mock(AsyncHttpGet)
        provider.createClient() >> Mock(AsyncHttpClient)
    }

    def "should provide contacts when valid json is given"() {
        given: "a valid contacts json"
        def contacts = new Contacts();
        contacts.setContacts([
                new Contact("john", "doe"),
                new Contact("jane", "doe"),
                new Contact("sheldon", "cooper"),
                new Contact("raj", "koothrappali"),
                new Contact("alex", "dunphy"),
                new Contact("mitchell", "pritchett"),
                new Contact("ross", "geller"),
                new Contact("chandler", "bing"),
        ])
        def contactsJson = new Gson().toJson(contacts)
        provider.createClient().executeString(_, _ as AsyncHttpClient.StringCallback) >> { _, stringCallback ->
            stringCallback.onCompleted(null, null, contactsJson)
        }

        when: "fetching contacts"
        println(contactsJson)
        provider.fetchContacts(callback)

        then: "contacts shall be given"
        1 * callback.onContacts(contacts)
    }

    def "should gracefully handle malformed contacts json"() {
        given: "a malformed contacts json"
        provider.createClient().executeString(_, _ as AsyncHttpClient.StringCallback) >> { _, stringCallback ->
            stringCallback.onCompleted(null, null, "{tis aint no valid jason, yo!}")
        }

        when: "fetching contacts"
        provider.fetchContacts(callback)

        then: "the corresponding exception will be passed along"
        1 * callback.onError({ it instanceof JsonSyntaxException })
    }

    def "should pass exception along in case http request failed"() {
        given: "an exception during communication attempt"
        def thrownException = new IOException("something went wrong")
        provider.createClient().executeString(_, _ as AsyncHttpClient.StringCallback) >> { _, stringCallback ->
            stringCallback.onCompleted(thrownException, null, null)
        }

        when: "fetching contacts"
        provider.fetchContacts(callback)

        then: "the exception will be passed along"
        1 * callback.onError({ it == thrownException })
    }

}
