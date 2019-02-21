package com.example.thereselarsson.da401a_assignment_1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;


public class EnterIncomeFragment extends Fragment {
    private EditText title;
    private EditText date;
    private EditText amount;
    private Spinner category;
    private Button confirmBtn;
    private Switch toggleBtn;
    private View rootView;

    public EnterIncomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_enter_income, container, false);
        return rootView;
    }

    public void initiateComponents() {
        title = rootView.findViewById(R.id.enterIncome_title);
        date = rootView.findViewById(R.id.enterIncome_date);
        amount = rootView.findViewById(R.id.enterIncome_amount);
        category = rootView.findViewById(R.id.enterIncome_category);
        confirmBtn = rootView.findViewById(R.id.enterIncome_confirmBtn);
        toggleBtn = rootView.findViewById(R.id.enterIncome_toggleBtn);
    }

    /**
     * adds listeners to components
     */
    private void registerListeners() {
        ClickListener clickListener = new ClickListener();
        confirmBtn.setOnClickListener(clickListener);
        toggleBtn.setOnClickListener(clickListener);
    }

    public void provideCategories() {

    }

    public void getDate() {

    }

    /**
     * shows the user a message when they fail to create a valid account
     */
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * checks if the required data fields are entered, if so return true.
     * Otherwise, return false
     * @return
     */
    private boolean validData() {

        return false;
    }

    /**
     * inner class to handle clicks
     */
    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.enterIncome_confirmBtn:
                    if(validData()) {
                        //lägg till inkomst i databas
                    } else {
                        showMessage("Please enter all data above");
                    }
                    break;

                case R.id.enterIncome_toggleBtn:
                    //toggle till enterOutcome fragment
                    break;
            }
        }
    }

}
