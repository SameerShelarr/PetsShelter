package com.example.android.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * {@link ContentProvider} for Pets app.
 */
public class PetProvider extends ContentProvider {

    /**
     * Tag for the log messages
     */
    private static final int PETS = 100;
    private static final int PETS_ID = 101;
    public static final String LOG_TAG = PetProvider.class.getSimpleName();
    private SQLiteOpenHelper mDbHelper;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(PetsContract.CONTENT_AUTHORITY, PetsContract.PetsEntry.PATH_PETS, PETS);
        sUriMatcher.addURI(PetsContract.CONTENT_AUTHORITY, PetsContract.PetsEntry.PATH_PETS + "/#", PETS_ID);
    }

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        //Create and initialize a PetDbHelper object to gain access to the pets database.
        mDbHelper = new PetDbHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                cursor = db.query(PetsContract.PetsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case PETS_ID:
                String selectionNow = PetsContract.PetsEntry._ID + "=?";
                String[] selectionArgsNow = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = db.query(PetsContract.PetsEntry.TABLE_NAME, projection, selectionNow, selectionArgsNow, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), PetsContract.PetsEntry.CONTENT_URI);

        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int matcher = sUriMatcher.match(uri);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long insertedID;

        String name = contentValues.getAsString(PetsContract.PetsEntry.COLUMN_PET_NAME);
        Integer gender = contentValues.getAsInteger(PetsContract.PetsEntry.COLUMN_PET_GENDER);
        Integer weight = contentValues.getAsInteger(PetsContract.PetsEntry.COLUMN_PET_WEIGHT);
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cant be empty or null");
        }


        if (gender == null || (gender != PetsContract.PetsEntry.GENDER_UNKNOWN && gender != PetsContract.PetsEntry.GENDER_MALE && gender != PetsContract.PetsEntry.GENDER_FEMALE)) {
            throw new IllegalArgumentException("Gender cannot be null or invalid gender selected");
        }

        if (weight != null && weight < 0) {
            throw new IllegalArgumentException("Pet requires valid weight");
        }

        if (matcher == PETS) {
            insertedID = db.insert(PetsContract.PetsEntry.TABLE_NAME, null, contentValues);
            if (insertedID == -1) {
                return null;
            }
        } else {
            throw new IllegalArgumentException("Insertion is not supported for URI" + uri);
        }

        getContext().getContentResolver().notifyChange(PetsContract.PetsEntry.CONTENT_URI, null);

        return ContentUris.withAppendedId(PetsContract.BASE_CONTENT_URI, insertedID);
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return updatePet(uri, contentValues, selection, selectionArgs);
            case PETS_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = PetsContract.PetsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.size() == 0) {
            return 0;
        }

        if (values.containsKey(PetsContract.PetsEntry.COLUMN_PET_NAME)) {
            String name = values.getAsString(PetsContract.PetsEntry.COLUMN_PET_NAME);
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Name cant be empty");
            }
        }

        if (values.containsKey(PetsContract.PetsEntry.COLUMN_PET_GENDER)) {
            Integer gender = values.getAsInteger(PetsContract.PetsEntry.COLUMN_PET_GENDER);
            if (gender == null || (gender != PetsContract.PetsEntry.GENDER_UNKNOWN && gender != PetsContract.PetsEntry.GENDER_MALE && gender != PetsContract.PetsEntry.GENDER_FEMALE)) {
                throw new IllegalArgumentException("Name cant be empty");
            }
        }

        if (values.containsKey(PetsContract.PetsEntry.COLUMN_PET_WEIGHT)) {
            String name = values.getAsString(PetsContract.PetsEntry.COLUMN_PET_NAME);
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Name cant be empty");
            }
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        getContext().getContentResolver().notifyChange(PetsContract.PetsEntry.CONTENT_URI, null);

        return database.update(PetsContract.PetsEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        getContext().getContentResolver().notifyChange(PetsContract.PetsEntry.CONTENT_URI, null);
        int matcher = sUriMatcher.match(uri);
        if (matcher == PETS) {
            return database.delete(PetsContract.PetsEntry.TABLE_NAME, selection, selectionArgs);
        } else if (matcher == PETS_ID) {
            selection = PetsContract.PetsEntry._ID + "=?";
            selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

            return database.delete(PetsContract.PetsEntry.TABLE_NAME, selection, selectionArgs);
        } else {
            throw new IllegalArgumentException("Cant perform delete operation");
        }
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {

        int matcher = sUriMatcher.match(uri);
        switch (matcher) {
            case PETS:
                return PetsContract.PetsEntry.CONTENT_LIST_TYPE;

            case PETS_ID:
                return PetsContract.PetsEntry.CONTENT_ITEM_TYPE;


            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + matcher);

        }
    }
}