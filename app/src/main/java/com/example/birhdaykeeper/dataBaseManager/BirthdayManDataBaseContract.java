package com.example.birhdaykeeper.dataBaseManager;

import android.provider.BaseColumns;

public final class BirthdayManDataBaseContract {

    public final static String DATABASE_NAME = "birthDb.db";
    public final static int DATABASE_VERSION = 1;


    public BirthdayManDataBaseContract(){

    }

    public static abstract class DbContract implements BaseColumns {

        public final static String TABLE_NAME = "BirthdayMen";
        public final static String COLUMN_ID = "id";
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_SURNAME = "surname";
        public final static String COLUMN_EMAIL = "email";
        public final static String COLUMN_PHONE = "phone";
        public final static String COLUMN_CATEGORY = "category";
        public final static String COLUMN_BIRTH = "birth_data";

    }

}
