package com.slotbooker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.slotbooker.Adapter.Model.MapList;
import com.slotbooker.Adapter.Model.PlayerList;
import com.slotbooker.R;

import java.util.List;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.ViewHolder> {
    private Context context;
    private List<PlayerList> playerList;


    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

    public PlayerListAdapter(Context context, List playerList){
        this.context= context;

    }

    public PlayerListAdapter(List<PlayerList> getPlayerList, Context applicationContext, FirebaseFirestore firestoreDB) {
        this.playerList = getPlayerList;
        this.context =applicationContext;
        this.firestoreDB = firestoreDB;
    }

    @NonNull
    @Override
    public PlayerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_list_row, parent, false);
        return new PlayerListAdapter.ViewHolder(view, context); //view from the ViewHolder @end is passed
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerListAdapter.ViewHolder holder, int position) {
        PlayerList player = playerList.get(position);

        holder.name.setText(player.getPlayerName());
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;


        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

//            PlayerList player = playerList.get(getAdapterPosition());
        name=itemView.findViewById(R.id.player_name);

        }
    }
}
