package com.example.thereselarsson.da401a_assignment_1;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;


public class EnterTransactionFragment extends Fragment {
    private View rootView;
    private DialogFragment datePickerFragment;

    //elements to change when income/outcome is toggles
    private TextView headline;
    private Switch toggleBtn;
    private boolean isIncome; //if false --> =outcome

    //variables to get input from user
    private EditText titleTxt;
    private String title;
    private Button datePickerBtn;
    private EditText amountTxt;
    private double amount;
    private Spinner spinner;
    private String category;

    //interaction
    private Button confirmBtn;

    //variables to handle date picking
    private DateFormat formatter;
    private Date dateObject;
    private String date;


    public EnterTransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_enter_transaction, container, false);
        initiateComponents();
        registerListeners();
        setIncomeCategories();
        isIncome = true;
        date = "";
        return rootView;
    }

    public void initiateComponents() {
        headline = rootView.findViewById(R.id.enterTransaction_headline);
        titleTxt = rootView.findViewById(R.id.enterTransaction_title);
        datePickerBtn = rootView.findViewById(R.id.enterTransaction_datePicker);
        amountTxt = rootView.findViewById(R.id.enterTransaction_amount);
        spinner = rootView.findViewById(R.id.enterTransaction_category);
        confirmBtn = rootView.findViewById(R.id.enterTransaction_confirmBtn);
        toggleBtn = rootView.findViewById(R.id.enterTransaction_toggleBtn);
    }

    /**
     * adds listeners to components
     */
    private void registerListeners() {
        ClickListener clickListener = new ClickListener();
        datePickerBtn.setOnClickListener(clickListener);
        confirmBtn.setOnClickListener(clickListener);
        toggleBtn.setOnClickListener(clickListener);

        SpinnerActivity spinnerActivity = new SpinnerActivity();
        spinner.setOnItemSelectedListener(spinnerActivity);
    }

    public void setIncomeCategories() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainActivity.context,
                R.array.categories_income, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    public void setOutcomeCategories() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainActivity.context,
                R.array.categories_outcome, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void showDatePickerDialog() {
        datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    public String getDate() {
        /*formatter = new SimpleDateFormat("dd/MM/yyyy"); // Make sure user insert date into edittext in this format.
        String dateString = datePicker.getText().toString();
        try {
            dateObject = formatter.parse(dateString);
            dateDate = new SimpleDateFormat("dd/MM/yyyy").format(dateObject);

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        Toast.makeText(getActivity(), dateDate, Toast.LENGTH_LONG).show(); //testing
        return dateDate;*/
        return "";
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
        if(titleTxt.getText().toString().matches(".*[a-zA-Z]+.*") && !(date.equals("")) && !(amountTxt.getText().toString().equals(""))) {
            title = titleTxt.getText().toString();
            amount = Double.parseDouble(amountTxt.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private void addNewIncomeToDatabase() {
        Startup.db.addIncome(title, date, amount, category);
    }

    private void addNewOutcomeToDatabase() {
        Startup.db.addOutcome(title, date, amount, category);
    }

    /**
     * inner class to handle clicks
     */
    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.enterTransaction_datePicker:
                    showDatePickerDialog();
                    break;

                case R.id.enterTransaction_confirmBtn:
                    if(validData()) {
                        if(isIncome) {
                            showMessage("ALL DATA IS VALID!, INCOME");
                            //addNewIncomeToDatabase();
                        } else {
                            showMessage("ALL DATA IS VALID!, OUTCOME");
                            //addNewOutcomeToDatabase();
                        }
                    } else {
                        showMessage("Please enter all data above");
                    }
                    break;

                case R.id.enterTransaction_toggleBtn:
                    if(isIncome) {
                        headline.setText("Enter new outcome");
                        toggleBtn.setText("Toggle to enter new income instead");
                        setOutcomeCategories();
                        isIncome = false;
                    } else {
                        headline.setText("Enter new income");
                        toggleBtn.setText("Toggle to enter new outcome instead");
                        setIncomeCategories();
                        isIncome = true;
                    }
                    break;
            }
        }
    }

    /**
     * gets the selected category from the spinner
     */
    private class SpinnerActivity implements AdapterView.OnItemSelectedListener {
        // An item was selected
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            category = parent.getItemAtPosition(pos).toString();
        }

        public void onNothingSelected(AdapterView<?> parent) {
            if(isIncome) {
                category = "Salary"; //automatically select first income item
            } else {
                category = "Food"; ////automatically select first outcome item
            }
        }
    }
}
