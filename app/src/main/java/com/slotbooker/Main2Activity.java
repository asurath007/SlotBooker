package com.slotbooker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.slotbooker.Registration.Duo;
import com.slotbooker.Registration.Solo;
import com.slotbooker.Registration.Squad;
import com.slotbooker.UI.MapSelector;

public class Main2Activity extends AppCompatActivity {

    private static final String TAG ="Main2Activity" ;
    private Button btn_solo;
    private Button btn_duo;
    private Button btn_squad;
    private int solo_count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main2);

        // Get token
        // [START retrieve_current_token]
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
//To do//
                            return;
                        }

// Get the Instance ID token//
                        String token = task.getResult().getToken();
                        String msg = getString(R.string.fcm_token, token);
                        Log.d(TAG, msg);

                    }
                });



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