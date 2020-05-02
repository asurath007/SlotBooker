package com.slotbooker.UI;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.slotbooker.Adapter.AdminMapAdapter;
import com.slotbooker.Adapter.Model.MapList;
import com.slotbooker.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AdminAddMatch extends AppCompatActivity {

    private static final String TAG = "Admin Add Match";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mapAdapter;
    private List<MapList> mapLists;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private EditText et_entryFee;
    private EditText et_prizeMoney;
    private EditText et_time;
    private EditText et_date;
    private EditText et_mode;
    private EditText et_map;
    private EditText et_title;
    private EditText et_moneyBreakUp;
//    private ProgressBar match_status;
    String id = "";

    private ListenerRegistration firestoreListener;
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference matchRef = firestoreDB.collection("Match List");
    private DocumentReference matchDetail = firestoreDB.document("Match List/Match id");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_match);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                popUp();
            }
        });

        recyclerView = findViewById(R.id.rv_add_match);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // get items from db
        loadMatchFromDB();

        firestoreListener = matchRef
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Listen failed!", e);
                            return;
                        }

                        List<MapList> getMatchList = new ArrayList<>();

                        assert queryDocumentSnapshots != null;
                        for (DocumentSnapshot doc : queryDocumentSnapshots){
                            MapList match = doc.toObject(MapList.class);
                            assert match != null;
                            match.setId(doc.getId());
                            getMatchList.add(match);
                        }
                        mapAdapter = new AdminMapAdapter(getMatchList, getApplicationContext(), firestoreDB);
                        recyclerView.setAdapter(mapAdapter);
                        mapAdapter.notifyDataSetChanged();
                    }
                });
    }


    private void popUp() {
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.match_edit_popup, null);
        et_title =view.findViewById(R.id.et_title);
        et_map = view.findViewById(R.id.et_map);
        et_mode =view.findViewById(R.id.et_mode);
        et_date = view.findViewById(R.id.et_date);
        et_time = view.findViewById(R.id.et_time);
        et_prizeMoney = view.findViewById(R.id.et_prizeMoney);
        et_entryFee = view.findViewById(R.id.et_entryFee);
//        match_status = view.findViewById(R.id.match_status);
        Button btn_save = view.findViewById(R.id.btn_save_match_edit);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                //Save to DataBase & go to next screen
                if (et_title.getText().toString().isEmpty() || et_date.getText().toString().isEmpty() ||
                        et_entryFee.getText().toString().isEmpty() || et_date.getText().toString().isEmpty() ||
                        et_map.getText().toString().isEmpty() || et_mode.getText().toString().isEmpty() ||
                        et_prizeMoney.getText().toString().isEmpty() || et_time.getText().toString().isEmpty() ){
                    Snackbar.make(v, "Empty Fields not Allowed", Snackbar.LENGTH_SHORT).show();
                }else{
                    saveMatchToDB(v);
                }
            }
        });
        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void saveMatchToDB(View v) {
        MapList newMatch = new MapList();

        String newName = et_title.getText().toString();
        String newMap = et_map.getText().toString();
        String newMode = et_mode.getText().toString();
        String newDate = et_date.getText().toString();
        String newTime = et_time.getText().toString();
        String newPrizeMoney = et_prizeMoney.getText().toString();
        String newEntryFee = et_entryFee.getText().toString();
//        String newMoneyBreakUp = et_moneyBreakUp.getText().toString();
//        int newProgress = match_status.getProgress();
        String newId = id;
//
        newMatch.setName(newName);
        newMatch.setMap(newMap);
        newMatch.setMode(newMode);
        newMatch.setDate(newDate);
        newMatch.setTime(newTime);
        newMatch.setPrizeMoney(newPrizeMoney);
        newMatch.setEntryFee(newEntryFee);
//        newMatch.setProgress(newProgress);
        newMatch.setId(newId);

        //save to db
        Map<String, Object> Match = new MapList(newName,newMap,newMode,newDate,newTime,newPrizeMoney,newEntryFee).newMatch();

        firestoreDB.collection("Match List").add(Match)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AdminAddMatch.this, "Match has been Added", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminAddMatch.this, "Note adding Failed", Toast.LENGTH_SHORT).show();
            }
        });

        Snackbar.make(v,"Match Details Saved", Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                //start new Activity
                startActivity(new Intent(AdminAddMatch.this, AdminAddMatch.class));
                finish();
            }
        },1000);
    }

    private void loadMatchFromDB() {
        matchRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    List<MapList> getMatchList = new ArrayList<>();

                    for (DocumentSnapshot doc : Objects.requireNonNull(task.getResult())) {
                        MapList match = doc.toObject(MapList.class);
                        assert match != null;
                        match.setId(doc.getId());
                        getMatchList.add(match);
                    }
                    mapAdapter = new AdminMapAdapter(getMatchList, getApplicationContext(), firestoreDB);
//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setAdapter(mapAdapter);
                    mapAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AdminAddMatch.this, "Error getting Match List", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    protected void onDestroy() {
        super.onDestroy();

        firestoreListener.remove();
    }

}
