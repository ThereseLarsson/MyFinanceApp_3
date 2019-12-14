package com.example.thereselarsson.da401a_assignment_1_v2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * registers a new user
 */
public class CreateAccount extends AppCompatActivity {
    private EditText firstName;
    private EditText lastName;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        initiateComponents();
        registerListeners();
    }

    /**
     * initiates necessary components
     */
    private void initiateComponents() {
        firstName = findViewById(R.id.newAccount_firstName);
        lastName = findViewById(R.id.newAccount_lastName);
        btn = findViewById(R.id.newAccount_btn);
    }

    /**
     * adds listeners to components
     */
    private void registerListeners() {
        ClickListener clickListener = new ClickListener();
        btn.setOnClickListener(clickListener);
    }

    /**
     * shows the user a message when they fail to create a valid account
     */
    public void showMessage(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    /**
     * checks if the firstname and lastname are both filled, if so return true.
     * otherwise, return false
     */
    private boolean validNames() {
        //TODO: you should handle the user data using SharedPreferences (see lecture 5, slide 5-6)
        if(firstName.getText().toString().matches(".*[a-zA-Z]+.*") &&
                lastName.getText().toString().matches(".*[a-zA-Z]+.*")) {
            return true;
        }
        return false;
    }

    /**
     * inner class to handle clicks
     */
    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.newAccount_btn:
                    if(validNames()) {
                        //testcomment
                        //TODO: use SharedPreferences instead of database usage
                        Startup.db.addPerson(firstName.getText().toString(), lastName.getText().toString());
                        Startup.db.printTablePersonAsString(); //testing purpose
                        Startup.accountCreated = true;
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        showMessage("Please enter both a firstname and a lastname");
                    }
                    break;
            }
        }
    }
}
