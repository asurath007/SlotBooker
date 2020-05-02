package com.slotbooker.Adapter.Model;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.slotbooker.Adapter.AdminMapAdapter;
import com.slotbooker.R;
import com.slotbooker.UI.AdminAddMatch;

import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AdminAddMatchDetail extends AppCompatActivity {

    private AlertDialog dialog;
    private AlertDialog.Builder dialogBuilder;

    private String id = "";
    private TextView entryFee,time, date,mode,map,title,prizeMoney;
    private EditText et_title,et_map,et_mode,et_date,et_time,et_prizeMoney,et_entryFee;
    private Button btn_edit,btn_delete;
    
    
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference matchRef = firestoreDB.collection("Match List");
    private DocumentReference matchDetail = firestoreDB.document("Match List/Match id");

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_match_detail);

        title =findViewById(R.id.dtv_admin_match_title);
        map = findViewById(R.id.dtv_admin_edit_map);
        mode = findViewById(R.id.dtv_admin_edit_mode);
        date = findViewById(R.id.dtv_admin_edit_date);
        time = findViewById(R.id.dtv_admin_edit_time);
        prizeMoney = findViewById(R.id.dtv_admin_edit_prizeMoney);
        entryFee = findViewById(R.id.dtv_admin_edit_entryFee);
        btn_edit =findViewById(R.id.btn_edit);
        btn_delete=findViewById(R.id.btn_delete);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            id = bundle.getString("UId");
            title.setText(bundle.getString("UName"));
            map.setText(bundle.getString("UMap"));
            mode.setText(bundle.getString("UMode"));
            date.setText(bundle.getString("UDate"));
            time.setText(bundle.getString("UTime"));
            prizeMoney.setText(bundle.getString("UPrizeMoney"));
            entryFee.setText(bundle.getString("UEntryFee"));
        }

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePopup();
            }
        });

    }

    private void deletePopup() {
        dialogBuilder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.delete_dialog,null);

        Button yesButton = v.findViewById(R.id.btn_yes);
        Button noButton = v.findViewById(R.id.btn_no);

        dialogBuilder.setView(v);
        dialog = dialogBuilder.create();
        dialog.show();

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View vi) {
                //deleteFromDB
                matchRef.document(id).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Match Details Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Deleting Failed", Toast.LENGTH_LONG).show();
                    }
                });
                dialog.dismiss();
                startActivity(new Intent(AdminAddMatchDetail.this, AdminAddMatch.class));
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void popup() {
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.match_edit_popup, null);

        TextView tv_title = view.findViewById(R.id.tv_title);
        tv_title.setText("Edit Match");

        et_title =view.findViewById(R.id.et_title);
        et_map = view.findViewById(R.id.et_map);
        et_mode =view.findViewById(R.id.et_mode);
        et_date = view.findViewById(R.id.et_date);
        et_time = view.findViewById(R.id.et_time);
        et_prizeMoney = view.findViewById(R.id.et_prizeMoney);
        et_entryFee = view.findViewById(R.id.et_entryFee);
//        match_status = view.findViewById(R.id.match_status);
        Button btn_save = view.findViewById(R.id.btn_save_match_edit);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            id = bundle.getString("UId");
            et_title.setText(bundle.getString("UName"));
            et_map.setText(bundle.getString("UMap"));
            et_mode.setText(bundle.getString("UMode"));
            et_date.setText(bundle.getString("UDate"));
            et_time.setText(bundle.getString("UTime"));
            et_prizeMoney.setText(bundle.getString("UPrizeMoney"));
            et_entryFee.setText(bundle.getString("UEntryFee"));
        }

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        btn_save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                //Save to DataBase & go to next screen
//                updateDB(v);
                if (et_title.getText().toString().isEmpty() || et_date.getText().toString().isEmpty() ||
                        et_entryFee.getText().toString().isEmpty() || et_date.getText().toString().isEmpty() ||
                        et_map.getText().toString().isEmpty() || et_mode.getText().toString().isEmpty() ||
                        et_prizeMoney.getText().toString().isEmpty() || et_time.getText().toString().isEmpty() ){
                    Snackbar.make(v, "Empty Fields not Allowed", Snackbar.LENGTH_SHORT).show();
                }else{
                    updateMatch(v);
                }
                dialog.dismiss();
                startActivity(new Intent(AdminAddMatchDetail.this, AdminAddMatch.class));
            }
        });

    }

    private void updateMatch(View v) {
        String newName = et_title.getText().toString();
        String newMap = et_map.getText().toString();
        String newMode = et_mode.getText().toString();
        String newDate = et_date.getText().toString();
        String newTime = et_time.getText().toString();
        String newPrizeMoney = et_prizeMoney.getText().toString();
        String newEntryFee = et_entryFee.getText().toString();
//        String newMoneyBreakUp = et_moneyBreakUp.getText().toString();
//        int newProgress = match_status.getProgress();

        Map<String, Object> updateMatch = new MapList(newName, newMap, newMode, newDate, newTime, newPrizeMoney, newEntryFee).newMatch();

        matchRef.document(id).set(updateMatch)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Match Update Successful", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Match Update Failed", Toast.LENGTH_LONG).show();
                    }
                });
    }

}
