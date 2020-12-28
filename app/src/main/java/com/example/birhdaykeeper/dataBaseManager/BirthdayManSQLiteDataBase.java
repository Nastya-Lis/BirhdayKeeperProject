package com.example.birhdaykeeper.dataBaseManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.birhdaykeeper.unit.BirthDayMan;
import com.example.birhdaykeeper.unit.Category;
import com.example.birhdaykeeper.unit.ExceptionBirth;

import java.util.ArrayList;
import java.util.List;

public class BirthdayManSQLiteDataBase {

    private static BirthdayManDataBaseHelper dataBaseHelper;
    private static BirthdayManSQLiteDataBase birthDaySQLiteDataBaseSingleton;
    private SQLiteDatabase sqLiteDatabase;
    Context context;


    private BirthdayManSQLiteDataBase(){}

    public static BirthdayManSQLiteDataBase getInstance(Context context){
        if(birthDaySQLiteDataBaseSingleton == null)
        {
            birthDaySQLiteDataBaseSingleton = new BirthdayManSQLiteDataBase();
        }
        birthDaySQLiteDataBaseSingleton.context = context;
        dataBaseHelper = new BirthdayManDataBaseHelper(context);
        birthDaySQLiteDataBaseSingleton.sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        return birthDaySQLiteDataBaseSingleton;
    }

    public void openOrCreate(Context context1){
        birthDaySQLiteDataBaseSingleton.sqLiteDatabase = context1.openOrCreateDatabase(BirthdayManDataBaseContract.DATABASE_NAME,
                Context.MODE_PRIVATE,null);
    }

    public void closeDataBase(){

        birthDaySQLiteDataBaseSingleton.sqLiteDatabase.close();
    }

    public void addBirthManToDb(BirthDayMan birthDayMan) throws SQLDBException{

        ContentValues contentValues = new ContentValues();
        contentValues.put(BirthdayManDataBaseContract.DbContract.COLUMN_ID,birthDayMan.getId());
        contentValues.put(BirthdayManDataBaseContract.DbContract.COLUMN_NAME,birthDayMan.getName());
        contentValues.put(BirthdayManDataBaseContract.DbContract.COLUMN_SURNAME,birthDayMan.getSurname());
        contentValues.put(BirthdayManDataBaseContract.DbContract.COLUMN_EMAIL,birthDayMan.getEmail());
        contentValues.put(BirthdayManDataBaseContract.DbContract.COLUMN_PHONE,birthDayMan.getPhone());
        contentValues.put(BirthdayManDataBaseContract.DbContract.COLUMN_CATEGORY,birthDayMan.getCategory().toString());
        contentValues.put(BirthdayManDataBaseContract.DbContract.COLUMN_BIRTH,birthDayMan.getBirthData());

        if(sqLiteDatabase.insert
                (BirthdayManDataBaseContract.DbContract.TABLE_NAME,null,contentValues)
                == -1){
            throw new SQLDBException("not inserted");
        }
    }

    public void updateBirthManInDb(BirthDayMan birthDayMan){

        ContentValues contentValues = new ContentValues();
        contentValues.put(BirthdayManDataBaseContract.DbContract.COLUMN_ID,birthDayMan.getId());
        contentValues.put(BirthdayManDataBaseContract.DbContract.COLUMN_NAME,birthDayMan.getName());
        contentValues.put(BirthdayManDataBaseContract.DbContract.COLUMN_SURNAME,birthDayMan.getSurname());
        contentValues.put(BirthdayManDataBaseContract.DbContract.COLUMN_EMAIL,birthDayMan.getEmail());
        contentValues.put(BirthdayManDataBaseContract.DbContract.COLUMN_PHONE,birthDayMan.getPhone());
        contentValues.put(BirthdayManDataBaseContract.DbContract.COLUMN_CATEGORY,birthDayMan.getCategory().toString());
        contentValues.put(BirthdayManDataBaseContract.DbContract.COLUMN_BIRTH,birthDayMan.getBirthData());

        sqLiteDatabase.update(BirthdayManDataBaseContract.DbContract.TABLE_NAME,contentValues,
                BirthdayManDataBaseContract.DbContract.COLUMN_ID + " =? ",
                new String[] {birthDayMan.getId().toString()});

    }

    public void deleteBirthManFromDb(BirthDayMan birthDayMan) throws SQLDBException{
        if(sqLiteDatabase.delete(BirthdayManDataBaseContract.DbContract.TABLE_NAME,
                BirthdayManDataBaseContract.DbContract.COLUMN_ID + " =? AND " +
                        BirthdayManDataBaseContract.DbContract.COLUMN_NAME + " =?",
                new String[] {birthDayMan.getId().toString(),birthDayMan.getName()}) == -1)
            throw new SQLDBException("not deleted");

    }


    public List<BirthDayMan> takeMenByBirth(String currentDate) throws SQLDBException{
        List<BirthDayMan> birthDayManList = new ArrayList<>();
        String cuttingDate  = currentDate.substring(0,5);
        String selection = BirthdayManDataBaseContract.DbContract.COLUMN_BIRTH + " LIKE ?";
        String[] selectionArgs = {cuttingDate + '%'};

        Cursor cursor = sqLiteDatabase.query(BirthdayManDataBaseContract.DbContract.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
                );

        if(cursor.moveToFirst()){
            do {
                birthDayManList.add(takeBirthManFromCursor(cursor));
            }
            while (cursor.moveToNext());
            cursor.close();
            return birthDayManList;
        }
        else
            throw  new SQLDBException("не считаны данные");
    }

    public List<BirthDayMan> takeAllBirthManFromDb() throws SQLDBException{
        List<BirthDayMan> birthDayManList = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.query(BirthdayManDataBaseContract.DbContract.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        if(cursor.moveToFirst()){
            do
                birthDayManList.add(takeBirthManFromCursor(cursor));
            while (cursor.moveToNext());
            cursor.close();
            return birthDayManList;
        }
        else
            throw  new SQLDBException("not readed");
    }

    private BirthDayMan takeBirthManFromCursor(Cursor cursor){
        BirthDayMan birthDayMan = new BirthDayMan();

        int indexId = cursor.getColumnIndexOrThrow(BirthdayManDataBaseContract.DbContract.COLUMN_ID);
        int indexName = cursor.getColumnIndexOrThrow(BirthdayManDataBaseContract.DbContract.COLUMN_NAME);
        int indexSurname = cursor.getColumnIndexOrThrow(BirthdayManDataBaseContract.DbContract.COLUMN_SURNAME);
        int indexEmail = cursor.getColumnIndexOrThrow(BirthdayManDataBaseContract.DbContract.COLUMN_EMAIL);
        int indexPhone = cursor.getColumnIndexOrThrow(BirthdayManDataBaseContract.DbContract.COLUMN_PHONE);
        int indexCategory = cursor.getColumnIndexOrThrow(BirthdayManDataBaseContract.DbContract.COLUMN_CATEGORY);
        int indexBirth = cursor.getColumnIndexOrThrow(BirthdayManDataBaseContract.DbContract.COLUMN_BIRTH);


        birthDayMan.setId(cursor.getInt(indexId));
        birthDayMan.setName(cursor.getString(indexName));
        birthDayMan.setSurname(cursor.getString(indexSurname));
        try {
            birthDayMan.setEmail(cursor.getString(indexEmail));
        } catch (ExceptionBirth exceptionBirth) {
            exceptionBirth.printStackTrace();
        }
        try {
            birthDayMan.setPhone(cursor.getString(indexPhone));
        } catch (ExceptionBirth exceptionBirth) {
            exceptionBirth.printStackTrace();
        }
        birthDayMan.setCategory(Category.convertStringIntoCategoryType(cursor.getString(indexCategory)));
        birthDayMan.setBirthData(cursor.getString(indexBirth));


        return birthDayMan;
    }

}
