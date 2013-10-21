package net.sitecore.android.sdk.web;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

import static net.sitecore.android.sdk.web.LogUtils.LOGD;

class ContactRemover {

    private ContentResolver mResolver;

    public ContactRemover(ContentResolver resolver) {
        mResolver = resolver;
    }

    public boolean remove(String id) {
        int result = removeContacts(ContactsContract.Contacts._ID + " = ?", new String[]{id});
        return result > 0;
    }

    public boolean removeMultiple(String[] ids) {
        final String args = TextUtils.join(", ", ids);
        final String selection = ContactsContract.Contacts._ID + " in (" + args + ")";

        int result = removeContacts(selection, null);
        return result > 0;
    }

    private int removeContacts(String selection, String[] selectionArgs) {
        int result = 0;

        Cursor cursor = mResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, selection, selectionArgs, null);
        LOGD("Found " + cursor.getCount() + " contacts to delete.");
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);

                result += mResolver.delete(uri, null, null);
                cursor.moveToNext();
            }
        }

        return result;
    }

}
