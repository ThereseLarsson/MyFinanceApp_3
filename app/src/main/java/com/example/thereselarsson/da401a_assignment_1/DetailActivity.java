package com.example.thereselarsson.da401a_assignment_1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * shows a detailed view of a transaction-item
 */
public class DetailActivity extends AppCompatActivity {
    private TextView titleTxtView;
    private TextView dateTxtView;
    private TextView amountTxtView;
    private TextView categoryTxtView;
    private ImageView iconView;

    private String title;
    private String date;
    private int amount;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initiateComponents();
        getData();
        setData();
    }

    public void initiateComponents() {
        iconView = findViewById(R.id.detailView_icon);
        titleTxtView = findViewById(R.id.detailView_title);
        dateTxtView = findViewById(R.id.detailView_date);
        amountTxtView = findViewById(R.id.detailView_amount);
        categoryTxtView = findViewById(R.id.detailView_category);
    }

    public void getData() {
        Intent intent = getIntent(); //get the intent in the target activity
        Bundle extras = intent.getExtras(); // gets the attached bundle from the intent

        //extract the stored data from the bundle
        title = extras.getString("ClickedItemTitle");
        date = extras.getString("ClickedItemDate");
        amount = extras.getInt("ClickedItemAmount");
        category = extras.getString("ClickedItemCategory");
    }

    private void setData() {
        titleTxtView.setText(title);
        dateTxtView.setText(date);
        amountTxtView.setText(Integer.toString(amount));
        categoryTxtView.setText(category);

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
