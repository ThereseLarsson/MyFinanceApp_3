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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EnterIncomeFragment extends Fragment {
    private EditText title;
    private EditText date;
    private EditText amount;
    private Spinner category;
    private Button confirmBtn;
    private Switch toggleBtn;
    private View rootView;
    private DateFormat formatter;
    private Date dateObject;
    private String dateDate;

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

    public String getDate() {
        formatter = new SimpleDateFormat("dd/MM/yyyy"); // Make sure user insert date into edittext in this format.
        String dateString = date.getText().toString();
        try {
            dateObject = formatter.parse(dateString);
            dateDate = new SimpleDateFormat("dd/MM/yyyy").format(dateObject);

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        Toast.makeText(getActivity(), dateDate, Toast.LENGTH_LONG).show(); //testing
        return dateDate;
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
