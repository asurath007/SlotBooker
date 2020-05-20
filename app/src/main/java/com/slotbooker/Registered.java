//package com.slotbooker;
//
//import android.content.SharedPreferences;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.EventListener;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestoreException;
//import com.google.firebase.firestore.ListenerRegistration;
//import com.google.firebase.firestore.QuerySnapshot;
//import com.slotbooker.Adapter.AfterPaymentAdapter;
//import com.slotbooker.Adapter.Model.MapList;
//import com.slotbooker.AfterPayment.AfterPayment;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//import static android.content.Context.MODE_PRIVATE;
//
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link Registered#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class Registered extends Fragment {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private static final String TAG ="Registered" ;
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//    private RecyclerView reg_rv;
//    private RecyclerView.LayoutManager layoutManager;
//    private RecyclerView.Adapter rv_adapter;
//    private List<MapList> mapLists;
//    String id="",sID="",lID="",playerID="";
//    private SharedPreferences sp1,sp2;
//
//    private ListenerRegistration firestoreListener;
//    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
//    private CollectionReference matchRef = firestoreDB.collection("Match List");
//
//    public Registered() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment Registered.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static Registered newInstance(String param1, String param2) {
//        Registered fragment = new Registered();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//        mapLists = new ArrayList<>();
//        loadMatchList();
//
//        sp1 = getContext().getSharedPreferences("signupKey", MODE_PRIVATE);
//        sID = sp1.getString("value", "");
//        sp2 = getContext().getSharedPreferences("loginKey",MODE_PRIVATE);
//        lID = sp2.getString("value","");
//        playerID = sID+"-"+lID;
//
////        firestoreListener = matchRef.whereArrayContains("players", playerID)
////                .addSnapshotListener(new EventListener<QuerySnapshot>() {
////                    @Override
////                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
////                        if (e != null) {
////                            Log.e(TAG, "Listen failed!", e);
////                            return;
////                        }
////
////                        List<MapList> getMatchList = new ArrayList<>();
////
////                        assert queryDocumentSnapshots != null;
////                        for (DocumentSnapshot doc : queryDocumentSnapshots){
////                            MapList match = doc.toObject(MapList.class);
////                            assert match != null;
////                            match.setId(doc.getId());
////                            getMatchList.add(match);
////                        }
////                        rv_adapter = new AfterPaymentAdapter(getMatchList, getContext(), firestoreDB);
////                        reg_rv.setAdapter(rv_adapter);
////                        rv_adapter.notifyDataSetChanged();
////                    }
////                });
//    }
//
//    private void loadMatchList() {
//        matchRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                if (task.isSuccessful()) {
//                    List<MapList> getMatchList = new ArrayList<>();
//
//                    for (DocumentSnapshot doc : Objects.requireNonNull(task.getResult())) {
//                        MapList match = doc.toObject(MapList.class);
//                        assert match != null;
//                        match.setId(doc.getId());
//                        getMatchList.add(match);
//                    }
//                } else {
//                    Toast.makeText(getActivity(), "Error getting Match List", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//
//    }
//
//    public void onDestroy() {
//        super.onDestroy();
//
//        firestoreListener.remove();
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_registered, container, false);
//        firestoreListener = matchRef.whereArrayContains("players", playerID)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                        if (e != null) {
//                            Log.e(TAG, "Listen failed!", e);
//                            return;
//                        }
//
//                        List<MapList> getMatchList = new ArrayList<>();
//
//                        assert queryDocumentSnapshots != null;
//                        for (DocumentSnapshot doc : queryDocumentSnapshots){
//                            MapList match = doc.toObject(MapList.class);
//                            assert match != null;
//                            match.setId(doc.getId());
//                            getMatchList.add(match);
//                        }
//                        rv_adapter = new AfterPaymentAdapter(getMatchList, getContext(), firestoreDB);
//                        reg_rv.setAdapter(rv_adapter);
//                        rv_adapter.notifyDataSetChanged();
//                    }
//                });
//        return view;
//    }
//}
