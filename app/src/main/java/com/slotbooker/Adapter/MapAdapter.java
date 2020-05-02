package com.slotbooker.Adapter;

import android.content.Context;
import android.content.Intent;
import android.service.autofill.ImageTransformation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.slotbooker.Adapter.Model.MapList;
import com.slotbooker.R;
import com.slotbooker.Registration.Solo;

import java.util.List;

public class MapAdapter extends RecyclerView.Adapter<MapAdapter.ViewHolder> {
    private Context context;
    private List<MapList> mapList;
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

    public MapAdapter(Context context, List mapList){
        this.context = context;
        this.mapList = mapList;
//        this.matchDB = matchDB;

    }

    public MapAdapter(List<MapList> getMatchList, Context applicationContext, FirebaseFirestore firestoreDB) {
        this.mapList = getMatchList;
        this.context = applicationContext;
        this.firestoreDB = firestoreDB;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.maps_list_row, parent, false);
        return new ViewHolder(view); //view from the ViewHolder @end is passed
    }

    @Override
    public void onBindViewHolder(@NonNull MapAdapter.ViewHolder holder, int position) {
        MapList map = mapList.get(position);

        holder.name.setText(map.getName()); holder.map.setText(map.getMap());
        holder.mode.setText(map.getMode()); holder.date.setText(map.getDate());
        holder.time.setText(map.getTime()); holder.prizeMoney.setText(map.getPrizeMoney());
        holder.moneyBreakUp.setText(map.getMoneyBreakUp()); holder.entryFee.setText(map.getEntryFee());
        holder.match_status.setProgress(map.getProgress());
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


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

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

//            MapList list =mapList.get(position);

            //get to registration page

            Intent intent = new Intent(context, Solo.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }


}
