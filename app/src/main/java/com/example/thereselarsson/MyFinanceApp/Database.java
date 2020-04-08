package com.example.thereselarsson.MyFinanceApp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//TODO: better, more effective solution idea (reduce code redundancy): use ONE table to store income AND income objects
//TODO: how? add additional row 'isIncome' (boolean) to check if income or outcome.

/**
 * Stores data (income and outcome transactions) entered by the user
 */
public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "a1Database";
    private static final int DATABASE_VERSION = 1;

    //table 1 - income
    private static final String TABLE_INCOME = "income";
    private static final String INCOME_COLUMN_ID = "_id";
    private static final String INCOME_COLUMN_TITLE = "title";
    private static final String INCOME_COLUMN_DATE = "date";
    private static final String INCOME_COLUMN_AMOUNT = "amount";
    private static final String INCOME_COLUMN_CATEGORY = "category";

    //table 2 - outcome
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOME + TABLE_OUTCOME);
        onCreate(db); //recreates tables
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_INCOME + "(" +
                INCOME_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                INCOME_COLUMN_TITLE + " TEXT, " +
                INCOME_COLUMN_DATE + " TEXT, " + //FIXA TILL DATE-OBJEKT?
                INCOME_COLUMN_AMOUNT + " DOUBLE, " +
                INCOME_COLUMN_CATEGORY + " TEXT)"
        );

        db.execSQL("CREATE TABLE " + TABLE_OUTCOME + "(" +
                OUTCOME_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                OUTCOME_COLUMN_TITLE + " TEXT, " +
                OUTCOME_COLUMN_DATE + " TEXT, " + //FIXA TILL DATE-OBJEKT?
                OUTCOME_COLUMN_AMOUNT + " DOUBLE, " +
                OUTCOME_COLUMN_CATEGORY + " TEXT)"
        );
    }

    //returns the total number of rows in the income table
    public int getNbrOfIncomeTableRows() {
        return (int) DatabaseUtils.queryNumEntries(getReadableDatabase(), TABLE_INCOME);
    }

    //returns the total number of rows in the outcome table
    public int getNbrOfOutcomeTableRows() {
        return (int) DatabaseUtils.queryNumEntries(getReadableDatabase(), TABLE_OUTCOME);
    }

    /**
     * Methods regarding Income-table
     * -----------------------------------------------------------------------------
     */

    //adds an income to the database (in the income table)
    public void addIncome(String title, String date, double amount, String category) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(INCOME_COLUMN_TITLE, title);
        contentValues.put(INCOME_COLUMN_DATE, date);
        contentValues.put(INCOME_COLUMN_AMOUNT, amount);
        contentValues.put(INCOME_COLUMN_CATEGORY, category);
        db.insert(TABLE_INCOME, null, contentValues);
    }

    //gets the total income (in kr)
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

    //checks of an income exists based on its´ title
    public boolean incomeTitleExists(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery( "SELECT * FROM " + TABLE_INCOME, null );
        if(cursor != null) {
            if(cursor.moveToFirst()) {
                do {
                    if(title.equals(cursor.getString(1))) {
                        return true;
                    }
                } while(cursor.moveToNext());
            }
        }
        cursor.close();
        return false;
    }

    /**
     * gets the values of all the items from the IncomeTable and returns them in a list
     *
     * row 0 - id
     * row 1 - title
     * row 2 - date
     * row 3 - amount
     * row 4- category
     */
    public String[] getIncomeValuesFromRowNbr(int indexInTable) {
        String rowValue = "";
        String[] rowValueList = new String[getNbrOfIncomeTableRows()];
        int index = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_INCOME, new String[] {"*"}, null, null, null, null, null, null );
        if(cursor != null) {
            if(cursor.moveToFirst()) {
                do {
                    rowValue = cursor.getString(indexInTable);
                    //Log.d(null, "Row value at index " + index + " is: " + rowValue);
                    rowValueList[index] = rowValue;
                    index++;
                } while(cursor.moveToNext()) ;
            }
        }
        cursor.close();
        db.close();
        return rowValueList;
    }

    /**
     * Methods regarding Outcome-table
     * -----------------------------------------------------------------------------
     */

    //adds an outcome to the database (in the outcome table)
    public void addOutcome(String title, String date, double amount, String category) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OUTCOME_COLUMN_TITLE, title);
        contentValues.put(OUTCOME_COLUMN_DATE, date);
        contentValues.put(OUTCOME_COLUMN_AMOUNT, amount);
        contentValues.put(OUTCOME_COLUMN_CATEGORY, category);
        db.insert(TABLE_OUTCOME, null, contentValues);
    }

    //gets the total outcome (in kr)
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

    //checks of an outcome exists based on its´ title
    public boolean outcomeTitleExists(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery( "SELECT * FROM " + TABLE_OUTCOME, null );
        if(cursor != null) {
            if(cursor.moveToFirst()) {
                do {
                    if(title.equals(cursor.getString(1))) {
                        return true;
                    }
                } while(cursor.moveToNext());
            }
        }
        cursor.close();
        return false;
    }

    /**
     * gets the values of all the items from the OutcomeTable and returns them in a list
     *
     * row 0 - id
     * row 1 - title
     * row 2 - date
     * row 3 - amount
     * row 4- category
     */
    public String[] getOutcomeValuesFromRowNbr(int indexInTable) {
        String rowValue = "";
        String[] rowValueList = new String[getNbrOfOutcomeTableRows()];
        int index = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_OUTCOME, new String[] {"*"}, null, null, null, null, null, null );
        if(cursor != null) {
            if(cursor.moveToFirst()) {
                do {
                    rowValue = cursor.getString(indexInTable);
                    //Log.d(null, "Row value at index " + index + " is: " + rowValue);
                    rowValueList[index] = rowValue;
                    index++;
                } while(cursor.moveToNext()) ;
            }
        }
        cursor.close();
        db.close();
        return rowValueList;
    }


    /**
     * Methods for printing contents of tables - used solely for testing purposes
     * -----------------------------------------------------------------------------
     */

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
