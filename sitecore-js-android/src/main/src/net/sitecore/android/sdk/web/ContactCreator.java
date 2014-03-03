package net.sitecore.android.sdk.web;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import static net.sitecore.android.sdk.web.LogUtils.LOGD;

class ContactCreator {

    /** Keep the photo size under the 1 MB blog limit. */
    private static final long MAX_PHOTO_SIZE = 1048576;
    private static final String EMAIL_REGEXP = ".+@.+\\.+.+"; /* <anything>@<anything>.<anything>*/

    static final String SELECTION_CONTACT_ID_MIMETYPE = ContactsContract.Data.CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE + "=?";
    static final String SELECTION_RAW_ID_MIMETYPE = ContactsContract.Data.RAW_CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE + "=?";

    private Context mContext;
    private ContentResolver mResolver;

    public ContactCreator(Context context) {
        mContext = context;
        mResolver = context.getContentResolver();
    }

    public long createOrUpdate(ScParams params) throws RemoteException, OperationApplicationException {
        AccountManager mgr = AccountManager.get(mContext);
        Account[] accounts = mgr.getAccounts();
        String accountName = null;
        String accountType = null;

        if (accounts.length == 1) {
            accountName = accounts[0].name;
            accountType = accounts[0].type;
        } else if (accounts.length > 1) {
            for (Account a : accounts) {
                if (a.type.contains("eas") && a.name.matches(EMAIL_REGEXP)) /*Exchange ActiveSync*/ {
                    accountName = a.name;
                    accountType = a.type;
                    break;
                }
            }
            if (accountName == null) {
                for (Account a : accounts) {
                    if (a.type.contains("com.google") && a.name.matches(EMAIL_REGEXP)) /*Google sync provider*/ {
                        accountName = a.name;
                        accountType = a.type;
                        break;
                    }
                }
            }
            if (accountName == null) {
                for (Account a : accounts) {
                    if (a.name.matches(EMAIL_REGEXP)) /*Last resort, just look for an email address...*/ {
                        accountName = a.name;
                        accountType = a.type;
                        break;
                    }
                }
            }
        }

        String id = params.getString("id");
        if (TextUtils.isEmpty(id)) {
            return createNewContact(params, accountType, accountName);
        } else {
            return modifyContact(id, params, accountType, accountName);
        }

    }

    private long modifyContact(String id, ScParams params, String accountType, String accountName)
            throws RemoteException, OperationApplicationException {
        LOGD("Modifying contact %s: %s", id, params.toString());
        int rawId = params.optInt("rawId");

        final ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ContentProviderOperation.Builder builder;

        ops.add(ContentProviderOperation.newUpdate(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, accountType)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, accountName)
                .build());

        //Modify name
        final String firstName = params.getString("firstName");
        final String lastName = params.getString("lastName");
        String fullName = "";
        if (!TextUtils.isEmpty(firstName)) {
            fullName = firstName;
        }

        if (!TextUtils.isEmpty(lastName)) fullName += " " + lastName;

        builder = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(SELECTION_CONTACT_ID_MIMETYPE, new String[]{
                        id,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE})
                .withValue(CommonDataKinds.StructuredName.DISPLAY_NAME, fullName)
                .withValue(CommonDataKinds.StructuredName.GIVEN_NAME, firstName)
                .withValue(CommonDataKinds.StructuredName.FAMILY_NAME, lastName);

        ops.add(builder.build());

        //Delete old phones
        ops.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
                .withSelection(SELECTION_RAW_ID_MIMETYPE, new String[]{
                        "" + rawId,
                        CommonDataKinds.Phone.CONTENT_ITEM_TYPE})
                .build());

        //Insert new phones
        final String[] phones = params.getStringArray("phones");
        for (String phone : phones) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
                    .withValue(ContactsContract.Data.MIMETYPE, CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(CommonDataKinds.Phone.NUMBER, phone)
                    .withValue(CommonDataKinds.Phone.TYPE, CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }

        //Delete old emails
        ops.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
                .withSelection(SELECTION_RAW_ID_MIMETYPE, new String[]{
                        "" + rawId,
                        CommonDataKinds.Email.CONTENT_ITEM_TYPE})
                .build());

        //Insert new emails
        final String[] emails = params.getStringArray("emails");
        for (String email : emails) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
                    .withValue(ContactsContract.Data.MIMETYPE, CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(CommonDataKinds.Email.DATA, email)
                    .withValue(CommonDataKinds.Email.TYPE, CommonDataKinds.Email.TYPE_HOME)
                    .build());
        }

        //Delete old company
        ops.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
                .withSelection(SELECTION_RAW_ID_MIMETYPE, new String[]{
                        "" + rawId,
                        CommonDataKinds.Organization.CONTENT_ITEM_TYPE})
                .build());

        //Insert new company
        final String company = params.getString("company");
        if (!TextUtils.isEmpty(company)) ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
                .withValue(ContactsContract.Data.MIMETYPE, CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                .withValue(CommonDataKinds.Organization.TYPE, CommonDataKinds.Organization.TYPE_WORK)
                .withValue(CommonDataKinds.Organization.COMPANY, company)
                .withValue(CommonDataKinds.Organization.TITLE, company)
                .build());

        //Delete old websites
        ops.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
                .withSelection(SELECTION_RAW_ID_MIMETYPE, new String[]{
                        "" + rawId,
                        CommonDataKinds.Website.CONTENT_ITEM_TYPE})
                .build());

        //Insert new websites
        final String[] websites = params.getStringArray("websites");
        for (String site : websites) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
                    .withValue(ContactsContract.Data.MIMETYPE, CommonDataKinds.Website.CONTENT_ITEM_TYPE)
                    .withValue(CommonDataKinds.Website.DATA, site)
                    .withValue(CommonDataKinds.Website.TYPE, CommonDataKinds.Email.TYPE_OTHER)
                    .build());
        }

        //Delete old birthday
        ops.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
                .withSelection(SELECTION_RAW_ID_MIMETYPE, new String[]{
                        "" + rawId,
                        CommonDataKinds.Event.CONTENT_ITEM_TYPE})
                .build());

        //Insert new birthday
        String birthday = params.getString("birthday");
        if (!TextUtils.isEmpty(birthday)) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Event.TYPE, ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY)
                    .withValue(ContactsContract.CommonDataKinds.Event.START_DATE, birthday)
                    .build());
        }

        //Delete old addresses
        ops.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
                .withSelection(SELECTION_RAW_ID_MIMETYPE, new String[]{
                        "" + rawId,
                        CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE})
                .build());

        //Insert new addresses
        List<ScParams.ScAddress> addresses = params.getAddressesList();
        for (ScParams.ScAddress address : addresses) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
                    .withValue(ContactsContract.Data.MIMETYPE, CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                    .withValue(CommonDataKinds.StructuredPostal.TYPE, CommonDataKinds.StructuredPostal.TYPE_WORK)
                    .withValue(CommonDataKinds.StructuredPostal.STREET, address.street)
                    .withValue(CommonDataKinds.StructuredPostal.CITY, address.city)
                    .withValue(CommonDataKinds.StructuredPostal.REGION, address.state)
                    .withValue(CommonDataKinds.StructuredPostal.POSTCODE, address.zip)
                    .withValue(CommonDataKinds.StructuredPostal.COUNTRY, address.country)
                    .build());
        }

        applyOperations(ops);
        return Long.parseLong(id);
    }

    private long createNewContact(ScParams params, String accountType, String accountName) throws RemoteException,
            OperationApplicationException {
        LOGD("Creating contact: %s", params.toString());
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, accountType)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, accountName)
                .build());

        // Add name
        final String firstName = params.getString("firstName");
        final String lastName = params.getString("lastName");
        insertName(ops, firstName, lastName);

        // Add phone numbers
        final String[] phones = params.getStringArray("phones");
        for (String phone : phones) {
            insertPhone(ops, phone);
        }

        // Add emails
        final String[] emails = params.getStringArray("emails");
        for (String email : emails) {
            insertEmail(ops, email);
        }

        // Add organizations
        final String company = params.getString("company");
        if (!TextUtils.isEmpty(company)) insertOrganization(ops, company);

        // Add urls
        final String[] websites = params.getStringArray("websites");
        for (String site : websites) {
            insertWebsite(ops, site);
        }

        // Add birthday
        String birthday = params.getString("birthday");
        if (!TextUtils.isEmpty(birthday)) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Event.TYPE, ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY)
                    .withValue(ContactsContract.CommonDataKinds.Event.START_DATE, birthday)
                    .build());
        }

        List<ScParams.ScAddress> addresses = params.getAddressesList();
        for (ScParams.ScAddress address : addresses) {
            insertAddress(ops, address);
        }

        return applyOperations(ops);
    }

    private long applyOperations(ArrayList<ContentProviderOperation> operations) throws RemoteException, OperationApplicationException {
        ContentProviderResult[] cpResults = mResolver.applyBatch(ContactsContract.AUTHORITY, operations);
        if (cpResults.length > 0) {
            if (cpResults[0].uri != null) {
                return ContentUris.parseId(cpResults[0].uri);
            }
        }

        return 0;
    }

    private void insertName(ArrayList<ContentProviderOperation> ops, String firstName, String lastName) {
        String name = "";
        if (!TextUtils.isEmpty(firstName)) {
            name = firstName;
        }

        if (!TextUtils.isEmpty(lastName)) name += " " + lastName;

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(CommonDataKinds.StructuredName.DISPLAY_NAME, name.trim())
                .withValue(CommonDataKinds.StructuredName.GIVEN_NAME, firstName)
                .withValue(CommonDataKinds.StructuredName.FAMILY_NAME, lastName)
                .build());
    }

    private void insertPhone(ArrayList<ContentProviderOperation> ops, String phone) {
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(CommonDataKinds.Phone.NUMBER, phone)
                .withValue(CommonDataKinds.Phone.TYPE, CommonDataKinds.Phone.TYPE_MOBILE)
                .build());
    }

    private void insertEmail(ArrayList<ContentProviderOperation> ops, String email) {
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(CommonDataKinds.Email.DATA, email)
                .withValue(CommonDataKinds.Email.TYPE, CommonDataKinds.Email.TYPE_HOME)
                .build());
    }

    private void insertOrganization(ArrayList<ContentProviderOperation> ops, String org) {
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                .withValue(CommonDataKinds.Organization.TYPE, CommonDataKinds.Organization.TYPE_WORK)
                .withValue(CommonDataKinds.Organization.COMPANY, org)
                .withValue(CommonDataKinds.Organization.TITLE, org)
                .build());
    }

    private void insertWebsite(ArrayList<ContentProviderOperation> ops, String website) {
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, CommonDataKinds.Website.CONTENT_ITEM_TYPE)
                .withValue(CommonDataKinds.Website.DATA, website)
                .withValue(CommonDataKinds.Website.TYPE, CommonDataKinds.Website.TYPE_OTHER)
                .build());
    }

    private void insertAddress(ArrayList<ContentProviderOperation> ops, ScParams.ScAddress address) {
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(CommonDataKinds.StructuredPostal.TYPE, CommonDataKinds.StructuredPostal.TYPE_WORK)
                .withValue(CommonDataKinds.StructuredPostal.STREET, address.street)
                .withValue(CommonDataKinds.StructuredPostal.CITY, address.city)
                .withValue(CommonDataKinds.StructuredPostal.REGION, address.state)
                .withValue(CommonDataKinds.StructuredPostal.POSTCODE, address.zip)
                .withValue(CommonDataKinds.StructuredPostal.COUNTRY, address.country)
                .build());
    }

}
