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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class ViewTransactionFragment extends Fragment implements DatePickerFragment.Listener {
    private View rootView;
    private ListView listView;
    private boolean isIncome; //if false --> = outcome
    private ArrayList<Item> itemArrayList;
    private CustomListAdapter customListAdapter;
    //private Item item;
    private ArrayList<Item> incomeItems;
    private ArrayList<Item> outcomeItems;
    private ArrayList<Item> filteredIncomeItems; //used to store item-objects filtered after a specific date
    private ArrayList<Item> filteredOutcomeItems;
    private String date;

    //variables for storing data from database
    private final int[] income_itemIconList = new int[] {R.drawable.icon_salary, R.drawable.icon_other}; //for storing the each items icon
    private final int[] outcome_itemIconList = new int[] {R.drawable.icon_food, R.drawable.icon_sparetime, R.drawable.icon_travel, R.drawable.icon_acc, R.drawable.icon_other, R.drawable.icon_salary};
    private String[] income_itemTitleList = {}; //for storing the each items title
    private String[] outcome_itemTitleList = {};
    private String[] income_itemDateList = {}; //for storing the each items date
    private String[] outcome_itemDateList = {};
    private int[] income_itemAmountList; //for storing the each items amount (price)
    private int[] outcome_itemAmountList;
    private String[] income_itemCategoryList = {}; //for storing the each items category
    private String[] outcome_itemCategoryList = {};

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

    //shows the items from income as default when the list is generated into the interface for the first time
    public ArrayList<Item> generateItemsList() {
        incomeItems = new ArrayList<Item>(); //income used as default
        outcomeItems = new ArrayList<Item>();
        fetchAllIncomeFromDatabase();
        fetchAllOutcomeFromDatabase();

        return incomeItems;
    }

    /**
     * methods that retrieves the data (item title, date, amount an category) from the database
     * and places it in lists.
     * Two methods from fetching data from the income-table and out-come table separately
     *
     * row 0 - id
     * row 1 - title
     * row 2 - date
     * row 3 - amount
     * row 4- category
     * --------------------------------------------------------------------------------------------
     */
    public void fetchAllIncomeFromDatabase() {
        //retrieves the data from the database
        income_itemTitleList = Startup.db.getIncomeValuesFromRowNbr(1);
        income_itemDateList = Startup.db.getIncomeValuesFromRowNbr(2);

        //amount-value needs to be converted from String to int
        String[] temp = Startup.db.getIncomeValuesFromRowNbr(3);
        income_itemAmountList = new int[temp.length];
        for(int i = 0; i < temp.length; i++) {
            income_itemAmountList[i] = Integer.parseInt(temp[i]);
        }
        income_itemCategoryList = Startup.db.getIncomeValuesFromRowNbr(4);

        createIncomeItemObjects();
        sortItemList(incomeItems);
    }

    public void fetchAllOutcomeFromDatabase() {
        //retrieves the data from the database
        outcome_itemTitleList = Startup.db.getOutcomeValuesFromRowNbr(1);
        outcome_itemDateList = Startup.db.getOutcomeValuesFromRowNbr(2);

        //amount-value needs to be converted from String to int
        String[] temp = Startup.db.getOutcomeValuesFromRowNbr(3);
        outcome_itemAmountList = new int[temp.length];
        for(int i = 0; i < temp.length; i++) {
            outcome_itemAmountList[i] = Integer.parseInt(temp[i]);
        }
        outcome_itemCategoryList = Startup.db.getOutcomeValuesFromRowNbr(4);

        //creates a Item-ArrayList of the outcome-values above
        createOutcomeItemObjects();
        //sortes the lists
        sortItemList(outcomeItems);
    }

    /**
     * methods that converts the lists (icon, title, date, amount and category) to a single list of Item-objects
     * -------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */

    //creates a list of Item-objects from the income data
    public void createIncomeItemObjects() {
        Item item;
        incomeItems = new ArrayList<Item>();

        for(int i = 0; i < income_itemTitleList.length; i++) {
            if(income_itemCategoryList[i].equals("Salary")) {
                item = new Item(income_itemIconList[0], income_itemTitleList[i], income_itemDateList[i], income_itemAmountList[i], income_itemCategoryList[i]);
            } else {
                item = new Item(income_itemIconList[1], income_itemTitleList[i], income_itemDateList[i], income_itemAmountList[i], income_itemCategoryList[i]);
            }
            incomeItems.add(item);
        }
    }

    //creates a list of Item-objects from the outcome data
    public void createOutcomeItemObjects() {
        Item item;
        outcomeItems = new ArrayList<Item>();

        for(int i = 0; i < outcome_itemTitleList.length; i++) { //längden på listan är ekvivalent med antalet items
            if(outcome_itemCategoryList[i].equals("Food")) {
                item = new Item(outcome_itemIconList[0], outcome_itemTitleList[i], outcome_itemDateList[i], outcome_itemAmountList[i], outcome_itemCategoryList[i]);
            } else if (outcome_itemCategoryList[i].equals("Leisure")) {
                item = new Item(outcome_itemIconList[1], outcome_itemTitleList[i], outcome_itemDateList[i], outcome_itemAmountList[i], outcome_itemCategoryList[i]);
            } else if (outcome_itemCategoryList[i].equals("Travel")) {
                item = new Item(outcome_itemIconList[2], outcome_itemTitleList[i], outcome_itemDateList[i], outcome_itemAmountList[i], outcome_itemCategoryList[i]);
            } else if (outcome_itemCategoryList[i].equals("Accommodation")) {
                item = new Item(outcome_itemIconList[3], outcome_itemTitleList[i], outcome_itemDateList[i], outcome_itemAmountList[i], outcome_itemCategoryList[i]);
            } else {
                item = new Item(outcome_itemIconList[4], outcome_itemTitleList[i], outcome_itemDateList[i], outcome_itemAmountList[i], outcome_itemCategoryList[i]);
            }
            outcomeItems.add(item);
        }
    }

    /**
     * methods for changing the content of the list view
     * ---------------------------------------------------------------------------------------------------------------------------------
     */
    public void setItemListContentToIncome() {
        customListAdapter = new CustomListAdapter(MainActivity.context, incomeItems);
        listView.setAdapter(customListAdapter);
    }

    public void setItemListContentToOutcome() {
        customListAdapter = new CustomListAdapter(MainActivity.context, outcomeItems);
        listView.setAdapter(customListAdapter);
    }

    /**
     * Methods for handling date picking
     * --------------------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public void returnDate(String date) {
        this.date = date;
        setDate(date);
        if(isIncome) {
            //TODO: visa income från och med date
            setHeadlineText("Income from " + date);
            filteredIncomeItems = filterItemsAfterDate(incomeItems, date); //filtrera item-listan: välj bort de items som är innan valt datum
            //uppdatera listvyn med setItemListContentToIncome();
        } else {
            //TODO: visa outcome från och med date
            setHeadlineText("Outcome from " + date);
            filteredOutcomeItems = filterItemsAfterDate(outcomeItems, date); //filtrera item-listan: välj bort de items som är innan valt datum
            //uppdatera listvyn med setItemListContentToIncome();
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
     * sortes the dates so they are in order
     * dates are given on the format DD/MM/YYYY
     */
    public void sortItemList(ArrayList<Item> itemList) {
        Collections.sort(itemList, new Comparator<Item>() {
            @Override
            public int compare(Item i1, Item i2) {
                return Integer.valueOf(i2.getDate().compareTo(i1.getDate()));
            }
        });

        /*//sorterar outcome-listan
        Collections.sort(outcomeItems, new Comparator<Item>() {
            @Override
            public int compare(Item oi1, Item oi2) {
                return Integer.valueOf(oi2.getDate().compareTo(oi1.getDate()));
            }
        });*/
    }

    /**
     * removes dates that occurs before a given start date
     * @param itemList
     * @param startDate
     */
    public ArrayList<Item> filterItemsAfterDate(ArrayList<Item> itemList, String startDate) {
        ArrayList<Item> newList = new ArrayList<Item>();
        int lastIndex = -1; //hitta det index som startdatum ligger på

        //iterera genom / hitta index direkt för startDate, i (redan sorterad) income/outcome lista efter startDate, behåll items som kommer efter startDate
        for(int i = 0; i < lastIndex; i++) {
            newList.add(itemList.get(i));
        }
        return newList;
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
                        showDatePickerDialog();
                    } else {
                        showDatePickerDialog();
                    }
                    break;

                case R.id.viewTransaction_resetDateBtn:
                    if(isIncome) {
                        fetchAllIncomeFromDatabase();
                        setItemListContentToIncome();
                        setHeadlineText("All income");
                    } else {
                        fetchAllOutcomeFromDatabase();
                        setItemListContentToOutcome();
                        setHeadlineText("All outcome");
                    }
                    break;
            }
        }
    }
}
