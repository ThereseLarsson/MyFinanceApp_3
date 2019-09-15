package com.example.thereselarsson.da401a_assignment_1;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ViewTransactionFragment extends Fragment implements DatePickerFragment.Listener {
    private View rootView;
    private ListView listView;
    private boolean isIncome; //if false --> = outcome
    private ArrayList<Item> itemArrayList;
    private CustomListAdapter customListAdapter;
    private Item item;
    private ArrayList<Item> items;
    //private DialogFragment datePickerFragment;
    private String date;

    //variables for storing data from database
    private String[] income_itemTitleList = {}; //for storing the each items title
    private String[] outcome_itemTitleList = {};
    private String[] income_itemDateList = {}; //for storing the each items date
    private String[] outcome_itemDateList = {};
    private String[] income_itemAmountList = {}; //for storing the each items amount (price)
    private String[] outcome_itemAmountList = {};
    private String[] income_itemCategoryList = {}; //for storing the each items category
    private String[] outcome_itemCategoryList = {};

    //variables for handling icons
    private int[] income_itemIconList = {}; //for storing the each items icon
    private int[] outcome_itemIconList = {};

    //variables for UI
    private TextView headline;
    private Switch toggleBtn;
    private Button filterDateBtn;
    private Button resetDateBtn;


    public ViewTransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_view_transaction, container, false);
        initiateComponents();
        registerListeners();
        isIncome = true;

        //set up the custom list adapter view
        initiateCustomListAdapter();

        return rootView;
    }

    public void initiateComponents() {
        headline = rootView.findViewById(R.id.viewTransaction_headline);
        toggleBtn = rootView.findViewById(R.id.viewTransaction_toggleBtn);
        filterDateBtn = rootView.findViewById(R.id.viewTransaction_filterDateBtn);
        resetDateBtn = rootView.findViewById(R.id.viewTransaction_resetDateBtn);
    }

    /**
     * adds listeners to components
     */
    private void registerListeners() {
        ClickListener clickListener = new ClickListener();
        toggleBtn.setOnClickListener(clickListener);
        filterDateBtn.setOnClickListener(clickListener);
        resetDateBtn.setOnClickListener(clickListener);
    }

    /**
     * methods for creating custom list adapter
     * ---------------------------------------------------------------------------------------
     */
    public void initiateCustomListAdapter() {
        itemArrayList = generateItemsList();
        customListAdapter = new CustomListAdapter(MainActivity.context, itemArrayList);
        listView = rootView.findViewById(R.id.viewTransaction_list);
        listView.setAdapter(customListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //starta DetailActivity + skicka med värden
                Log.d(null, "CLICKED row number: " + arg2);
            }
        });
    }

    public ArrayList<Item> generateItemsList() {
        items = new ArrayList<Item>();

        /*
        row 0 - id
        row 1 - title
        row 2 - date
        row 3 - amount
        row 4- category
         */
        income_itemTitleList = Startup.db.getIncomeValuesFromRowNbr(1);
        outcome_itemTitleList = Startup.db.getOutcomeValuesFromRowNbr(1); //is created here but not used as income is the default
        income_itemCategoryList = Startup.db.getIncomeValuesFromRowNbr(4);
        outcome_itemCategoryList = Startup.db.getOutcomeValuesFromRowNbr(4); //is created here but not used as income is the default

        //not used here but need to be created
        income_itemDateList = Startup.db.getIncomeValuesFromRowNbr(2);
        outcome_itemDateList = Startup.db.getOutcomeValuesFromRowNbr(2); //is created here but not used as income is the default
        income_itemAmountList = Startup.db.getIncomeValuesFromRowNbr(3);
        outcome_itemAmountList = Startup.db.getOutcomeValuesFromRowNbr(3); //is created here but not used as income is the default

        income_itemIconList = new int[] {R.drawable.icon_salary, R.drawable.icon_other};
        outcome_itemIconList = new int[] {R.drawable.icon_food, R.drawable.icon_sparetime, R.drawable.icon_travel, R.drawable.icon_acc, R.drawable.icon_other, R.drawable.icon_salary}; //is created here but not used as income is the default

        //shows the items from income as default when the list is generated into the interface for the first time
        for(int i = 0; i < income_itemTitleList.length; i++) { //längden på listan är ekvivalent med antalet items
            if(income_itemCategoryList[i].equals("Salary")) {
                item = new Item(income_itemIconList[0], income_itemTitleList[i]);
            } else {
                item = new Item(income_itemIconList[1], income_itemTitleList[i]);
            }
            items.add(item);
        }

        return items;
    }

    /**
     * methods for changing the content of the list view
     * ---------------------------------------------------------------------------------------
     */
    public void setItemListContentToIncome() {
        items = new ArrayList<Item>();

        for(int i = 0; i < income_itemTitleList.length; i++) { //längden på listan är ekvivalent med antalet items
            if(income_itemCategoryList[i].equals("Salary")) {
                item = new Item(income_itemIconList[0], income_itemTitleList[i]);
            } else {
                item = new Item(income_itemIconList[1], income_itemTitleList[i]);
            }
            items.add(item);
        }

        customListAdapter = new CustomListAdapter(MainActivity.context, items);
        listView.setAdapter(customListAdapter);
    }

    public void setItemListContentToOutcome() {
        items = new ArrayList<Item>();

        for(int i = 0; i < outcome_itemTitleList.length; i++) { //längden på listan är ekvivalent med antalet items
            if(outcome_itemCategoryList[i].equals("Food")) {
                item = new Item(outcome_itemIconList[0], outcome_itemTitleList[i]);
            } else if (outcome_itemCategoryList[i].equals("Leisure")) {
                item = new Item(outcome_itemIconList[1], outcome_itemTitleList[i]);
            } else if (outcome_itemCategoryList[i].equals("Travel")) {
                item = new Item(outcome_itemIconList[2], outcome_itemTitleList[i]);
            } else if (outcome_itemCategoryList[i].equals("Accommodation")) {
                item = new Item(outcome_itemIconList[3], outcome_itemTitleList[i]);
            } else {
                item = new Item(outcome_itemIconList[4], outcome_itemTitleList[i]);
            }
            items.add(item);
        }

        customListAdapter = new CustomListAdapter(MainActivity.context, items);
        listView.setAdapter(customListAdapter);
    }

    /**
     * Methods for handling date picking
     * --------------------------------------------------------------------------------------
     */
    @Override
    public void returnDate(String date) {
        this.date = date;
        setDate(date);
        if(isIncome) {
            setHeadlineText("Income from " + date);
        } else {
            setHeadlineText("Outcome from " + date);
        }
    }

    public void showDatePickerDialog() {
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.onDateSetListener(this);
        datePicker.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    public void setDate(String string) {
        date = string;
    }

    public void setHeadlineText(String string) {
        headline.setText(string);
    }

    /**
     * methods for resetting list view
     * --------------------------------------------------------------------------------------
     */
    public void showAllIncome() {
        //TODO: visa alla inkomster
        setHeadlineText("All income");
    }

    public void showAllOutcome() {
        //TODO: visa alla utgifter
        setHeadlineText("All outcome");
    }

    /**
     * inner class to handle clicks
     * --------------------------------------------------------------------------------------
     */
    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.viewTransaction_toggleBtn:
                    if(isIncome) {
                        headline.setText("All outcome");
                        toggleBtn.setText("Toggle to show income instead");
                        setItemListContentToOutcome();
                        isIncome = false;
                    } else {
                        headline.setText("All income");
                        toggleBtn.setText("Toggle to show outcome instead");
                        setItemListContentToIncome();
                        isIncome = true;
                    }
                    break;

                case R.id.viewTransaction_filterDateBtn:
                    if(isIncome) {
                        Log.d(null, "FILTERDATEBUTTON IS PRESSED");
                        //TODO: filter from date --> show list of outcome items from table in database
                        showDatePickerDialog();
                    } else {
                        Log.d(null, "FILTERDATEBUTTON IS PRESSED");
                        //TODO: filter from date --> show list of outcome items from table in database
                        showDatePickerDialog();
                    }
                    break;

                case R.id.viewTransaction_resetDateBtn:
                    if(isIncome) {
                        Log.d(null, "RESETBUTTON IS PRESSED");
                        showAllIncome();
                    } else {
                        Log.d(null, "RESETBUTTON IS PRESSED");
                        showAllOutcome();
                    }
                    break;
            }
        }
    }
}
