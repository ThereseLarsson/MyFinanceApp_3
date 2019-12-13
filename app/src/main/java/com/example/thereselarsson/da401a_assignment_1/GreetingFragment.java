package com.example.thereselarsson.da401a_assignment_1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * the greeting page that is displayed before the user has
 * chosen an option in the navigation drawer
 */
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

    /**
     * initiates the necessary components
     */
    public void initiateComponents() {
        name = Startup.db.getPersonName();
        greetingName = rootView.findViewById(R.id.greeting_name);
        greetingName.setText("Dear " + name);
    }
}
