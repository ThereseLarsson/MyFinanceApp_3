package com.example.thereselarsson.da401a_assignment_1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    private static final String INCOME_COLUMN_TITLE = "title";
    private static final String INCOME_COLUMN_DATE = "date";
    private static final String INCOME_COLUMN_AMOUNT = "amount";
    private static final String INCOME_COLUMN_CATEGORY = "category";

    //table 3 - outcome
    private static final String TABLE_OUTCOME = "outcome";
    private static final String OUTCOME_COLUMN_ID = "_id";
    private static final String OUTCOME_COLUMN_TITLE = "title";
    private static final String OUTCOME_COLUMN_DATE = "date";
    private static final String OUTCOME_COLUMN_AMOUNT = "amount";
    private static final String OUTCOME_COLUMN_CATEGORY = "category";

    public Database(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSONS + TABLE_INCOME + TABLE_OUTCOME);
        onCreate(db); //recreate tables
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
                INCOME_COLUMN_TITLE + " TEXT, " +
                INCOME_COLUMN_DATE + " TEXT, " + //FIXA TILL DATE-OBJEKT
                INCOME_COLUMN_AMOUNT + " DOUBLE, " +
                INCOME_COLUMN_CATEGORY + " TEXT)"
        );

        db.execSQL("CREATE TABLE " + TABLE_OUTCOME + "(" +
                OUTCOME_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                OUTCOME_COLUMN_TITLE + " TEXT, " +
                OUTCOME_COLUMN_DATE + " TEXT, " + //FIXA TILL DATE-OBJEKT
                OUTCOME_COLUMN_AMOUNT + " DOUBLE, " +
                OUTCOME_COLUMN_CATEGORY + " TEXT)"
        );
    }

    /**
     * Methods regarding Person-table
     * -----------------------------------------------------------------------------
     */

    /**
     * adds a person into the database
     * @param firstName
     * @param lastName
     */
    public void addPerson(String firstName, String lastName) {
        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues contentValues = new ContentValues();
        contentValues.put(PERSONS_COLUMN_FIRST_NAME, firstName);
        contentValues.put(PERSONS_COLUMN_LAST_NAME, lastName);

        // Insert the new row, returning the primary key value of the new row
        db.insert(TABLE_PERSONS, null, contentValues);
    }

    /**
     * checks if a user existss
     * @return
     */
    public boolean userExists() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_PERSONS, null);
        if(cursor != null) {
            cursor.moveToFirst();
            if(cursor.getInt(0) == 0) { //table is empty
                cursor.close();
                return false;
            } else {
                cursor.close();
                return true;
            }
        }
        return false;
    }

    /**
     * gets the users first name and last name and
     * returns these as one string
     * @return
     */
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

    /**
     * Methods regarding Income-table
     * -----------------------------------------------------------------------------
     */

    public void addIncome(String title, String date, double amount, String category) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(INCOME_COLUMN_TITLE, title);
        contentValues.put(INCOME_COLUMN_DATE, date);
        contentValues.put(INCOME_COLUMN_AMOUNT, amount);
        contentValues.put(INCOME_COLUMN_CATEGORY, category);
        db.insert(TABLE_INCOME, null, contentValues);
    }

    public double getTotalIncome() {
        double totalIncome = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery( "SELECT * FROM " + TABLE_INCOME, null ); //rad3
        if(cursor != null) {
            if(cursor.moveToFirst()) {
                do {
                    totalIncome = totalIncome + cursor.getDouble(3);
                } while(cursor.moveToNext());
            }
        }
        cursor.close();
        return totalIncome;
    }

    public String getTotalIncomeFromDate() {
        String result = "";

        //do something

        return result;
    }

    /**
     * Methods regarding Outcome-table
     * -----------------------------------------------------------------------------
     */

    public void addOutcome(String title, String date, double amount, String category) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OUTCOME_COLUMN_TITLE, title);
        contentValues.put(OUTCOME_COLUMN_DATE, date);
        contentValues.put(OUTCOME_COLUMN_AMOUNT, amount);
        contentValues.put(OUTCOME_COLUMN_CATEGORY, category);
        db.insert(TABLE_OUTCOME, null, contentValues);
    }

    public double getTotalOutcome() {
        double totalOutcome = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery( "SELECT * FROM " + TABLE_OUTCOME, null );
        if(cursor != null) {
            if(cursor.moveToFirst()) {
                do {
                    totalOutcome = totalOutcome + cursor.getDouble(3);
                } while(cursor.moveToNext());
            }
        }
        cursor.close();
        return totalOutcome;
    }

    public String getTotalOutcomeFromDate() {
        String result = "";

        //do something

        return result;
    }

    /**
     * Methods for printing contents of tables
     * -----------------------------------------------------------------------------
     */

    /**
     * prints the contents of the person table
     * used for testing purposes (to see that the data is stored correctly)
     */
    public void printTablePersonAsString() {
        SQLiteDatabase dbHandler = this.getReadableDatabase();
        String tableString = String.format("TABLE %s:\n", TABLE_PERSONS);
        Cursor allRows  = dbHandler.rawQuery("SELECT * FROM " + TABLE_PERSONS, null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }
        allRows.close();
        Log.d(null, tableString);
    }

    /**
     * prints the contents of the income table
     * used for testing purposes (to see that the data is stored correctly)
     */
    public void printTableIncomeAsString() {
        SQLiteDatabase dbHandler = this.getReadableDatabase();
        String tableString = String.format("TABLE %s:\n", TABLE_INCOME);
        Cursor allRows  = dbHandler.rawQuery("SELECT * FROM " + TABLE_INCOME, null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }
        allRows.close();
        Log.d(null, tableString);
    }

    /**
     * prints the contents of the outcome table
     * used for testing purposes (to see that the data is stored correctly)
     */
    public void printTableOutcomeAsString() {
        SQLiteDatabase dbHandler = this.getReadableDatabase();
        String tableString = String.format("TABLE %s:\n", TABLE_OUTCOME);
        Cursor allRows  = dbHandler.rawQuery("SELECT * FROM " + TABLE_OUTCOME, null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }
        allRows.close();
        Log.d(null, tableString);
    }
}
