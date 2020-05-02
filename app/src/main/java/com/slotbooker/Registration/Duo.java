package com.slotbooker.Registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;
import java.util.Map;

public class Duo extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private EditText et_duo_name;
    private EditText et_duo_1;
    private EditText et_duo_2;
    private Button btn_submit;
    private Spinner spinner_duo;
    private ProgressBar progressBar;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Duo Registration");
//    private DocumentReference documentReference = db.collection("Solo Registration").document("Team[]");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duo);

        et_duo_name = findViewById(R.id.et_duo_name);
        et_duo_1 = findViewById(R.id.et_duo_1);
        et_duo_2 = findViewById(R.id.et_duo_2);
        btn_submit = findViewById(R.id.btn_duo_submit);
        spinner_duo = findViewById(R.id.spinner_duo);
        progressBar = findViewById(R.id.duo_progressBar);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.slot_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_duo.setAdapter(adapter);

        spinner_duo.setOnItemSelectedListener(Duo.this);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(et_duo_name.getText().toString())
                    && !TextUtils.isEmpty(et_duo_1.getText().toString())
                    && !TextUtils.isEmpty(et_duo_2.getText().toString())) {

                progressBar.setVisibility(View.VISIBLE);

                String teamName = et_duo_name.getText().toString().trim();
                String player1 = et_duo_1.getText().toString().trim();
                String player2 = et_duo_2.getText().toString().trim();
                String slot = spinner_duo.getSelectedItem().toString();

                Map<String, Object> player = new HashMap<>();
                player.put("teamName",teamName);
                player.put("player1",player1);
                player.put("player2",player2);
                player.put("slotBooked",slot);

                collectionReference.add(player).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Duo.this, "Registration Failed",
                                Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(Duo.this, "\t\tComplete Payment to \n confirm your participation",
                                Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });

            } else {
                Toast.makeText(Duo.this, "Empty fields not allowed",
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
