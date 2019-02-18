package com.example.thereselarsson.da401a_assignment_1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    //table 2 - income
    private static final String TABLE_INCOME = "income";
    private static final String INCOME_COLUMN_ID = "_id";
    private static final String INCOME_COLUMN_TYPE = "type"; //income or outcome
    private static final String INCOME_COLUMN_TITLE = "title";
    private static final String INCOME_COLUMN_DATE = "date"; //FIXA TILL DATE-OBJEKT
    private static final String INCOME_COLUMN_AMOUNT = "amount";
    private static final String INCOME_COLUMN_CATEGORY = "category";

    //table 3 - outcome
    private static final String TABLE_OUTCOME = "outcome";
    private static final String OUTCOME_COLUMN_ID = "_id";
    private static final String OUTCOME_COLUMN_TYPE = "type"; //income or outcome
    private static final String OUTCOME_COLUMN_TITLE = "title";
    private static final String OUTCOME_COLUMN_DATE = "date"; //FIXA TILL DATE-OBJEKT
    private static final String OUTCOME_COLUMN_AMOUNT = "amount";
    private static final String OUTCOME_COLUMN_CATEGORY = "category";

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

        db.execSQL("CREATE TABLE " + TABLE_INCOME + "(" +
                INCOME_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                INCOME_COLUMN_TYPE + " TEXT, " +
                INCOME_COLUMN_TITLE + " TEXT, " +
                INCOME_COLUMN_DATE + " TEXT, " + //FIXA TILL DATE-OBJEKT
                INCOME_COLUMN_AMOUNT + " INTEGER, " +
                INCOME_COLUMN_CATEGORY + " TEXT)"
        );

        db.execSQL("CREATE TABLE " + TABLE_OUTCOME + "(" +
                OUTCOME_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                OUTCOME_COLUMN_TYPE + " TEXT, " +
                OUTCOME_COLUMN_TITLE + " TEXT, " +
                OUTCOME_COLUMN_DATE + " TEXT, " + //FIXA TILL DATE-OBJEKT
                OUTCOME_COLUMN_AMOUNT + " INTEGER, " +
                OUTCOME_COLUMN_CATEGORY + " TEXT)"
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

    public boolean personExists() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PERSONS, new String[] {"*"}, null, null, null, null, null, null );
        if(cursor != null) {
            return true;
        }
        return false;
    }

    public String getPersonName() {
        String firstName = "";
        String lastName = "";
        String fullName = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PERSONS, new String[] {"*"}, null, null, null, null, null, null );
        if(cursor != null) {
            if(cursor.moveToFirst()) {
                firstName = cursor.getString(1);
                lastName = cursor.getString(2);
                fullName = firstName + " " + lastName;
            }
            cursor.close();
        }
        db.close();
        return fullName;
    }

    //FIXA TILL DATE-OBJEKT --> ARGUMENT
    public boolean addIncome(String type, String title, String date, int amount, String category) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(INCOME_COLUMN_TYPE, type);
        contentValues.put(INCOME_COLUMN_TITLE, title);
        contentValues.put(INCOME_COLUMN_DATE, date); //FIXA TILL DATE-OBJEKT
        contentValues.put(INCOME_COLUMN_AMOUNT, amount);
        contentValues.put(INCOME_COLUMN_CATEGORY, category);
        db.insert(TABLE_INCOME, null, contentValues);
        return true;
    }

    public int getTotalIncome() {
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.rawQuery( "SELECT * FROM " + TABLE_INCOME, null );
        return -1;
    }

    //FIXA TILL DATE-OBJEKT --> ARGUMENT
    public boolean addOutcome(String type, String title, String date, int amount, String category) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OUTCOME_COLUMN_TYPE, type);
        contentValues.put(OUTCOME_COLUMN_TITLE, title);
        contentValues.put(OUTCOME_COLUMN_DATE, date); //FIXA TILL DATE-OBJEKT
        contentValues.put(OUTCOME_COLUMN_AMOUNT, amount);
        contentValues.put(OUTCOME_COLUMN_CATEGORY, category);
        db.insert(TABLE_OUTCOME, null, contentValues);
        return true;
    }

    public int getTotalOutcome() {
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.rawQuery( "SELECT * FROM " + TABLE_OUTCOME, null );
        return -1;
    }
}
