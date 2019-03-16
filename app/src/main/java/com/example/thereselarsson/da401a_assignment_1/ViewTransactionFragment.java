package com.example.thereselarsson.da401a_assignment_1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    private String[] titleList = {};
    private int[] iconList = {};
    private boolean isIncome; //if false --> = outcome

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
        ArrayList<Item> itemArrayList = generateItemsList();
        CustomListAdapter customListAdapter = new CustomListAdapter(MainActivity.context, itemArrayList);
        listView = rootView.findViewById(R.id.viewTransaction_list);
        listView.setAdapter(customListAdapter);
    }

    public ArrayList<Item> generateItemsList() {
        ArrayList<Item> items = new ArrayList<Item>();
        titleList = new String[] {"Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6"};
        iconList = new int[] {R.drawable.icon_acc, R.drawable.icon_food, R.drawable.icon_sparetime, R.drawable.icon_travel, R.drawable.icon_other, R.drawable.icon_salary};
        Item item;

        for(int i = 0; i < 6; i++) {
            item = new Item(iconList[i], titleList[i]);
            items.add(item);
        }
        return items;
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
                        //show list of income items from table in database
                        isIncome = false;
                    } else {
                        headline.setText("All income");
                        toggleBtn.setText("Toggle to show outcome instead");
                        //show list of outcome items from table in database
                        isIncome = true;
                    }
                    break;

                case R.id.viewTransaction_filterDateBtn:
                    if(isIncome) {
                        //filter from date
                    } else {
                        //filter from date
                    }
                    break;
            }
        }
    }
}
