package com.example.thereselarsson.da401a_assignment_1;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * shows a detailed view of a transaction-item
 */
public class DetailActivity extends AppCompatActivity {
    private View rootView;
    private Context mContext;
    private Item currentItem;
    private TextView titleTxtView;
    private TextView dateTxtView;
    private TextView amountTxtView;
    private TextView categoryTxtView;
    private ImageView iconView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initiateComponents();
    }

    public void initiateComponents() {
        iconView = rootView.findViewById(R.id.detailView_icon);
        titleTxtView = rootView.findViewById(R.id.detailView_title);
        dateTxtView = rootView.findViewById(R.id.detailView_date);
        amountTxtView = rootView.findViewById(R.id.detailView_amount);
        categoryTxtView = rootView.findViewById(R.id.detailView_category);
    }

    public void setInformation(Item item) {
        this.currentItem = item;
    }

    private void setItemToCurrent() {
        if(currentItem != null) {
            if(titleTxtView != null && dateTxtView != null && amountTxtView != null && categoryTxtView != null) {
                //iconView.setImageResource(currentItem.getIcon());
                titleTxtView.setText(currentItem.getTitle());
                dateTxtView.setText(currentItem.getDate());
                amountTxtView.setText(currentItem.getAmount());
                categoryTxtView.setText(currentItem.getCategory());

                //sets the icon
                //income
                if(categoryTxtView.getText().equals("Salary")) {
                    iconView.setImageResource(R.drawable.icon_salary);
                } else if(categoryTxtView.getText().equals("Other")) {
                    iconView.setImageResource(R.drawable.icon_other);

                    //outcome
                } else if(categoryTxtView.getText().equals("Food")) {
                    iconView.setImageResource(R.drawable.icon_food);
                } else if(categoryTxtView.getText().equals("Leisure")) {
                    iconView.setImageResource(R.drawable.icon_sparetime);
                } else if(categoryTxtView.getText().equals("Travel")) {
                    iconView.setImageResource(R.drawable.icon_travel);
                } else if(categoryTxtView.getText().equals("Accommodation")) {
                    iconView.setImageResource(R.drawable.icon_acc);
                }
            }
        }
    }
}
