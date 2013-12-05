package net.sitecore.android.sdk.api.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.ArrayList;

import static net.sitecore.android.sdk.api.LogUtils.LOGV;
import static net.sitecore.android.sdk.api.provider.ScItemsContract.Fields;
import static net.sitecore.android.sdk.api.provider.ScItemsContract.Items;
import static net.sitecore.android.sdk.api.provider.ScItemsDatabase.Tables;

public class ScItemsProvider extends ContentProvider {

    private static final int ITEMS = 100;
    private static final int ITEMS_ID = 101;

    private static final int ITEMS_FIELDS = 110;

    private static final int FIELDS = 200;
    private static final int FIELDS_ID = 201;

    private ScItemsDatabase mDatabaseHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ScItemsContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, "items", ITEMS);
        matcher.addURI(authority, "items/fields", ITEMS_FIELDS);
        matcher.addURI(authority, "items/*", ITEMS_ID);

        matcher.addURI(authority, "fields", FIELDS);
        matcher.addURI(authority, "fields/*", FIELDS_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new ScItemsDatabase(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case ITEMS:
                return Items.CONTENT_TYPE;

            case ITEMS_ID:
                return Items.CONTENT_ITEM_TYPE;

            case FIELDS:
                return Fields.CONTENT_TYPE;

            case FIELDS_ID:
                return Fields.CONTENT_ITEM_TYPE;

            case ITEMS_FIELDS:
                return Items.CONTENT_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        final SelectionBuilder builder = new SelectionBuilder();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS: {
                builder.table(Tables.ITEMS);
                break;
            }
            case ITEMS_ID: {
                String itemId = Items.getItemId(uri);
                builder.table(Tables.ITEMS).where(Items.ITEM_ID + "=?", itemId);
                break;
            }
            case FIELDS: {
                builder.table(Tables.FIELDS);
                break;
            }
            case FIELDS_ID: {
                String fieldId = Fields.getFieldId(uri);
                builder.table(Tables.FIELDS).where(Fields.FIELD_ID + "=?", fieldId);
                break;
            }
            case ITEMS_FIELDS: {
                builder.table(Tables.ITEMS_JOIN_FIELDS)
                        .mapToTable(Items._ID, Tables.ITEMS)
                        .mapToTable(Items.ITEM_ID, Tables.ITEMS);
                break;
            }
            default:
                throw new UnsupportedOperationException("Not supported query uri: " + uri);
        }

        Cursor c = builder.where(selection, selectionArgs).query(db, projection, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        LOGV("Insert: " + uri);
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        ContentResolver resolver = getContext().getContentResolver();
        switch (match) {
            case ITEMS: {
                db.insertOrThrow(Tables.ITEMS, null, values);
                resolver.notifyChange(Items.CONTENT_URI, null);
                return Items.buildItemUri(values.getAsString(Items.ITEM_ID));
            }

            case FIELDS: {
                db.insertOrThrow(Tables.FIELDS, null, values);
                resolver.notifyChange(Fields.CONTENT_URI, null);
                String fieldId = values.getAsString(Fields.FIELD_ID);
                return Fields.buildFieldUri(fieldId);
            }

            default:
                throw new UnsupportedOperationException("Not supported insert uri: " + uri);
        }

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        final SelectionBuilder builder = new SelectionBuilder();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case ITEMS:
                builder.table(Tables.ITEMS);
                break;

            case ITEMS_ID:
                builder.table(Tables.ITEMS);
                break;

            case FIELDS:
                builder.table(Tables.FIELDS);
                break;


            default:
                throw new UnsupportedOperationException("Not supported delete uri: " + uri);
        }

        int result = builder.where(selection, selectionArgs).delete(db);
        if (match == ITEMS_ID) {
            getContext().getContentResolver().notifyChange(Items.CONTENT_URI, null);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        final SelectionBuilder builder = new SelectionBuilder();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case ITEMS:
                builder.table(Tables.ITEMS);
                break;

            case ITEMS_ID:
                String itemId = Items.getItemId(uri);
                builder.table(Tables.ITEMS).where(Items.ITEM_ID + "=?", itemId);
                break;

            case FIELDS:
                builder.table(Tables.FIELDS);
                break;

            default:
                throw new UnsupportedOperationException("Not supported update uri: " + uri);
        }

        int result = builder.where(selection, selectionArgs).update(db, values);
        if (match == ITEMS_ID) {
            getContext().getContentResolver().notifyChange(Items.CONTENT_URI, null);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    /**
     * Apply the given set of {@link ContentProviderOperation}, executing inside
     * a {@link SQLiteDatabase} transaction. All changes will be rolled back if
     * any single one fails.
     */
    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            final int numOperations = operations.size();
            LOGV("Applying %d operations.", numOperations);
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }
            db.setTransactionSuccessful();
            return results;
        } finally {
            db.endTransaction();
        }
    }
}
