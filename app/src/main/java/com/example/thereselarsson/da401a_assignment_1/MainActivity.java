package com.example.thereselarsson.da401a_assignment_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

//Main Menu in the app
public class MainActivity extends AppCompatActivity {
    private TextView greeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateComponents();
        updatePersonalGreeting();
    }

    public void initiateComponents() {
        greeting = findViewById(R.id.main_greeting_1);
    }

    public void updatePersonalGreeting() {
        String name = Startup.db.getPersonName();
        greeting.setText("Welcome " + name + "!");
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}
