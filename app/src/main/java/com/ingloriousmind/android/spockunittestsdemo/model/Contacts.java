package com.ingloriousmind.android.spockunittestsdemo.model;

import java.util.List;

/**
 * contacts model
 *
 * @author lavong.soysavanh
 */
public class Contacts {

    private List<Contact> contacts;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contacts)) return false;

        Contacts contacts1 = (Contacts) o;

        return !(contacts != null ? !contacts.equals(contacts1.contacts) : contacts1.contacts != null);

    }

    @Override
    public int hashCode() {
        return contacts != null ? contacts.hashCode() : 0;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }
}
