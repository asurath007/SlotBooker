package com.slotbooker.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.slotbooker.Adapter.Model.AdminAddMatchDetail;
import com.slotbooker.Adapter.Model.MapList;
import com.slotbooker.R;
import com.slotbooker.UI.AdminAddMatch;

import java.util.List;
import java.util.Map;

public class AdminMapAdapter extends RecyclerView.Adapter<AdminMapAdapter.ViewHolder> {

    private Context context;
    private List<MapList> mapList;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference matchRef = firestoreDB.collection("Match List");
    private DocumentReference matchDetail = firestoreDB.document("Match List/Match id");
    String id = "";

    public AdminMapAdapter(Context context, List mapList){
        this.context = context;
        this.mapList = mapList;
    }

    public AdminMapAdapter(List<MapList> getMatchList, Context applicationContext, FirebaseFirestore firestoreDB) {
        this.mapList = getMatchList;
        this.context = applicationContext;
        this.firestoreDB = firestoreDB;
    }

    @NonNull
    @Override
    public AdminMapAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_add_match_list_row, parent, false);
        return new AdminMapAdapter.ViewHolder(view, context); //view from the ViewHolder @end is passed
    }

    @Override
    public void onBindViewHolder(@NonNull AdminMapAdapter.ViewHolder holder, int position) {
        MapList map = mapList.get(position);

        holder.name.setText(map.getName()); holder.map.setText(map.getMap());
        holder.mode.setText(map.getMode()); holder.date.setText(map.getDate());
        holder.time.setText(map.getTime()); holder.prizeMoney.setText(map.getPrizeMoney());
        holder.moneyBreakUp.setText(map.getMoneyBreakUp()); holder.entryFee.setText(map.getEntryFee());
        holder.match_status.setProgress(map.getProgress());

//        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mapList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView map;
        public TextView mode;
        public TextView date;
        public TextView time;
        public TextView prizeMoney;
        public TextView moneyBreakUp;
        public TextView entryFee;
        public ProgressBar match_status;
        public Button btn_edit;
        public Button btn_delete;


        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

            context = ctx;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MapList editMatch = mapList.get(getAdapterPosition());
                    Intent intent = new Intent(context, AdminAddMatchDetail.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("UId", editMatch.getId());
                    intent.putExtra("UName", editMatch.getName());
                    intent.putExtra("UMap", editMatch.getMap());
                    intent.putExtra("UMode",editMatch.getMode());
                    intent.putExtra("UDate",editMatch.getDate());
                    intent.putExtra("UTime",editMatch.getTime());
                    intent.putExtra("UPrizeMoney",editMatch.getPrizeMoney());
                    intent.putExtra("UEntryFee",editMatch.getEntryFee());

                    context.startActivity(intent);
                }
            });

            name = itemView.findViewById(R.id.tv_admin_match_title);
            map = itemView.findViewById(R.id.tv_admin_edit_map);
            date = itemView.findViewById(R.id.tv_admin_edit_date);
            time = itemView.findViewById(R.id.tv_admin_edit_time);
            mode = itemView.findViewById(R.id.tv_admin_edit_mode);
            prizeMoney =itemView.findViewById(R.id.tv_admin_edit_prizeMoney);
            moneyBreakUp = itemView.findViewById(R.id.tv_admin_edit_moneyBreakUp);
            entryFee = itemView.findViewById(R.id.tv_admin_edit_entryFee);
            match_status = itemView.findViewById(R.id.admin_statusBar);
//            btn_edit = itemView.findViewById(R.id.btn_edit);
//            btn_delete = itemView.findViewById(R.id.btn_delete);

//            btn_delete.setOnClickListener(this);
//            btn_edit.setOnClickListener(this);
        }

////               @Override
//        public void onClick(View v) {
//
//            switch (v.getId()) {
//                case R.id.btn_edit:
//                    int position = getAdapterPosition();
//                    MapList editMatch = mapList.get(position);
//                    updateMatch(editMatch);
//                    break;
//
//                case R.id.btn_delete:
//                    position = getAdapterPosition();
//                    editMatch = mapList.get(position);
//                    deleteMatchDB(editMatch.getId(), position);
//                    break;
//            }
        }
//
//        private void updateMatch(final MapList editMatch) {
//            alertDialogBuilder = new AlertDialog.Builder(context);
//
//            View v = LayoutInflater.from(context).inflate(R.layout.match_edit_popup, null);
//            final EditText et_title =v.findViewById(R.id.et_title);
//            final EditText et_map = v.findViewById(R.id.et_map);
//            final EditText et_mode =v.findViewById(R.id.et_mode);
//            final EditText et_date = v.findViewById(R.id.et_date);
//            final EditText et_time = v.findViewById(R.id.et_time);
//            final EditText et_prizeMoney = v.findViewById(R.id.et_prizeMoney);
//            final EditText et_entryFee = v.findViewById(R.id.et_entryFee);
////            match_status = v.findViewById(R.id.match_status);
//
//            TextView tv_title = v.findViewById(R.id.tv_title);
//            tv_title.setText("Edit Match");
//
//            Button btn_save = v.findViewById(R.id.btn_save_match_edit);
//            btn_save.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //update DB
//                    String newName = et_title.getText().toString();
//                    String newMap = et_map.getText().toString();
//                    String newMode = et_mode.getText().toString();
//                    String newDate = et_date.getText().toString();
//                    String newTime = et_time.getText().toString();
//                    String newPrizeMoney = et_prizeMoney.getText().toString();
//                    String newEntryFee = et_entryFee.getText().toString();
////                    String newMoneyBreakUp = et_moneyBreakUp.getText().toString();
////                    int newProgress = match_status.getProgress();
//                    String newId = id;
//
//                    editMatch.setName(newName);
//                    editMatch.setMap(newMap);
//                    editMatch.setMode(newMode);
//                    editMatch.setDate(newDate);
//                    editMatch.setTime(newTime);
//                    editMatch.setPrizeMoney(newPrizeMoney);
//                    editMatch.setEntryFee(newEntryFee);
////        newMatch.setProgress(newProgress);
//                    editMatch.setId(newId);
//
//                    if (et_title.getText().toString().isEmpty() || et_date.getText().toString().isEmpty() ||
//                            et_entryFee.getText().toString().isEmpty() || et_date.getText().toString().isEmpty() ||
//                            et_map.getText().toString().isEmpty() || et_mode.getText().toString().isEmpty() ||
//                            et_prizeMoney.getText().toString().isEmpty() || et_time.getText().toString().isEmpty() ){
//                        Snackbar.make(v, "Empty Fields not Allowed", Snackbar.LENGTH_SHORT).show();
//                    }else {
//                        //updateDB
//                        Map<String, Object> updateMatch = new MapList(newName,newMap,newMode,newDate,newTime,newPrizeMoney,newEntryFee).newMatch();
//
//                        matchRef.document(newId).set(updateMatch)
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        Toast.makeText(context,"Match Update Successful", Toast.LENGTH_SHORT).show();
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(context,"Match Update Failed", Toast.LENGTH_LONG).show();
//                                    }
//                        });
//                    }
//                }
//            });
//        }
//
//
//        private void deleteMatchDB(final String id, final int position) {
//            alertDialogBuilder = new AlertDialog.Builder(context);
//
//            inflater = LayoutInflater.from(context);
//            View view = inflater.inflate(R.layout.delete_dialog,null);
//
//            Button yesButton = view.findViewById(R.id.btn_yes);
//            Button noButton = view.findViewById(R.id.btn_no);
//
//            alertDialogBuilder.setView(view);
//            dialog = alertDialogBuilder.create();
//            dialog.show();
//
//            yesButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //delete the item
//                    firestoreDB.collection("Match List")
//                            .document(id).delete()
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    mapList.remove(getAdapterPosition());
//                                    notifyItemRemoved(getAdapterPosition());
//                                    dialog.dismiss();
//                                    Toast.makeText(context, "Match Deleted", Toast.LENGTH_SHORT).show();
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(context, "Match Deleting Failed", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }
//            });
//            noButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            });
//        }
//
//
//    }


}
