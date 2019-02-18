package com.example.thereselarsson.da401a_assignment_1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static Database db;
    public static boolean accountCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createDatabase(getApplicationContext());

        if(accountCreated) {
            //"logga in" användaren
        } else {
            Intent intent = new Intent(getApplicationContext(), CreateAccount.class);
            startActivity(intent);
        }
    }

    public static Database createDatabase(Context context){
        if(db == null) {
            db = new Database(context);
        }
        return db;
    }

    /**
     * checks if the user has used the application before
     */
    public boolean userExists() {
        return db.personExists();
    }
}
