package com.example.thereselarsson.da401a_assignment_1;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * class that provides a date picker fragment with a interface so it can be used
 * by multiple activities / fragments
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public static int year;
    public static int month;
    public static int day;
    public String date;
    //private EnterTransactionFragment etf;
    //private Button btn;
    private Listener listener;

    public interface Listener {
        void returnDate(String date);
    }

    public void onDateSetListener(Listener listener){
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        //etf = new EnterTransactionFragment();
        //listener = (Listener)getActivity();

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    /**
     * Do something with the date chosen by the user
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        date = Integer.toString(day) + "/" + Integer.toString(month) + "/" + Integer.toString(year);
        //etf.setDate(date);
        if(listener != null) {
            listener.returnDate(date);
        }
    }
}
