package net.sitecore.android.sdk.web;

import android.app.Activity;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;


/**
 * Plugin for manipulating with phone contacts.
 * <p>Example :
 * <pre>function createContactWithFirstLastName() {
 *     var firstName = "FirstName";
 *     createContact(firstName,"LastName",null, null, null, null, null, null);
 *     var predicate = function (contact) {
 *         return (contact.firstName.indexOf("FirstName") != -1
 *             && contact.lastName.indexOf("LastName") != -1);
 *     }
 *     var displayFirstLastName = function (contacts) {
 *         for (var i = 0; i < contacts.length; i++) {
 *             scmobile.notification.alert("Contact found", contacts[i].firstName +
 *                 " " + contacts[i].lastName, function () {
 *                 }, 'Ok');
 *         }
 *     }
 *     findContact(predicate, displayFirstLastName);
 * }
 * </pre>
 *
 * @since Sitecore Mobile SDK for Android 1.0
 */
public final class ContactsPlugin extends ScPlugin {

    private static final int REQUEST_NEW_CONTACT = 0;

    private ScCallbackContext mCallbackContext;

    @Override
    public String getPluginName() {
        return "contacts";
    }

    @Override
    public String getPluginJsCode() {
        return IoUtils.readRawTextFile(mContext, R.raw.plugin_contacts);
    }

    /**
     * Saves, deletes or retrieves contacts from contact book.
     *
     * @param method          {@code String} method name.
     * @param params          {@link ScParams} params that will be used by action.
     * @param callbackContext {@link ScCallbackContext} callback for action.
     *
     * @throws JSONException if execution fails.
     */
    @Override
    public void exec(String method, ScParams params, ScCallbackContext callbackContext) throws JSONException {
        mCallbackContext = callbackContext;

        if ("silentSave".equals(method)) {
            saveContact(params);
        } else if ("save".equals(method)) {
            showSaveIntent(params);
        } else if ("remove".equals(method)) {
            removeContact(params);
        } else if ("removeMultiple".equals(method)) {
            removeMultipleContacts(params);
        } else if ("select".equals(method)) {
            ContactsFinder finder = new ContactsFinder(mContext.getContentResolver());
            JSONArray results = finder.selectAll();
            mCallbackContext.sendSuccess(results.toString());
        }

    }

    private void saveContact(ScParams params) {
        ContactCreator contactCreator = new ContactCreator(mContext);
        try {
            long contactId = contactCreator.createOrUpdate(params);
            if (contactId > 0) {
                final String photoUri = params.getString("photo");
                if (TextUtils.isEmpty(photoUri)) {
                    mCallbackContext.sendSuccess();
                } else {
                    //result callback is called from task.
                    new LoadAndSavePhotoTask(mCallbackContext, mContext.getContentResolver(), contactId)
                            .execute(photoUri);
                }
            } else {
                mCallbackContext.sendError("Failed to create/update contact.");
            }
        } catch (RemoteException e) {
            mCallbackContext.sendError(new ScJavascriptError("Failed to create new contact.", e.getMessage()));
        } catch (OperationApplicationException e) {
            mCallbackContext.sendError(new ScJavascriptError("Failed to create new contact.", e.getMessage()));
        }

    }

    private void removeContact(ScParams params) {
        String id = params.getString("id");
        boolean removed = new ContactRemover(mContext.getContentResolver()).remove(id);
        if (removed) {
            mCallbackContext.sendSuccess();
        } else {
            mCallbackContext.sendError("Contact not found");
        }
    }

    private void removeMultipleContacts(ScParams params) {
        String[] ids = params.getStringArray("ids");

        boolean removed = new ContactRemover(mContext.getContentResolver()).removeMultiple(ids);
        if (removed) {
            mCallbackContext.sendSuccess();
        } else {
            mCallbackContext.sendError("Contacts not found for deletion.");
        }
    }

    private void showSaveIntent(ScParams params) {
        final Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

        final String firstName = params.getString("firstName");
        final String lastName = params.getString("lastName");
        String name = "";
        if (!TextUtils.isEmpty(firstName)) {
            name = firstName + " ";
        }
        if (!TextUtils.isEmpty(lastName)) name += lastName;
        intent.putExtra(ContactsContract.Intents.Insert.NAME, name.trim());

        // Set phone numbers
        final String[] phones = params.getStringArray("phones");
        if (phones.length > 0) {
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, phones[0]);
            if (phones.length > 1) intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE, phones[1]);
            if (phones.length > 2) intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE, phones[2]);
        }

        // Set emails
        final String[] emails = params.getStringArray("emails");
        if (emails.length > 0) {
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, emails[0]);
            if (emails.length > 1) intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_EMAIL, emails[1]);
            if (emails.length > 2) intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_EMAIL, emails[2]);
        }

        intent.putExtra(ContactsContract.Intents.Insert.COMPANY, params.getString("company"));

        startActivityForResult(intent, REQUEST_NEW_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NEW_CONTACT) {
            if (resultCode == Activity.RESULT_OK) {
                mCallbackContext.sendSuccess();
            } else {
                mCallbackContext.sendError("Creating new contact cancelled.");
            }
        }
    }

}
