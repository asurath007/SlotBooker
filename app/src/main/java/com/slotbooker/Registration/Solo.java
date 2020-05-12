package com.slotbooker.Registration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.slotbooker.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Solo extends AppCompatActivity implements AdapterView.OnItemSelectedListener, PaymentResultListener {

    private static final String TAG = "SoloRegistration";
    private EditText et_solo;
    private Button btn_submit;
    private Spinner spinner_solo;
    private ProgressBar progressBar;
    private ProgressBar statusBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Solo Registration");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo);

        firebaseAuth = FirebaseAuth.getInstance();

        et_solo = findViewById(R.id.et_solo);
        btn_submit = findViewById(R.id.btn_solo_submit);
        spinner_solo = findViewById(R.id.spinner_solo);
        progressBar = findViewById(R.id.solo_progressBar);
        statusBar = findViewById(R.id.progressbar_solo);


        //Slot Booking dropdown
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.slot_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_solo.setAdapter(adapter);

        spinner_solo.setOnItemSelectedListener(this);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(et_solo.getText().toString())) {

                    progressBar.setVisibility(View.VISIBLE);

                    String player1 = et_solo.getText().toString().trim();
                    String slotBooked = spinner_solo.getSelectedItem().toString();

                    Map<String, Object> player = new HashMap<>();
                    player.put("player1", player1);
                    player.put("slotBooked", slotBooked);

                    collectionReference.add(player).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Solo.this, "Registration Failed",
                                    Toast.LENGTH_LONG).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(Solo.this, "\t\tComplete Payment to \n confirm your participation",
                                    Toast.LENGTH_SHORT).show();
                            startPayment();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                } else {
                    Toast.makeText(Solo.this, "Empty fields not allowed",
                            Toast.LENGTH_LONG).show();
                }
            }

    });

//        Bundle gotKeeper = getIntent().getExtras();
//        score = gotKeeper.getInt("key");

        //set progress bar
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int count=0;
                    if (count<100){
                    for (DocumentSnapshot snapshot : Objects.requireNonNull(task.getResult())) {
                        count=count+1;
                    }Toast.makeText(Solo.this, "Participants joined= "+count,Toast.LENGTH_SHORT).show();
                        statusBar.setProgress(count);
                    }else{
                        Toast.makeText(Solo.this,"Slot Filled",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "Error getting participant list ", task.getException());
                }
            }
        });

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        String text = parent.getSelectedItem().toString();

        // Showing selected spinner item
//        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

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
