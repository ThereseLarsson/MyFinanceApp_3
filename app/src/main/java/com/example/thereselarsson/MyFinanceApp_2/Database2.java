package com.example.thereselarsson.MyFinanceApp_2;

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
public class Database2 extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "a1Database";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;

    //queries
    private final String selectIncomeQuery = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TYPE + " LIKE '%income%'";
    private final String selectOutcomeQuery = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TYPE + " LIKE '%outcome%'";
    //private final String selectOutcomeQuery = "SELECT * FROM transactions WHERE type LIKE '%outcome%'";

    //table + its columns
    private static final String TABLE_TRANSACTIONS = "transactions";
    private static final String COLUMN_ID = "id"; //unique title, therefore not utilized as of now
    private static final String COLUMN_TYPE = "type"; //income or outcome
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_CATEGORY = "category";

    public Database2(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase(); //ta bort sen? behövs denna rad?
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        onCreate(db); //recreates table(s)
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_TRANSACTIONS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_AMOUNT + " DOUBLE, " +
                COLUMN_CATEGORY + " TEXT)"
        );
        Log.d(null, "DATABASE_CREATED");
    }

    //adds a transaction to the database
    public void addTransaction(String type, String title, String date, double amount, String category) {
        db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TYPE, type);
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_AMOUNT, amount);
        contentValues.put(COLUMN_CATEGORY, category);
        db.insert(TABLE_TRANSACTIONS, null, contentValues);
    }

    //gets the total income/outcome (in kr)
    public double getSum(String transactionType) {
        double sum = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;

        if(transactionType.equals("income")) {
            cursor = db.rawQuery(selectIncomeQuery, null);

        } else if(transactionType.equals("outcome")) {
            cursor = db.rawQuery(selectOutcomeQuery, null);

        } else {
            Log.d(null, "error: invalid transaction type");
            cursor = null;
        }

        if(cursor != null) {
            if(cursor.moveToFirst()) {
                do {
                    sum = sum + cursor.getDouble(4);
                } while(cursor.moveToNext());
            }
            cursor.close();
        }

        //Log.d(null, "NUMBER_OF " + transactionType + ": " + getNbrOfTransactions(transactionType));
        return sum;
    }

    //checks if an transaction exists based on its´ title
    public boolean titleExists(String title, String transactionType) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;

        if(transactionType.equals("income")) {
            cursor = db.rawQuery(selectIncomeQuery, null);

        } else if(transactionType.equals("outcome")) {
            cursor = db.rawQuery(selectOutcomeQuery, null);

        } else {
            Log.d(null, "error: invalid transaction type");
            cursor = null;
        }

        if(cursor != null) {
            if(cursor.moveToFirst()) {
                do {
                    if(title.equals(cursor.getString(2))) {
                        return true;
                    }
                } while(cursor.moveToNext());
            }
            cursor.close();
        }
        return false;
    }

    //returns the total number of income/outcome items in the table
    public int getNbrOfTransactions(String transactionType) {
        int nbrOfTransactions = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;

        if(transactionType.equals("income")) {
            cursor = db.rawQuery(selectIncomeQuery, null);

        } else if(transactionType.equals("outcome")) {
            cursor = db.rawQuery(selectOutcomeQuery, null);

        } else {
            Log.d(null, "error: invalid transaction type");
            cursor = null;
        }

        if(cursor != null) {
            if(cursor.moveToFirst()) {
                do {
                    nbrOfTransactions++;
                } while(cursor.moveToNext());
            }
            cursor.close();
        }
        return nbrOfTransactions;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////// NOT FIXED YET ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * gets the values of all the items from the IncomeTable and returns them in a list
     *
     * row 0 - id
     * row 1 - transactionType
     * row 2 - title
     * row 3 - date
     * row 4 - amount
     * row 5- category
     */
    public String[] getIncomeValuesFromRowNbr(int indexInTable) {
/*        String rowValue = "";
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
        return rowValueList;*/

        return null;
    }

    /**
     * gets the values of all the items from the OutcomeTable and returns them in a list
     *
     * row 0 - id
     * row 1 - transactionType
     * row 2 - title
     * row 3 - date
     * row 4 - amount
     * row 5- category
     */
    public String[] getOutcomeValuesFromRowNbr(int indexInTable) {
/*        String rowValue = "";
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
        return rowValueList;*/

        return null;
    }

    public String[] getTransactionItemsFromColumnNbr(String type, int indexInTable) {
        /*String currentColumnValue = "";
        String[] columnItems = new String[getNbrOfTransactions(type)];
        int index = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_INCOME, new String[] {"*"}, null, null, null, null, null, null );

        if(cursor != null) {
            if(cursor.moveToFirst()) {
                do {
                    currentColumnValue = cursor.getString(indexInTable);
                    //Log.d(null, "column value at index " + index + " is: " + currentColumnValue);
                    columnItems[index] = currentColumnValue;
                    index++;
                } while(cursor.moveToNext()) ;
            }
        }
        cursor.close();
        db.close();
        return columnItems;*/

        return null;
    }

    /**
     * prints the contents of the table
     * used for testing purposes (to see that the data is stored correctly)
     */
    public void printTableAsString() {
        SQLiteDatabase dbHandler = this.getReadableDatabase();
        String tableString = String.format("TABLE %s:\n", TABLE_TRANSACTIONS);
        Cursor allRows  = dbHandler.rawQuery("SELECT * FROM " + TABLE_TRANSACTIONS, null);
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
