package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PetDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shelter.db"; //Data base name.
    private static final int DATABASE_VERSION = 1;  //Data base version.

    private static final String CREATE_TABLE_STATEMENT = "CREATE TABLE " +
            PetsContract.PetsEntry.TABLE_NAME +
            "(" +
            PetsContract.PetsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            PetsContract.PetsEntry.COLUMN_PET_NAME + " TEXT NOT NULL," +
            PetsContract.PetsEntry.COLUMN_PET_BREED + " TEXT," +
            PetsContract.PetsEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL," +
            PetsContract.PetsEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0" +
            ");";

    private static final String DELETE_TABLE_STATEMENT = "DROP TABLE IF EXISTS " + PetsContract.PetsEntry.TABLE_NAME;

    PetDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
}

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DELETE_TABLE_STATEMENT);
        onCreate(sqLiteDatabase);
    }
}
