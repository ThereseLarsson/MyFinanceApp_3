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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateComponents();
        registerListeners();
        //CHECK IF USER HAS USED APPLICATION BEFORE, IF SO --> CHANGE NAME IN GREETING MESSAGE
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
     * shows the user a message when they fail to create a valid account
     */
    public void showMessage(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    /**
     * checks if the user has used the application before
     */
    public void checkName() {

    }

    /**
     * changes the greeting message
     */
    public void changeGreeting(String string) {
        greeting.setText(string);
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
                        Intent intent = new Intent(getApplicationContext(), NEXT_ACTIVITY.class);
                        startActivity(intent);
                    } else {
                        showMessage("Please enter both firstname and a lastname");
                    }
                    break;
            }
        }
    }
}
