package com.slotbooker.Admin;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.slotbooker.R;
import com.slotbooker.UI.AdminAddMatch;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public  class datepicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private int _date,_month,_year;
    private Context context;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int date) {
        // Do something with the date chosen by the user
        _date=date;_month=month;_year=year;


        Intent intent = new Intent(getActivity().getApplicationContext(), AdminAddMatch.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("date",_date);
        intent.putExtra("month",_month);
        intent.putExtra("year", _year);
        startActivity(intent);
    }

}
