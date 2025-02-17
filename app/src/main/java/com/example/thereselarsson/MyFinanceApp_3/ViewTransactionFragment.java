package com.example.thereselarsson.MyFinanceApp_3;

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

//Displays the income/outcome items in a clickable listview
public class ViewTransactionFragment extends Fragment implements DatePickerFragment.Listener {
    //general variables
    private View rootView;
    private boolean isIncome; //if false --> = outcome
    private boolean isFiltered;
    private CustomListAdapter customListAdapter;
    private String date;

    //UI-components
    private TextView headline;
    private Switch toggleBtn;
    private Button filterDateBtn;
    private Button resetDateBtn;
    private ListView listView;

    //variables for storing data (attributes for the Item-object) from database
    private final int[] income_itemIconList = new int[] {R.drawable.icon_salary, R.drawable.icon_other}; //for storing the each items icon
    private final int[] outcome_itemIconList = new int[] {R.drawable.icon_food, R.drawable.icon_sparetime, R.drawable.icon_travel, R.drawable.icon_acc, R.drawable.icon_other, R.drawable.icon_salary};
    private String[] itemTypeList = {};
    private String[] itemTitleList = {};
    private String[] itemDateList = {};
    private double[] itemAmountList = {};
    private String[] itemCategoryList = {};

    //variables for creating item-objects (non-filtered and filtered by date) from the database-attributes
    private ArrayList<Item> itemList;
    private ArrayList<Item> filteredItems;


    public ViewTransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_view_transaction, container, false);
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
        outState.putParcelableArrayList("filteredItems", filteredItems);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initiateComponents();
        registerListeners();

        //the very first time this fragment/UI is loaded
        if(savedInstanceState == null) {
            isIncome = true;
            isFiltered = false;

        //if this fragment/UI has been loaded before - then there are values that (has been saved, and) needs to be restored
        //allows to restore (saved) values after the screen rotation is done
        } else {
            //restoring UI-components
            headline.setText(savedInstanceState.getString("headline"));
            toggleBtn.setText(savedInstanceState.getString("toggleBtnText"));

            //restoring local variables that holds data
            date = savedInstanceState.getString("filterBtnText"); //this little special case is needed since the date-value is retrieved from the datePicker (which is not called on screen rotation)
            isIncome = savedInstanceState.getBoolean("isIncome");
            isFiltered = savedInstanceState.getBoolean("isFiltered");
            filteredItems = savedInstanceState.getParcelableArrayList("filteredItems");
        }
        initiateCustomListAdapter(isIncome); //set up the custom list adapter view

        toggleBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(isIncome) {
                    headline.setText("All outcome");
                    toggleBtn.setText("Toggle to show income instead");
                    isIncome = false;
                    fetchAllTransactionItems("outcome"); //the item-list is filled with outcome-items (which attributes are fetched from the database)
                } else {
                    headline.setText("All income");
                    toggleBtn.setText("Toggle to show outcome instead");
                    isIncome = true;
                    fetchAllTransactionItems("income"); //the item-list is filled with income-items (which attributes are fetched from the database)
                }
                isFiltered = false;
                setItemListContent(itemList);
            }
        });
    }

    public void initiateComponents() {
        headline = rootView.findViewById(R.id.viewTransaction_headline);
        toggleBtn = rootView.findViewById(R.id.viewTransaction_toggleBtn);
        filterDateBtn = rootView.findViewById(R.id.viewTransaction_filterDateBtn);
        resetDateBtn = rootView.findViewById(R.id.viewTransaction_resetDateBtn);
    }

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
        itemList = generateItemList(isIncome);
        customListAdapter = new CustomListAdapter(MainActivity.context, itemList);
        listView = rootView.findViewById(R.id.viewTransaction_list);
        listView.setAdapter(customListAdapter);

        //listener for the listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View arg1, int position, long arg3) {
                Log.d(null, "CLICKED row number: " + position);

                Item clickedItem = itemList.get(position);

                Bundle extras = new Bundle();
                extras.putString("ClickedItemTitle", clickedItem.getTitle());
                extras.putString("ClickedItemDate", clickedItem.getDate());
                extras.putDouble("ClickedItemAmount", clickedItem.getAmount());
                extras.putString("ClickedItemCategory", clickedItem.getCategory());

                Intent intent = new Intent(getActivity().getApplicationContext(), DetailActivity.class);
                intent.putExtras(extras); //sends the Item-attributes
                startActivity(intent);
            }
        });
    }

    //shows the items from income as default when the list is generated into the interface for the first time
    public ArrayList<Item> generateItemList(boolean isIncome) {
        if(isFiltered) { //if the item-list is filtered after date
            return filteredItems;
        } else { //if the item-list is NOT filtered after date, then we want to retrieve ALL the items from the database
            if(isIncome) {
                fetchAllTransactionItems("income");
            } else {
                fetchAllTransactionItems("outcome");
            }
            return itemList;
        }
    }

    /**
     * Retrieves the attributes (item, type, title, date, amount an category)
     * from the database and places them in lists.
     * From these attribute-lists, a single list of Item-objects is created
     *
     * row 0 - id
     * row 1 - type
     * row 2 - title
     * row 3 - date
     * row 4 - amount
     * row 5- category
     * ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    public void fetchAllTransactionItems(String transactionType) {
        Item item;
        itemList = new ArrayList<Item>();

        //retrieves the attributes from the database
        itemTypeList = Startup.db.getTransactionItemsFromColumnNbr(transactionType, 1);
        itemTitleList = Startup.db.getTransactionItemsFromColumnNbr(transactionType, 2);
        itemDateList = Startup.db.getTransactionItemsFromColumnNbr(transactionType, 3);
        itemCategoryList = Startup.db.getTransactionItemsFromColumnNbr(transactionType, 5);

        //amount-value needs to be converted from String to double
        String[] temp = Startup.db.getTransactionItemsFromColumnNbr(transactionType, 4);
        itemAmountList = new double[temp.length];
        for(int i = 0; i < temp.length; i++) {
            itemAmountList[i] = Double.parseDouble(temp[i]);
        }

        //creates an array list of Item-objects
        if(isIncome) {
            for(int i = 0; i < itemTitleList.length; i++) {
                if(itemCategoryList[i].equals("Salary")) {
                    item = new Item(income_itemIconList[0], itemTypeList[i], itemTitleList[i], itemDateList[i], itemAmountList[i], itemCategoryList[i]);
                } else {
                    item = new Item(income_itemIconList[1], itemTypeList[i], itemTitleList[i], itemDateList[i], itemAmountList[i], itemCategoryList[i]);
                }
                itemList.add(item);
            }

        } else {
            for(int i = 0; i < itemTitleList.length; i++) { //the length of the list is equal to the number of Item-objects
                if(itemCategoryList[i].equals("Food")) {
                    item = new Item(outcome_itemIconList[0], itemTypeList[i], itemTitleList[i], itemDateList[i], itemAmountList[i], itemCategoryList[i]);
                } else if (itemCategoryList[i].equals("Leisure")) {
                    item = new Item(outcome_itemIconList[1], itemTypeList[i], itemTitleList[i], itemDateList[i], itemAmountList[i], itemCategoryList[i]);
                } else if (itemCategoryList[i].equals("Travel")) {
                    item = new Item(outcome_itemIconList[2], itemTypeList[i], itemTitleList[i], itemDateList[i], itemAmountList[i], itemCategoryList[i]);
                } else if (itemCategoryList[i].equals("Accommodation")) {
                    item = new Item(outcome_itemIconList[3], itemTypeList[i], itemTitleList[i], itemDateList[i], itemAmountList[i], itemCategoryList[i]);
                } else {
                    item = new Item(outcome_itemIconList[4], itemTypeList[i], itemTitleList[i], itemDateList[i], itemAmountList[i], itemCategoryList[i]);
                }
                itemList.add(item);
            }
        }

        sortItemList(itemList); //sorts the list after date
    }

    //method for changing the content of the list view
    public void setItemListContent(ArrayList<Item> list) {
        customListAdapter = new CustomListAdapter(MainActivity.context, list);
        listView.setAdapter(customListAdapter);
    }

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
        } else {
            setHeadlineText("Outcome from " + date);
        }

        filterItemsAfterDate(date); //filters the item-list: do not include the items BEFORE the chosen date-value
        setItemListContent(filteredItems); //updates the listview with the filtered item-list
        isFiltered = true;
    }

    //displays a date picker dialog
    public void showDatePickerDialog() {
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.onDateSetListener(this);
        datePicker.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    public void setDate(String string) {
        this.date = string;
    }

    public void setHeadlineText(String string) {
        headline.setText(string);
    }

    //sorts the dates so they are in order, dates are given on the format DD/MM/YYYY
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
     * @param startDate, t.ex. 18/08/2019
     */
    public void filterItemsAfterDate(String startDate) {
        filteredItems = new ArrayList<Item>();
        boolean lastRelevantDateFound = false;
        int index = 0;

        //iterate through / find index direclty for startDate, in (the already sorted) income/outcome-list after startDate, keep the items that occurs AFTER the startDate
        while(lastRelevantDateFound == false) {
            if(0 <= itemList.get(index).getDate().compareTo(startDate)) { //if the current date is "bigger" than or equal to startDate, then we want to add the item to the filtered item-list
                filteredItems.add(itemList.get(index));
                index++;
            } else {
                lastRelevantDateFound = true;
            }
        }
    }

    //inner class to handle clicks
    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.viewTransaction_filterDateBtn:
                    if(isIncome) {
                        showDatePickerDialog();
                    } else {
                        showDatePickerDialog();
                    }
                    break;

                case R.id.viewTransaction_resetDateBtn:
                    if(isIncome) {
                        fetchAllTransactionItems("income");
                        setHeadlineText("All income");
                    } else {
                        fetchAllTransactionItems("outcome");
                        setHeadlineText("All outcome");
                    }
                    setItemListContent(itemList);
                    isFiltered = false;
                    break;
            }
        }
    }
}
