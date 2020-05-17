package com.slotbooker.Adapter.Model;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PlayerList {
    String player[];

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    String playerName;


    public String[] getPlayer() {
        return player;
    }

    public void setPlayer(String[] player) {
        this.player = player;
    }



    public PlayerList(String player[]){
        this.player = player;

    }

    public PlayerList(){}

    public Map<String,Object> newPlayer(){

        HashMap<String,Object> playerList = new HashMap<>();
        playerList.put("player1",playerName);
        return newPlayer();
    }


}
