package com.example.thereselarsson.da401a_assignment_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * shows a detailed view of a transaction-item
 */
public class DetailActivity extends AppCompatActivity {
    private TextView titleTxt;
    private TextView dateTxt;
    private TextView amountTxt;
    private TextView categoryTxt;
    private ImageView iconView;

    private String title;
    private String date;
    private double amount;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initiateComponents();
    }

    public void initiateComponents() {

    }

    public void setIcon() {
        //utefter kategori
    }
}
