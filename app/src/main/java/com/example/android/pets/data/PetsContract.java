package com.example.android.pets.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class PetsContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.pets";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private PetsContract(){}

    public static final class PetsEntry implements BaseColumns{

        public static final String PATH_PETS = "pets";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PETS);
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + BASE_CONTENT_URI + "/" + PATH_PETS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + BASE_CONTENT_URI + "/" + PATH_PETS;

        //Constant for table name.
        public static final String TABLE_NAME = "pets";

        //Constant for column pet id.
        public static final String _ID = BaseColumns._ID;
        //Constant for column pet name.
        public static final String COLUMN_PET_NAME = "name";
        //Constant for column pet breed.
        public static final String COLUMN_PET_BREED = "breed";
        //Constant for column pet gender.
        public static final String COLUMN_PET_GENDER = "gender";
        //Constant for column weight.
        public static final String COLUMN_PET_WEIGHT = "weight";

        //Constants for gender.
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;
        public static final int GENDER_UNKNOWN = 0;
    }
}
