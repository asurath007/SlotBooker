package com.slotbooker.Registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.slotbooker.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Duo extends AppCompatActivity implements AdapterView.OnItemSelectedListener, PaymentResultListener {


    private EditText et_duo_name;
    private EditText et_duo_1;
    private EditText et_duo_2;
    private Button btn_submit;
    private Spinner spinner_duo;
    private ProgressBar progressBar;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Match List").document("Duo Match").collection("Match List");
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
                player.put("payment", "");

                collectionReference.add(player).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Duo.this, "Registration Failed",
                                Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String id = documentReference.getId();
                        Log.d("Duo","ID: "+id);
                        Toast.makeText(Duo.this, "\t\tComplete Payment to \n confirm your participation",
                                Toast.LENGTH_SHORT).show();
                        startPayment();
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
        Toast.makeText(parent.getContext(), "Selected: " + text + item, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void startPayment() {
        /**
         * You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "MyKnot Gaming");
            options.put("description", "Registration Fee");
            //You can omit the image option to fetch the image from dashboard
//            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");

//            String payment = editTextPayment.getText().toString();
            String payment = "10";
            double total = Double.parseDouble(payment);
            total = total * 100;
            options.put("amount", total);

            JSONObject preFill = new JSONObject();
//            preFill.put("email", "axe.nexas@gmail.com");
//            preFill.put("contact", "9853837232");
//
//            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
//        Toast.makeText(this, "Payment successfully done! " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Payment successfully done! ", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment error please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }
}
