package com.slotbooker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.slotbooker.Registration.Duo;
import com.slotbooker.Registration.Solo;
import com.slotbooker.Registration.Squad;
import com.slotbooker.UI.MapSelector;

public class Main2Activity extends AppCompatActivity {

    private Button btn_solo;
    private Button btn_duo;
    private Button btn_squad;
    private int solo_count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main2);

//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);

        btn_solo = findViewById(R.id.btn_solo);
        btn_duo = findViewById(R.id.btn_duo);
        btn_squad = findViewById(R.id.btn_squad);



        btn_solo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                solo_count++;
//                Bundle keeper = new Bundle();
//                keeper.putInt("key", solo_count);
                startActivity(new Intent(Main2Activity.this, MapSelector.class));

            }
        });


        btn_duo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this, Duo.class));

            }
        });

        btn_squad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this, Squad.class));
            }
        });
    }
}