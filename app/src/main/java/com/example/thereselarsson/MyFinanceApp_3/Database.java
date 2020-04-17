package com.example.thereselarsson.MyFinanceApp_3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//Stores data (income and outcome transactions) entered by the user
public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "a1Database";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;
    private Cursor cursor;

    //queries
    private final String selectIncomeQuery = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TYPE + " LIKE '%income%'";
    private final String selectOutcomeQuery = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TYPE + " LIKE '%outcome%'";

    //table + its columns
    private static final String TABLE_TRANSACTIONS = "transactions";
    private static final String COLUMN_ID = "id"; //unique title, therefore not utilized as of now
    private static final String COLUMN_TYPE = "type"; //income or outcome
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_CATEGORY = "category";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
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
        Log.d(null, "DATABASE_CREATED"); //testing purpose
    }

    private void initializeCursor(String transactionType) {
        db = getReadableDatabase();

        if(transactionType.equals("income")) {
            cursor = db.rawQuery(selectIncomeQuery, null);

        } else if(transactionType.equals("outcome")) {
            cursor = db.rawQuery(selectOutcomeQuery, null);

        } else {
            Log.d(null, "error: invalid transaction type");
            cursor = null;
        }
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
        //printTableAsString(); //testing purpose
    }

    //gets the total income/outcome (in kr)
    public double getSum(String transactionType) {
        double sum = 0;
        initializeCursor(transactionType);

        if(cursor != null) {
            if(cursor.moveToFirst()) {
                do {
                    sum = sum + cursor.getDouble(4);
                } while(cursor.moveToNext());
            }
            cursor.close();
        }
        return sum;
    }

    //checks if an transaction exists based on its´ title
    public boolean titleExists(String title, String transactionType) {
        initializeCursor(transactionType);

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
    private int getNbrOfTransactions(String transactionType) {
        int nbrOfTransactions = 0;
        initializeCursor(transactionType);

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

    /**
     * retrieves all the items of one column (income or outcome) and returns them in a list
     * row 0 - id
     * row 1 - transactionType
     * row 2 - title
     * row 3 - date
     * row 4 - amount
     * row 5- category
     */
    public String[] getTransactionItemsFromColumnNbr(String transactionType, int indexInTable) {
        String currentColumnValue = "";
        String[] columnItems = new String[getNbrOfTransactions(transactionType)];
        int index = 0;

        initializeCursor(transactionType);

        if(cursor != null) {
            if(cursor.moveToFirst()) {
                do {
                    currentColumnValue = cursor.getString(indexInTable);
                    //Log.d(null, "COLUMN_VALUE_AT_INDEX " + index + " IS: " + currentColumnValue); //testing purpose
                    columnItems[index] = currentColumnValue;
                    index++;
                } while(cursor.moveToNext());
            }
            cursor.close();
        }
        return columnItems;
    }

    /**
     * prints the contents of the table
     * used for testing purposes (to see that the data is stored correctly)
     */
    private void printTableAsString() {
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
