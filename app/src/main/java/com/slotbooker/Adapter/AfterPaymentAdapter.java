package com.slotbooker.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.slotbooker.Adapter.Model.MapList;
import com.slotbooker.AfterPayment.AFSoloDetail;
import com.slotbooker.R;
import com.slotbooker.Registration.Duo;
import com.slotbooker.Registration.Solo;
import com.slotbooker.Registration.Squad;

import java.util.List;

public class AfterPaymentAdapter extends RecyclerView.Adapter<AfterPaymentAdapter.ViewHolder> {
    private Context context;
    private List<MapList> mapList;
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private String mTag;

    public AfterPaymentAdapter(Context context, List mapList){
        this.context = context;
        this.mapList = mapList;
//        this.matchDB = matchDB;

    }
    public AfterPaymentAdapter(List<MapList> getMatchList, Context applicationContext, FirebaseFirestore firestoreDB) {
        this.mapList = getMatchList;
        this.context = applicationContext;
        this.firestoreDB = firestoreDB;
    }

    public AfterPaymentAdapter(List<MapList> getMatchList, Context applicationContext, FirebaseFirestore firestoreDB, String Tag) {
        this.mapList = getMatchList;
        this.context = applicationContext;
        this.firestoreDB = firestoreDB;
        this.mTag = Tag;
    }

    @NonNull
    @Override
    public AfterPaymentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.maps_list_row, parent, false);
        return new AfterPaymentAdapter.ViewHolder(view, context); //view from the ViewHolder @end is passed
    }

    @Override
    public void onBindViewHolder(@NonNull AfterPaymentAdapter.ViewHolder holder, int position) {
        MapList map = mapList.get(position);

        holder.name.setText(map.getName()); holder.map.setText(map.getMap());
        holder.mode.setText(map.getMode()); holder.date.setText(map.getDate());
        holder.time.setText(map.getTime()); holder.prizeMoney.setText(map.getPrizeMoney());
        holder.moneyBreakUp.setText(map.getMoneyBreakUp()); holder.entryFee.setText(map.getEntryFee());
        holder.match_status.setProgress(map.getProgress());
        //holder.type.setText(map.getType());
    }

    @Override
    public int getItemCount() {
        return mapList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name;
        public TextView map;
        public TextView mode;
        public TextView date;
        public TextView time;
        public TextView prizeMoney;
        public TextView moneyBreakUp;
        public TextView entryFee;
        public ProgressBar match_status;


        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
//            mTag = tag;
            context = ctx;
            itemView.setOnClickListener(this);

            name = itemView.findViewById(R.id.tv_match_title);
            map = itemView.findViewById(R.id.tv_map);
            date = itemView.findViewById(R.id.tv_date);
            time = itemView.findViewById(R.id.tv_time);
            mode = itemView.findViewById(R.id.tv_mode);
            prizeMoney =itemView.findViewById(R.id.tv_prizeMoney);
            moneyBreakUp = itemView.findViewById(R.id.tv_moneyBreakUp);
            entryFee = itemView.findViewById(R.id.tv_entryFee);
            match_status = itemView.findViewById(R.id.match_status);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            MapList list = mapList.get(position);

            //get to registered page
//            switch (mTag){
//                case "SOLO_PAID":
//                    Intent intentSolo = new Intent(context, AFSoloDetail.class);
//                    intentSolo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intentSolo.putExtra("name",list.getName());
//                    intentSolo.putExtra("map",list.getMap());
//                    intentSolo.putExtra("date",list.getDate());
//                    intentSolo.putExtra("time",list.getTime());
//                    intentSolo.putExtra("pm",list.getPrizeMoney());
//                    intentSolo.putExtra("ef",list.getEntryFee());
//                    intentSolo.putExtra("mbu",list.getMoneyBreakUp());
//                    intentSolo.putExtra("mode",list.getMode());
//                    context.startActivity(intentSolo);
//                    break;
//                case "DUO":
//                    Intent intentDuo = new Intent(context, Duo.class);
//                    intentDuo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intentDuo.putExtra("mode",list.getMode());
//                    intentDuo.putExtra("name",list.getName());
//                    intentDuo.putExtra("map",list.getMap());
//                    intentDuo.putExtra("date",list.getDate());
//                    intentDuo.putExtra("time",list.getTime());
//                    intentDuo.putExtra("pm",list.getPrizeMoney());
//                    intentDuo.putExtra("ef",list.getEntryFee());
//                    intentDuo.putExtra("mbu",list.getMoneyBreakUp());
//                    context.startActivity(intentDuo);
//                    break;
//                case "SQUAD":
//                    Intent intentSquad = new Intent(context, Squad.class);
//                    intentSquad.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intentSquad.putExtra("mode",list.getMode());
//                    intentSquad.putExtra("name",list.getName());
//                    intentSquad.putExtra("map",list.getMap());
//                    intentSquad.putExtra("date",list.getDate());
//                    intentSquad.putExtra("time",list.getTime());
//                    intentSquad.putExtra("pm",list.getPrizeMoney());
//                    intentSquad.putExtra("ef",list.getEntryFee());
//                    intentSquad.putExtra("mbu",list.getMoneyBreakUp());
//                    context.startActivity(intentSquad);
//                    break;
//            }

        }
    }

}
