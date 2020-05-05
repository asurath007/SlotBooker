package com.slotbooker.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.slotbooker.Adapter.MapAdapter;
import com.slotbooker.Adapter.Model.MapList;
import com.slotbooker.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SquadMapSelector extends AppCompatActivity {

    private static final String TAG = "Map Selector";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mapAdapter;
    private List<MapList> mapLists;

    private ListenerRegistration firestoreListener;
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference matchRef = firestoreDB.collection("Match List");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_selector_solo);

        recyclerView = findViewById(R.id.rv_map_squad);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mapLists = new ArrayList<>();
        loadMatchList();

        firestoreListener = matchRef.whereEqualTo("type","SQUAD")
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
                        mapAdapter = new MapAdapter(getMatchList, getApplicationContext(), firestoreDB);
                        recyclerView.setAdapter(mapAdapter);
                        mapAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void loadMatchList() {
        matchRef.whereEqualTo("map","erangel").
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
//                    mapAdapter = new MapAdapter(getMatchList, getApplicationContext(), firestoreDB);
////                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//                    recyclerView.setAdapter(mapAdapter);
//                    mapAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(SquadMapSelector.this, "Error getting Match List", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    protected void onDestroy() {
        super.onDestroy();

        firestoreListener.remove();
    }

}