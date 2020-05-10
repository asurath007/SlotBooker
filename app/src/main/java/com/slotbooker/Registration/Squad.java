package com.slotbooker.Registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.slotbooker.R;
import com.slotbooker.Payment.RazorPay.RazorPay;

import java.util.HashMap;
import java.util.Map;

public class Squad extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText et_squad_name;
    private EditText et_squad_1;
    private EditText et_squad_2;
    private EditText et_squad_3;
    private EditText et_squad_4;
    private Button btn_submit;
    private Spinner spinner_squad;
    private ProgressBar progressBar;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Squad Registration");
//  private DocumentReference documentReference = db.collection("Solo Registration").document("Team[]");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_squad);

        et_squad_name = findViewById(R.id.et_squad_name);
        et_squad_1 = findViewById(R.id.et_squad_1);
        et_squad_2 = findViewById(R.id.et_squad_2);
        et_squad_3 = findViewById(R.id.et_squad_3);
        et_squad_4 = findViewById(R.id.et_squad_4);
        btn_submit = findViewById(R.id.btn_squad_submit);
        spinner_squad = findViewById(R.id.spinner_squad);
        progressBar =findViewById(R.id.squad_progressBar);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.slot_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_squad.setAdapter(adapter);

        spinner_squad.setOnItemSelectedListener(this);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(et_squad_name.getText().toString())
                        && !TextUtils.isEmpty(et_squad_1.getText().toString())
                        && !TextUtils.isEmpty(et_squad_2.getText().toString())
                        && !TextUtils.isEmpty(et_squad_3.getText().toString())
                        && !TextUtils.isEmpty(et_squad_4.getText().toString())) {

                progressBar.setVisibility(View.VISIBLE);

                String teamName = et_squad_name.getText().toString().trim();
                String player1 = et_squad_1.getText().toString().trim();
                String player2 = et_squad_2.getText().toString().trim();
                String player3 = et_squad_3.getText().toString().trim();
                String player4 = et_squad_4.getText().toString().trim();
                String slot = spinner_squad.getSelectedItem().toString();

                Map<String, Object> player = new HashMap<>();
                player.put("teamName",teamName);
                player.put("player1",player1);
                player.put("player2",player2);
                player.put("player3",player3);
                player.put("player4",player4);
                player.put("slotBooked",slot);

                collectionReference.add(player).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Squad.this, "Registration Failed",
                                Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(Squad.this, "\t\tComplete Payment to \n confirm your participation",
                                Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        startActivity(new Intent(Squad.this, RazorPay.class));
                    }
                });

            }else {
                Toast.makeText(Squad.this, "Empty fields not allowed",
                        Toast.LENGTH_LONG).show();
            }
        }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        String text = parent.getSelectedItem().toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
