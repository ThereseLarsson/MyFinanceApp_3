package com.example.thereselarsson.da401a_assignment_1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Fragment for displaying the user´s financial summary
 */
public class SummaryFragment extends Fragment {
    private View rootView;
    private TextView totalIncomeTxt;
    private TextView totalOutcomeTxt;
    private TextView sumTxt;
    private double totalIncome;
    private double totalOutcome;
    private String sum;

    public SummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_summary, container, false);
        initiateComponents();
        setTotalIncome();
        setTotalOutcome();
        setSum();
        return rootView;
    }

    /**
     * initiates the components
     */
    public void initiateComponents() {
        totalIncomeTxt = rootView.findViewById(R.id.summary_income);
        totalOutcomeTxt = rootView.findViewById(R.id.summary_outcome);
        sumTxt = rootView.findViewById(R.id.summary_sum);
    }

    /**
     * gets the total income from the database and updates the UI
     */
    public void setTotalIncome() {
        totalIncome = Startup.db.getTotalIncome();
        totalIncomeTxt.setText(Double.toString(totalIncome));
    }

    /**
     * gets the total outcome from the database and updates the UI
     */
    public void setTotalOutcome() {
        totalOutcome = Startup.db.getTotalOutcome();
        totalOutcomeTxt.setText(Double.toString(totalOutcome));
    }

    /**
     * gets the sum (difference between income and outcome) and updates the UI
     */
    public void setSum() {
        if(totalIncome > totalOutcome) {
            sum = "+" + Double.toString(totalIncome - totalOutcome) + " kr";
        } else if(totalOutcome > totalIncome) {
            sum = "-" + Double.toString(totalOutcome - totalIncome) + " kr";
        } else {
            sum = "0 kr";
        }
        sumTxt.setText(sum);
    }
}
