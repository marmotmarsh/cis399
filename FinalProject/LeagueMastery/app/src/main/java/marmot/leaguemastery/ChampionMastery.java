package marmot.leaguemastery;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Holden on 7/18/2017.
 *
 */

public class ChampionMastery {

    // Database fields

    private int championMasteryId;
    private int listId;

    // Champion Mastery Information Fields

    private String chestGranted;
    private String championLevel;
    private String championPoints;
    private String championId;
    private String playerId;
    private String championPointsUntilNextLevel;
    private String championPointsSinceLastLevel;
    private String lastPlayTime;

    // Other constants

    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String ZERO = "0";

    public ChampionMastery() {
        this.chestGranted = FALSE;
        this.championLevel = ZERO;
        this.championPoints = ZERO;
        this.championId = ZERO;
        this.playerId = ZERO;
        this.championPointsUntilNextLevel = ZERO;
        this.championPointsSinceLastLevel = ZERO;
        this.lastPlayTime = ZERO;
    }

    public ChampionMastery(int listId,
                           String chestGranted,
                           String championLevel,
                           String championPoints,
                           String championId,
                           String playerId,
                           String championPointsUntilNextLevel,
                           String championPointsSinceLastLevel,
                           String lastPlayTime) {
        this.listId = listId;
        this.chestGranted = chestGranted;
        this.championLevel = championLevel;
        this.championPoints = championPoints;
        this.championId = championId;
        this.playerId = playerId;
        this.championPointsUntilNextLevel = championPointsUntilNextLevel;
        this.championPointsSinceLastLevel = championPointsSinceLastLevel;
        this.lastPlayTime = lastPlayTime;
    }

    public ChampionMastery(int championMasteryId,
                           int listId,
                           String chestGranted,
                           String championLevel,
                           String championPoints,
                           String championId,
                           String playerId,
                           String championPointsUntilNextLevel,
                           String championPointsSinceLastLevel,
                           String lastPlayTime) {
        this.championMasteryId = championMasteryId;
        this.listId = listId;
        this.chestGranted = chestGranted;
        this.championLevel = championLevel;
        this.championPoints = championPoints;
        this.championId = championId;
        this.playerId = playerId;
        this.championPointsUntilNextLevel = championPointsUntilNextLevel;
        this.championPointsSinceLastLevel = championPointsSinceLastLevel;
        this.lastPlayTime = lastPlayTime;
    }

    public static ChampionMastery fromJsonHelper(JSONObject json) {
        ChampionMastery mastery = new ChampionMastery();
        try {
            mastery.setChestGranted(json.getString("chestGranted"));
            mastery.setChampionLevel(json.getString("championLevel"));
            mastery.setChampionPoints(json.getString("championPoints"));
            mastery.setChampionId(json.getString("championId"));
            mastery.setPlayerId(json.getString("playerId"));
            mastery.setChampionPointsUntilNextLevel(json.getString("championPointsUntilNextLevel"));
            mastery.setChampionPointsSinceLastLevel(json.getString("championPointsSinceLastLevel"));
            mastery.setLastPlayTime(json.getString("lastPlayTime"));
        } catch (JSONException e) {
            Log.e("Error", e.toString());

            return null;
        }

        return mastery;
    }

    public static ArrayList<ChampionMastery> fromJson(JSONArray jsonArray) {
        JSONObject jsonObject;
        ArrayList<ChampionMastery> masteries = new ArrayList<>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            try {
                jsonObject = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                Log.e("Error", e.toString());
                continue;
            }

            ChampionMastery mastery = ChampionMastery.fromJsonHelper(jsonObject);
            if (mastery != null) {
                masteries.add(mastery);
            }
        }

        return masteries;
    }

    // Getters and Setters

    public int getChampionMasteryId() {
        return championMasteryId;
    }

    public void setChampionMasteryId(int championMasteryId) {
        this.championMasteryId = championMasteryId;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getChestGranted() {
        return chestGranted;
    }

    public void setChestGranted(String chestGranted) {
        this.chestGranted = chestGranted;
    }

    public String getChampionLevel() {
        return championLevel;
    }

    public void setChampionLevel(String championLevel) {
        this.championLevel = championLevel;
    }

    public String getChampionPoints() {
        return championPoints;
    }

    public void setChampionPoints(String championPoints) {
        this.championPoints = championPoints;
    }

    public String getChampionId() {
        return championId;
    }

    public void setChampionId(String championId) {
        this.championId = championId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getChampionPointsUntilNextLevel() {
        return championPointsUntilNextLevel;
    }

    public void setChampionPointsUntilNextLevel(String championPointsUntilNextLevel) {
        this.championPointsUntilNextLevel = championPointsUntilNextLevel;
    }

    public String getChampionPointsSinceLastLevel() {
        return championPointsSinceLastLevel;
    }

    public void setChampionPointsSinceLastLevel(String championPointsSinceLastLevel) {
        this.championPointsSinceLastLevel = championPointsSinceLastLevel;
    }

    public String getLastPlayTime() {
        return lastPlayTime;
    }

    public void setLastPlayTime(String lastPlayTime) {
        this.lastPlayTime = lastPlayTime;
    }
}
