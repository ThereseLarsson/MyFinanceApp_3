package com.example.thereselarsson.MyFinanceApp_2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * backup of the class ViewTransactionFragment 2019-12-13 kl.19:06.
 * Not in use anymore.
 */
public class ViewTransactionFragmentBackup extends Fragment implements DatePickerFragment.Listener {
    private View rootView;
    private boolean isIncome; //if false --> = outcome
    private boolean isFiltered;
    private ArrayList<Item> itemArrayList;
    private CustomListAdapter customListAdapter;
    private String date;

    //UI-components
    private TextView headline;
    private Switch toggleBtn;
    private Button filterDateBtn;
    private Button resetDateBtn;
    private ListView listView;

    //variables for storing data from database
    // - INCOME
    private final int[] income_itemIconList = new int[] {R.drawable.icon_salary, R.drawable.icon_other}; //for storing the each items icon
    private String[] income_itemTitleList = {}; //for storing the each items title
    private String[] income_itemDateList = {}; //for storing the each items date
    private double[] income_itemAmountList; //for storing the each items amount (price)
    private String[] income_itemCategoryList = {}; //for storing the each items category
    // - OUTCOME
    private final int[] outcome_itemIconList = new int[] {R.drawable.icon_food, R.drawable.icon_sparetime, R.drawable.icon_travel, R.drawable.icon_acc, R.drawable.icon_other, R.drawable.icon_salary};
    private String[] outcome_itemTitleList = {};
    private String[] outcome_itemDateList = {};
    private double[] outcome_itemAmountList;
    private String[] outcome_itemCategoryList = {};

    //variables for generating item-objects from the data (icon, title, date, amount and category) fetched from the database
    private ArrayList<Item> incomeItems;
    private ArrayList<Item> outcomeItems;
    private ArrayList<Item> filteredIncomeItems; //used to store item-objects filtered after a specific date
    private ArrayList<Item> filteredOutcomeItems;


    public ViewTransactionFragmentBackup() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_view_transaction, container, false);
        //initiateComponents();
        //registerListeners();
        //isIncome = true;

        //set up the custom list adapter view
        //initiateCustomListAdapter();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //save relevant values just before the screen rotation
        //UI-components
        outState.putString("headline", headline.getText().toString());
        outState.putString("toggleBtnText", toggleBtn.getText().toString());

        //save local variables that holds data
        outState.putString("date", date);
        outState.putBoolean("isIncome", isIncome);
        outState.putBoolean("isFiltered", isFiltered);
        outState.putParcelableArrayList("filteredIncomeItems", filteredIncomeItems);
        outState.putParcelableArrayList("filteredOutcomeItems", filteredOutcomeItems);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initiateComponents();
        registerListeners();

        //första gången som detta fragment laddas
        if(savedInstanceState == null) {
            isIncome = true;
            isFiltered = false;

            //allows to restore (saved) values after the screen rotation is done
        } else {
            //restoring UI-components
            headline.setText(savedInstanceState.getString("headline"));
            toggleBtn.setText(savedInstanceState.getString("toggleBtnText"));

            //restoring local variables that holds data
            date = savedInstanceState.getString("filterBtnText"); //denna lilla specialaren behövs då date hämtas från datePickern (vilket inte anropas då skärmen roteras)
            isIncome = savedInstanceState.getBoolean("isIncome");
            isFiltered = savedInstanceState.getBoolean("isFiltered");
            filteredIncomeItems = savedInstanceState.getParcelableArrayList("filteredIncomeItems");
            filteredOutcomeItems = savedInstanceState.getParcelableArrayList("filteredOutcomeItems");
        }

        initiateCustomListAdapter(isIncome); //set up the custom list adapter view


        toggleBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(isIncome) {
                    headline.setText("All outcome");
                    toggleBtn.setText("Toggle to show income instead");
                    setItemListContent(outcomeItems);
                    isIncome = false;
                } else {
                    headline.setText("All income");
                    toggleBtn.setText("Toggle to show outcome instead");
                    setItemListContent(incomeItems);
                    isIncome = true;
                }
            }
        });
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
    public void initiateCustomListAdapter(boolean isIncome) {
        itemArrayList = generateItemsList(isIncome);
        customListAdapter = new CustomListAdapter(MainActivity.context, itemArrayList);
        listView = rootView.findViewById(R.id.viewTransaction_list);
        listView.setAdapter(customListAdapter);

        /**
         * listener for the listview
         */
        //TODO: maybe move the method out from initiateCustomListAdapter so the itemlistener can adapt after if the listview is displaying income or outcome?
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View arg1, int position, long arg3) {
                Log.d(null, "CLICKED row number: " + position);

                //TODO: picks wrong item (i avseende till income/outcome), why??? BUT, it does pick from the right row
                Item clickedItem = itemArrayList.get(position);

                Bundle extras = new Bundle();
                extras.putString("ClickedItemTitle", clickedItem.getTitle());
                extras.putString("ClickedItemDate", clickedItem.getDate());
                extras.putDouble("ClickedItemAmount", clickedItem.getAmount());
                extras.putString("ClickedItemCategory", clickedItem.getCategory());

                Intent intent = new Intent(getActivity().getApplicationContext(), DetailActivity.class);
                intent.putExtras(extras); //skicka med Items attribut
                startActivity(intent);
            }
        });
    }

    //shows the items from income as default when the list is generated into the interface for the first time
    public ArrayList<Item> generateItemsList(boolean isIncome) {
        incomeItems = new ArrayList<Item>(); //income used as default
        outcomeItems = new ArrayList<Item>();
        fetchAllIncomeFromDatabase();
        fetchAllOutcomeFromDatabase();

        if(isFiltered && isIncome) {
            return filteredIncomeItems;
        } else  if(isFiltered && !isIncome) {
            return filteredOutcomeItems;
        }

        if(isIncome) {
            return incomeItems;
        } else {
            return outcomeItems;
        }
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
     * ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    public void fetchAllIncomeFromDatabase() {
        //retrieves the data from the database
        income_itemTitleList = Startup.db.getIncomeValuesFromRowNbr(1);
        income_itemDateList = Startup.db.getIncomeValuesFromRowNbr(2);

        //amount-value needs to be converted from String to int
        String[] temp = Startup.db.getIncomeValuesFromRowNbr(3);
        income_itemAmountList = new double[temp.length];
        for(int i = 0; i < temp.length; i++) {
            income_itemAmountList[i] = Double.parseDouble(temp[i]);
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
        outcome_itemAmountList = new double[temp.length];
        for(int i = 0; i < temp.length; i++) {
            outcome_itemAmountList[i] = Double.parseDouble(temp[i]);
        }
        outcome_itemCategoryList = Startup.db.getOutcomeValuesFromRowNbr(4);

        createOutcomeItemObjects(); //creates a Item-ArrayList of the outcome-values above
        sortItemList(outcomeItems); //sortes the list after date
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
     * --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    public void setItemListContent(ArrayList<Item> itemList) {
        customListAdapter = new CustomListAdapter(MainActivity.context, itemList);
        listView.setAdapter(customListAdapter);
    }

    /*public void setItemListContentToOutcome() {
        customListAdapter = new CustomListAdapter(MainActivity.context, outcomeItems);
        listView.setAdapter(customListAdapter);
    }*/

    /**
     * Methods for handling date picking
     * --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public void returnDate(String date) {
        this.date = date;
        setDate(date);
        if(isIncome) {
            setHeadlineText("Income from " + date);
            filteredIncomeItems = filterItemsAfterDate(incomeItems, date); //filtrera item-listan: välj bort de items som är innan valt datum
            setItemListContent(filteredIncomeItems); //uppdaterar listvyn med setItemListContentToIncome();
        } else {
            setHeadlineText("Outcome from " + date);
            filteredOutcomeItems = filterItemsAfterDate(outcomeItems, date); //filtrera item-listan: välj bort de items som är innan valt datum
            setItemListContent(filteredOutcomeItems); //uppdaterar listvyn med setItemListContentToOutcome();
        }
        isFiltered = true;
    }

    /**
     * displays a date picker dialog
     */
    public void showDatePickerDialog() {
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.onDateSetListener(this);
        datePicker.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    /**
     * sets the date
     */
    public void setDate(String string) {
        this.date = string;
    }

    /**
     * sets the text of the headline in ViewTransactionFragment
     */
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
    }

    /**
     * removes dates that occurs before a given start date
     * @param itemList, list of items
     * @param startDate, t.ex. 18/08/2019
     */
    public ArrayList<Item> filterItemsAfterDate(ArrayList<Item> itemList, String startDate) {
        ArrayList<Item> filteredItemList = new ArrayList<Item>();
        boolean lastRelevantDateFound = false;
        int index = 0;

        //iterera genom / hitta index direkt för startDate, i (redan sorterad) income/outcome lista efter startDate, behåll items som kommer efter startDate
        while(lastRelevantDateFound == false) {
            if(0 <= itemList.get(index).getDate().compareTo(startDate)) { //om nuvarande datum är "större" än eller lika med startDate
                filteredItemList.add(itemList.get(index));
                index++;
            } else {
                lastRelevantDateFound = true;
            }
        }

        return filteredItemList;
    }

    /**
     * inner class to handle clicks
     * ----------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                /* case R.id.viewTransaction_toggleBtn:
                    if(isIncome) {
                        headline.setText("All outcome");
                        toggleBtn.setText("Toggle to show income instead");
                        setItemListContent(outcomeItems);
                        isIncome = false;
                    } else {
                        headline.setText("All income");
                        toggleBtn.setText("Toggle to show outcome instead");
                        setItemListContent(incomeItems);
                        isIncome = true;
                    }
                    break; */

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
                        setItemListContent(incomeItems);
                        setHeadlineText("All income");
                    } else {
                        fetchAllOutcomeFromDatabase();
                        setItemListContent(outcomeItems);
                        setHeadlineText("All outcome");
                    }
                    isFiltered = false;
                    break;
            }
        }
    }
}
