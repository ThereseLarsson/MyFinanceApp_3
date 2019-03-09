package com.example.thereselarsson.da401a_assignment_1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * the first thing that loads upon app launch.
 * if a account is already created --> go to main menu / MainActivity
 * otherwise, --> go to Create new account activity
 */
public class Startup extends AppCompatActivity {
    Intent intent;
    public static Database db;
    public static boolean accountCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        initiateDatabase(getApplicationContext());

        if(db.userExists()) {
            accountCreated = true;
        } else {
            accountCreated = false;
        }

        if(accountCreated) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        } else {
            intent = new Intent(getApplicationContext(), CreateAccount.class);
            startActivity(intent);
        }
    }

    /**
     * initiates and returns the database
     * @param context
     * @return
     */
    public static Database initiateDatabase(Context context){
        if(db == null) {
            db = new Database(context);
        }
        return db;
    }
}
