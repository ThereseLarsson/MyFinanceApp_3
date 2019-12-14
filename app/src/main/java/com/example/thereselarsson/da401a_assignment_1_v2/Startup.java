package com.example.thereselarsson.da401a_assignment_1_v2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * the first thing that loads upon app launch.
 * if an account is already created --> go to main menu / MainActivity
 * otherwise, --> go to Create (new) account activity
 */
public class Startup extends AppCompatActivity {
    Intent intent;
    public static Database db;
    public static boolean accountCreated;
    protected static SharedPreferences sharedPreferences;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        initiateDatabase(getApplicationContext());
        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);

        if(sharedPreferences.getString("userName", "").equals("")) {
            accountCreated = false;
        } else {
            accountCreated = true;
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
     */
    public static Database initiateDatabase(Context context){
        if(db == null) {
            db = new Database(context);
        }
        return db;
    }
}
