package com.example.thereselarsson.da401a_assignment_1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    public static Database db;
    public static boolean accountCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateDatabase(getApplicationContext());

        if(accountCreated) {
            //Intent intent = new Intent(getApplicationContext(), "LOGIN".class);
            //startActivity(intent);

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
