package com.slotbooker.UI;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.slotbooker.Adapter.Model.PlayerList;
import com.slotbooker.Adapter.PlayerListAdapter;
import com.slotbooker.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddPlayerList extends AppCompatActivity {

    private static final String TAG = "AddPLayerList";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mapAdapter;
    private ListenerRegistration firestoreListener;
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference playerRef = firestoreDB.collection("Solo Registration");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);

        loadPlayerList();
        recyclerView = findViewById(R.id.rv_list_player);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //prioritize acc to date,time,payment
        firestoreListener = playerRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(TAG, "Listen failed!", e);
                    return;
                }

                List<PlayerList> getMapList = new ArrayList<>();

                assert queryDocumentSnapshots != null;
                for (DocumentSnapshot doc : queryDocumentSnapshots){
                    PlayerList player = doc.toObject(PlayerList.class);
                    assert player != null;
//                    player.setId(doc.getId());
                    getMapList.add(player);
                }
                mapAdapter = new PlayerListAdapter(getMapList, getApplicationContext(), firestoreDB);
                recyclerView.setAdapter(mapAdapter);
                mapAdapter.notifyDataSetChanged();
            }
        });

    }

    private void loadPlayerList() {
        playerRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    List<PlayerList> getMatchList = new ArrayList<>();

                    for (DocumentSnapshot doc : Objects.requireNonNull(task.getResult())) {
                        PlayerList match = doc.toObject(PlayerList.class);
                        assert match != null;
//                        match.setId(doc.getId());
                        getMatchList.add(match);
                    }
                    mapAdapter = new PlayerListAdapter(getMatchList, getApplicationContext(), firestoreDB);
//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setAdapter(mapAdapter);
                    mapAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AddPlayerList.this, "Error getting Match List", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
