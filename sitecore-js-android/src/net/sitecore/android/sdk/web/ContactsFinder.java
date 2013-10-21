package net.sitecore.android.sdk.web;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.text.TextUtils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static net.sitecore.android.sdk.web.LogUtils.LOGD;

class ContactsFinder {

    private ContentResolver mResolver;

    public ContactsFinder(ContentResolver resolver) {
        mResolver = resolver;
    }

    public JSONArray selectAll() throws JSONException {
        Cursor c = mResolver.query(ContactsContract.Data.CONTENT_URI, Query.PROJECTION, null, null, Query.ORDER_BY_ID_ASC);

        ArrayList<Contact> contacts = new ArrayList<Contact>();
        c.moveToFirst();
        //LogUtils.logCursor(c);

        String currentContactId = null;
        Contact currentContact = null;
        try {
            if (c.moveToFirst()) {
                do {
                    final String contactId = c.getString(Query.CONTACT_ID);
                    final String rawId = c.getString(Query.RAW_CONTACT_ID);

                    if (TextUtils.isEmpty(currentContactId) || !currentContactId.equals(contactId)) {
                        currentContactId = contactId;

                        currentContact = new Contact();
                        currentContact.setId(contactId);
                        currentContact.setRawId(rawId);
                        contacts.add(currentContact);
                    }

                    populateContactRow(c, currentContact);
                } while(c.moveToNext());
            }
        } finally {
            c.close();
        }

        JSONArray result = new JSONArray();
        for (Contact contact : contacts) {
            JSONObject jsonObject = contact.getJSONObject();
            LOGD("Contact loaded:" + jsonObject.toString());
            result.put(jsonObject);
        }

        return result;
    }

    private void populateContactRow(Cursor c, Contact contact) throws JSONException {
        final String mimetype = c.getString(Query.MIMETYPE);

        if (mimetype.equals(CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)) {
            final String firstName = c.getString(Query.FIRST_NAME);
            final String lastName = c.getString(Query.LAST_NAME);

            if (TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName)) {
                contact.setFirstName(c.getString(Query.DISPLAY_NAME));
            } else {
                contact.setFirstName(firstName);
                contact.setLastName(lastName);
            }
        }
        if (mimetype.equals(CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
            contact.addPhone(c.getString(Query.PHONE));
        }
        if (mimetype.equals(CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {
            contact.addEmail(c.getString(Query.EMAIL));
        }
        if (mimetype.equals(CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)) {
            final JSONObject address = new JSONObject();
            address.put("street", c.getString(Query.STREET));
            address.put("city", c.getString(Query.CITY));
            address.put("state", c.getString(Query.REGION));
            address.put("zip", c.getString(Query.ZIP));
            address.put("country", c.getString(Query.COUNTRY));

            contact.addAddress(address);
        }
        if (mimetype.equals(CommonDataKinds.Organization.CONTENT_ITEM_TYPE)) {
            contact.setCompany(c.getString(Query.COMPANY));
        }
        if (mimetype.equals(CommonDataKinds.Event.CONTENT_ITEM_TYPE)) {
            if (ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY == c.getInt(Query.EVENT_TYPE)) {
                contact.setBirthday(c.getString(Query.EVENT_START_DATE));
            }

        }
        if (mimetype.equals(CommonDataKinds.Photo.CONTENT_ITEM_TYPE)) {
            long contactId = c.getLong(Query.CONTACT_ID);
            Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
            Uri photoUri = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
            contact.setPhoto(photoUri.toString());
        }

        if (mimetype.equals(CommonDataKinds.Website.CONTENT_ITEM_TYPE)) {
            contact.addWebsite(c.getString(Query.WEBSITE));
        }
    }

    interface Query {
        String[] PROJECTION = {
                ContactsContract.Contacts._ID,
                ContactsContract.Data.MIMETYPE,
                ContactsContract.Data.CONTACT_ID,
                CommonDataKinds.StructuredName.DISPLAY_NAME,
                CommonDataKinds.StructuredName.GIVEN_NAME,
                CommonDataKinds.StructuredName.FAMILY_NAME,
                CommonDataKinds.Phone.NUMBER,
                CommonDataKinds.Email.DATA,
                CommonDataKinds.Organization.COMPANY,
                CommonDataKinds.StructuredPostal.STREET,
                CommonDataKinds.StructuredPostal.CITY,
                CommonDataKinds.StructuredPostal.REGION,
                CommonDataKinds.StructuredPostal.POSTCODE,
                CommonDataKinds.StructuredPostal.COUNTRY,
                CommonDataKinds.Event.TYPE,
                CommonDataKinds.Event.START_DATE,
                ContactsContract.Data.RAW_CONTACT_ID,
                CommonDataKinds.Website.DATA
        };

        int _ID = 0;
        int MIMETYPE = 1;
        int CONTACT_ID = 2;
        int DISPLAY_NAME = 3;
        int FIRST_NAME = 4;
        int LAST_NAME = 5;
        int PHONE = 6;
        int EMAIL = 7;
        int COMPANY = 8;
        int STREET = 9;
        int CITY = 10;
        int REGION = 11;
        int ZIP = 12;
        int COUNTRY = 13;
        int EVENT_TYPE = 14;
        int EVENT_START_DATE = 15;
        int RAW_CONTACT_ID = 16;
        int WEBSITE = 17;

        String ORDER_BY_ID_ASC = ContactsContract.Data.CONTACT_ID + " ASC";

    }


}
