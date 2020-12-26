package com.example.birhdaykeeper.dataBaseManager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BirthdayManDataBaseHelper extends SQLiteOpenHelper {


    private static final String queryCreation = "CREATE TABLE " +
            BirthdayManDataBaseContract.DbContract.TABLE_NAME + "(" +
            BirthdayManDataBaseContract.DbContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            BirthdayManDataBaseContract.DbContract.COLUMN_NAME + " TEXT" + "," +
            BirthdayManDataBaseContract.DbContract.COLUMN_SURNAME + " TEXT," +
            BirthdayManDataBaseContract.DbContract.COLUMN_EMAIL + " TEXT," +
            BirthdayManDataBaseContract.DbContract.COLUMN_PHONE + " TEXT," +
            BirthdayManDataBaseContract.DbContract.COLUMN_CATEGORY + " TEXT," +
            BirthdayManDataBaseContract.DbContract.COLUMN_BIRTH + " TEXT"+")";

    private final static String queryDelete = "DROP TABLE IF EXISTS " +
            BirthdayManDataBaseContract.DbContract.TABLE_NAME;


    public BirthdayManDataBaseHelper(Context context){
        super(context,BirthdayManDataBaseContract.DATABASE_NAME,
                null,BirthdayManDataBaseContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(queryCreation);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(queryDelete);
        onCreate(sqLiteDatabase);
    }
}
