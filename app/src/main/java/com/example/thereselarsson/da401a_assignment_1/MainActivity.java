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
    private TextView greeting;
    private EditText firstName;
    private EditText lastName;
    private Button btn;
    private static Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateComponents();
        registerListeners();
        //updateGreetingMessage();
    }

    /**
     * initiates necessary components
     */
    private void initiateComponents() {
        greeting = findViewById(R.id.start_headline);
        firstName = findViewById(R.id.start_firstName);
        lastName = findViewById(R.id.start_lastName);
        btn = findViewById(R.id.start_btn);
    }

    /**
     * adds listeners to components
     */
    private void registerListeners() {
        ClickListener clickListener = new ClickListener();
        btn.setOnClickListener(clickListener);
    }

    public void updateGreetingMessage() {
        if(userExists()) {
            //setGreetingMessage();
        } else {
            //setGreetingMessage();
        }
    }

    /**
     * checks if the user has used the application before
     */
    public boolean userExists() {
        return db.personExists();
    }

    /**
     * changes the greeting message
     */
    public void setGreetingMessage(String string) {
        greeting.setText(string);
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
     * Otherwise, return false
     * @return
     */
    private boolean validNames() {
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
                case R.id.start_btn:
                    if(validNames()) {
                        db.addPerson(firstName.getText().toString(), lastName.getText().toString());
                        Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                        startActivity(intent);
                    } else {
                        showMessage("Please enter both a firstname and a lastname");
                    }
                    break;
            }
        }
    }
}
