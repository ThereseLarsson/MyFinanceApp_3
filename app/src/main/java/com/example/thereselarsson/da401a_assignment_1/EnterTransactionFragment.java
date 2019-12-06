package com.example.thereselarsson.da401a_assignment_1;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**
 * Provides the function to add a transaction (income or outcome)
 */
public class EnterTransactionFragment extends Fragment implements DatePickerFragment.Listener {
    private View rootView;
    //private DialogFragment datePickerFragment;

    //UI-components and variables to change when income/outcome is toggled
    private TextView headline;
    private Switch toggleBtn;
    private boolean isIncome; //if false --> = outcome

    //UI-components to get input from user
    private EditText titleTxt;
    private Button datePickerBtn;
    private EditText amountTxt;
    private Spinner spinner;

    //variables to save input from corresponding UI-components
    private String title;
    private String date;
    private double amount;
    private String category;

    //interaction
    private Button confirmBtn;


    public EnterTransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_enter_transaction, container, false);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title", titleTxt.getText().toString());
        outState.putString("date", datePickerBtn.getText().toString());
        outState.putString("amount", amountTxt.getText().toString());
        outState.putString("category", category);

        outState.putString("headline", headline.getText().toString());
        outState.putBoolean("isIncome", isIncome);
        outState.putString("toggleBtn", toggleBtn.getText().toString());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initiateComponents();
        registerListeners();

        if(savedInstanceState == null) { //första gången som detta fragment laddas
            isIncome = true;
            date = "";

        } else { //probably orientation change
            titleTxt.setText(savedInstanceState.getString("title"));
            datePickerBtn.setText(savedInstanceState.getString("date"));
            date = savedInstanceState.getString("date");
            amountTxt.setText(savedInstanceState.getString("amount"));
            category = savedInstanceState.getString("category");

            headline.setText(savedInstanceState.getString("headline"));
            isIncome = savedInstanceState.getBoolean("isIncome");
            toggleBtn.setText(savedInstanceState.getString("toggleBtn"));
        }

        if(isIncome) {
            setIncomeCategories();
        } else {
            setOutcomeCategories();
        }

        toggleBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
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
            }
        });
    }

    /**
     * initiates the necessary components
     */
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

    /**
     * shows the user a message when they fail to create a valid account
     */
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Methods for setting up categories
     * -----------------------------------------------------------------------------------------------
     */
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

    /**
     * checks if the required data fields are entered, if so return true.
     * Otherwise, return false
     * @return
     * --------------------------------------------------------------------------------------------
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

    //TODO: remove this method? The title might not need to be unique
    /**
     * checks if the entered title (of the item) of the transaction is unique
     * if not, return false
     */
    private boolean uniqueTitle() {
        title = titleTxt.getText().toString();
        if(isIncome) {
            if(Startup.db.incomeTitleExists(title)) {
                return false;
            } else {
                return true;
            }
        } else {
            if(Startup.db.outcomeTitleExists(title)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * Methods that connects to the database
     * -----------------------------------------------------------------------------------------
     */
    private void addNewIncomeToDatabase() {
        Startup.db.addIncome(title, date, amount, category);
    }

    private void addNewOutcomeToDatabase() {
        Startup.db.addOutcome(title, date, amount, category);
    }


    /**
     * Methods for handling date picking
     * -----------------------------------------------------------------------------------------
     */

    @Override
    public void returnDate(String date) {
        setDate(date);
        setDateButtonText(date);
    }

    public void showDatePickerDialog() {
        //datePickerFragment = new DatePickerFragment2();
        //datePickerFragment.show(getFragmentManager(), "datePicker");

        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.onDateSetListener(this);
        datePicker.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    public void setDate(String string) {
        this.date = string;
        Log.d(null, "DATE ÄR: " + date);
    }

    public void setDateButtonText(String string) {
        datePickerBtn.setText(string);
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

    /**
     * Inner class that handle events from the user
     * ---------------------------------------------------------------------------------------------
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
                        if(uniqueTitle()) {
                            if(isIncome) {
                                addNewIncomeToDatabase();
                                showMessage("Income successfully added!");
                                Startup.db.printTableIncomeAsString(); //testing purpose
                            } else {
                                addNewOutcomeToDatabase();
                                showMessage("Outcome successfully added!");
                                Startup.db.printTableOutcomeAsString(); //testing purpose
                            }
                        } else {
                            showMessage("Please enter a unique title");
                        }

                    } else {
                        showMessage("Please enter all data above");
                    }
                    break;

                /**case R.id.enterTransaction_toggleBtn:
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
                    break; */
            }
        }
    }
}
