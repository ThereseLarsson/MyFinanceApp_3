package com.example.thereselarsson.MyFinanceApp_2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    //Do something with the date chosen by the user
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        month++;
        String strDay;
        String strMonth;
        String strYear = Integer.toString(year);

        if(day < 10) { //make the 'day' into two-digits if not already
            strDay = "0" + Integer.toString(day);
        } else {
            strDay = Integer.toString(day);
        }

        if(month < 10) {
            strMonth = "0" + Integer.toString(month);
        } else {
            strMonth = Integer.toString(month);
        }

        date = strDay + "/" + strMonth + "/" + strYear;

        if(listener != null) {
            listener.returnDate(date);
        }
    }
}
