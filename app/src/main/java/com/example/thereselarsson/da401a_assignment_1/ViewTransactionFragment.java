package com.example.thereselarsson.da401a_assignment_1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ViewTransactionFragment extends Fragment {
    private View rootView;
    private ListView listView;
    private boolean isIncome; //if false --> = outcome
    private ArrayList<Item> itemArrayList;
    private CustomListAdapter customListAdapter;
    private Item item;
    private ArrayList<Item> items;

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
    }

    /**
     * adds listeners to components
     */
    private void registerListeners() {
        ClickListener clickListener = new ClickListener();
        toggleBtn.setOnClickListener(clickListener);
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
     * shows the user a message when they fail to create a valid account
     */
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * inner class to handle clicks
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
                        //filter from date --> show list of outcome items from table in database
                    } else {
                        //filter from date --> show list of outcome items from table in database
                    }
                    break;
            }
        }
    }
}
