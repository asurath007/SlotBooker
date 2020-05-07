package com.slotbooker.Admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.slotbooker.Main2Activity;
import com.slotbooker.R;
import com.slotbooker.Registration.Duo;

public class Admin extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner_admin;
    private ImageButton set_date;
    private ImageButton set_time;
    private TextView player_link;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        set_date = findViewById(R.id.btn_date);
        set_time = findViewById(R.id.btn_time);
        spinner_admin = findViewById(R.id.spinner_admin);
        player_link = findViewById(R.id.player_link);

        set_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });

        set_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDatePickerDialog(v);
            }
        });

        player_link.setClickable(true);
        player_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Admin.this, Main2Activity.class));
            }
        });

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.map_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_admin.setAdapter(adapter);

        spinner_admin.setOnItemSelectedListener(Admin.this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
//        String text = parent.getSelectedItem().toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void showTimePickerDialog(View v) {
//        DialogFragment newFragment = new timepicker();
//        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
//        DialogFragment newFragment = new datepicker();
//        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

}
