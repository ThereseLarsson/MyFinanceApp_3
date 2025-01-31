package com.example.thereselarsson.MyFinanceApp_3;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//Greeting view before the user has chosen an option in the (vertical) navigation drawer
public class GreetingFragment extends Fragment {
    private View rootView;
    private TextView greetingName;
    private String name;

    public GreetingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_greeting, container, false);
        initiateComponents();
        return rootView;
    }

    public void initiateComponents() {
        name = Startup.sharedPreferences.getString("userName", "");
        greetingName = rootView.findViewById(R.id.greeting_name);
        greetingName.setText("Dear " + name);
    }
}
