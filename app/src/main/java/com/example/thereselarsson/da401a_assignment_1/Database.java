package com.example.thereselarsson.da401a_assignment_1;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * class for storing the entered user data
 */
public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "a1Database";
    private static final int DATABASE_VERSION = 1;

    //table 1 - name
    private static final String TABLE_PERSONS = "person";
    private static final String PERSONS_COLUMN_ID = "_id";
    private static final String PERSONS_COLUMN_FIRST_NAME = "firstName";
    private static final String PERSONS_COLUMN_LAST_NAME = "lastName";

    //table 2 - income/outcome
    private static final String TABLE_FINANCES = "FINANCES";
    private static final String FINANCES_COLUMN_ID = "_id";
    private static final String FINANCES_COLUMN_TYPE = "type"; //income or outcome
    private static final String FINANCES_COLUMN_TITLE = "title";
    private static final String FINANCES_COLUMN_DATE = "date"; //FIXA TILL DATE-OBJEKT
    private static final String FINANCES_COLUMN_AMOUNT = "amount";
    private static final String FINANCES_COLUMN_CATEGORY = "category";

    public Database(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PERSONS + "(" +
                PERSONS_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                PERSONS_COLUMN_FIRST_NAME + " TEXT, " +
                PERSONS_COLUMN_LAST_NAME + " TEXT) "
        );

        db.execSQL("CREATE TABLE " + TABLE_FINANCES + "(" +
                FINANCES_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                FINANCES_COLUMN_TYPE + " TEXT, " +
                FINANCES_COLUMN_TITLE + " TEXT, " +
                FINANCES_COLUMN_DATE + " TEXT, " + //FIXA TILL DATE-OBJEKT
                FINANCES_COLUMN_AMOUNT + " INTEGER, " +
                FINANCES_COLUMN_CATEGORY + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSONS + TABLE_FINANCES);
        //onCreate(db);
    }

    public boolean addPerson(String firstName, String lastName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PERSONS_COLUMN_FIRST_NAME, firstName);
        contentValues.put(PERSONS_COLUMN_LAST_NAME, lastName);
        db.insert(TABLE_PERSONS, null, contentValues);
        return true;
    }

    //FIXA TILL DATE-OBJEKT --> ARGUMENT
    public boolean addFinance(String type, String title, String date, int amount, String category) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FINANCES_COLUMN_TYPE, type);
        contentValues.put(FINANCES_COLUMN_TITLE, title);
        contentValues.put(FINANCES_COLUMN_DATE, date); //FIXA TILL DATE-OBJEKT
        contentValues.put(FINANCES_COLUMN_AMOUNT, amount);
        contentValues.put(FINANCES_COLUMN_CATEGORY, category);
        db.insert(TABLE_FINANCES, null, contentValues);
        return true;
    }

    //+ update- och get-metoder för varje table
}
