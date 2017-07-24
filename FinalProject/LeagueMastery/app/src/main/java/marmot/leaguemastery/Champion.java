package marmot.leaguemastery;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Holden on 7/21/2017.
 *
 */

public class Champion {

    // Database fields

    private int championTableId;
    private int listId;

    // Champion Mastery Information Fields

    private String title;
    private String championId;
    private String key;
    private String name;

    // Other constants

    public static final String ZERO = "0";

    public Champion() {
        this.title = "";
        this.championId = ZERO;
        this.key = "";
        this.name = "";
    }

    public Champion(int listId,
                    String title,
                    String championId,
                    String key,
                    String name) {
        this.listId = listId;
        this.title = title;
        this.championId = championId;
        this.key = key;
        this.name = name;
    }

    public Champion(int championTableId,
                    int listId,
                    String title,
                    String championId,
                    String key,
                    String name) {
        this.championTableId = championTableId;
        this.listId = listId;
        this.title = title;
        this.championId = championId;
        this.key = key;
        this.name = name;
    }

    public static ArrayList<Champion> fromJson(JSONObject jsonObject) {
        ArrayList<Champion> champions = new ArrayList<>(jsonObject.length());
        JSONObject championObject;

        Iterator<String> keys = jsonObject.keys();

        while( keys.hasNext() ) {
            String key = (String) keys.next();
            try {
                championObject = jsonObject.getJSONObject(key);
            } catch (Exception e) {
                Log.e("Error", e.toString());
                continue;
            }

            Champion champion = Champion.fromJsonHelper(championObject);
            if (champion != null) {
                champions.add(champion);
            }
        }

        return champions;
    }

    public static Champion fromJsonHelper(JSONObject json) {
        Champion champion = new Champion();
        try {
            champion.setTitle(json.getString("title"));
            champion.setChampionId(json.getString("id"));
            champion.setKey(json.getString("key"));
            champion.setName(json.getString("name"));
        } catch (JSONException e) {
            Log.e("Error", e.toString());

            return null;
        }

        return champion;
    }

    public int getChampionTableId() {
        return championTableId;
    }

    public void setChampionTableId(int championTableId) {
        this.championTableId = championTableId;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChampionId() {
        return championId;
    }

    public void setChampionId(String championId) {
        this.championId = championId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
