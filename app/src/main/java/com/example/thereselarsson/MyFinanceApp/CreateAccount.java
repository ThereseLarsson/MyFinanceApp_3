package com.example.thereselarsson.MyFinanceApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Registers a new user
 */
public class CreateAccount extends AppCompatActivity {
    private EditText firstName;
    private EditText lastName;
    private Button btn;

    public CreateAccount() {
        //empty constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        initiateComponents();
        registerListeners();
    }

    private void initiateComponents() {
        firstName = findViewById(R.id.newAccount_firstName);
        lastName = findViewById(R.id.newAccount_lastName);
        btn = findViewById(R.id.newAccount_btn);
    }

    private void registerListeners() {
        ClickListener clickListener = new ClickListener();
        btn.setOnClickListener(clickListener);
    }

    private void showMessage(String message) {
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
        if(firstName.getText().toString().matches(".*[a-zA-Z]+.*") &&
                lastName.getText().toString().matches(".*[a-zA-Z]+.*")) {
            return true;
        }
        return false;
    }

    /**
     * Writes data in SharedPreferences
     * stores the userdata (i.e. firstname and lastname of the user)
     */
    protected void storeUserData() {
        String name = firstName.getText().toString() + " " + lastName.getText().toString();
        //Startup.sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE); //is in the class Startup now

        //creating an Editor object to edit (write to the file)
        SharedPreferences.Editor editor = Startup.sharedPreferences.edit();

        //storing the key and its value as the data fetched from edittext
        editor.putString("userName", name);

        //onde the changes have been made, we need to commit to apply those changes made,
        //otherwise, it will throw an error
        editor.commit();
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
                        storeUserData();
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
