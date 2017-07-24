package marmot.leaguemastery;

/*
 * Created by Holden on 7/18/2017.
 *
 */

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChampionDB {

    // database constants

    public static final String DB_NAME = "champion.db";
    public static final int    DB_VERSION = 6;

    // list table constants

    public static final String LIST_TABLE = "list";

    public static final String LIST_ID = "_id";
    public static final int    LIST_ID_COL = 0;

    public static final String LIST_NAME = "list_name";
    public static final int    LIST_NAME_COL = 1;

    // champion mastery table constants

    public static final String MASTERY_TABLE = "champion_mastery";

    public static final String MASTERY_TABLE_ID = "_id";
    public static final int    MASTERY_TABLE_ID_COL = 0;

    public static final String MASTERY_LIST_ID = "list_id";
    public static final int    MASTERY_LIST_ID_COL = 1;

    public static final String MASTERY_CHEST_GRANTED = "chest_granted";
    public static final int    MASTERY_CHEST_GRANTED_COL = 2;

    public static final String MASTERY_CHAMPION_LEVEL = "champion_level";
    public static final int    MASTERY_CHAMPION_LEVEL_COL = 3;

    public static final String MASTERY_CHAMPION_POINTS = "champion_points";
    public static final int    MASTERY_CHAMPION_POINTS_COL = 4;

    public static final String MASTERY_CHAMPION_ID = "champion_id";
    public static final int    MASTERY_CHAMPION_ID_COL = 5;

    public static final String MASTERY_PLAYER_ID = "player_id";
    public static final int    MASTERY_PLAYER_ID_COL = 6;

    public static final String MASTERY_POINTS_UNTIL_NEXT_LEVEL = "champion_points_until_next_level";
    public static final int    MASTERY_POINTS_UNTIL_NEXT_LEVEL_COL = 7;

    public static final String MASTERY_POINTS_SINCE_LAST_LEVEL = "champion_points_since_last_level";
    public static final int    MASTERY_POINTS_SINCE_LAST_LEVEL_COL = 8;

    public static final String MASTERY_LAST_PLAY_TIME = "last_play_time";
    public static final int    MASTERY_LAST_PLAY_TIME_COL = 9;

    // summoner table constants

    public static final String SUMMONER_TABLE = "summoner";

    public static final String SUMMONER_TABLE_ID = "_id";
    public static final int    SUMMONER_TABLE_ID_COL = 0;

    public static final String SUMMONER_LIST_ID = "list_id";
    public static final int    SUMMONER_LIST_ID_COL = 1;

    public static final String SUMMONER_PROFILE_ICON_ID = "profile_icon_id";
    public static final int    SUMMONER_PROFILE_ICON_ID_COL = 2;

    public static final String SUMMONER_NAME = "name";
    public static final int    SUMMONER_NAME_COL = 3;

    public static final String SUMMONER_SUMMONER_LEVEL = "summoner_level";
    public static final int    SUMMONER_SUMMONER_LEVEL_COL = 4;

    public static final String SUMMONER_REVISION_DATE = "revision_date";
    public static final int    SUMMONER_REVISION_DATE_COL = 5;

    public static final String SUMMONER_SUMMONER_ID = "summoner_id";
    public static final int    SUMMONER_SUMMONER_ID_COL = 6;

    public static final String SUMMONER_ACCOUNT_ID = "account_id";
    public static final int    SUMMONER_ACCOUNT_ID_COL = 7;

    // champion table constants

    public static final String CHAMPION_TABLE = "champion";

    public static final String CHAMPION_ID = "_id";
    public static final int    CHAMPION_ID_COL = 0;

    public static final String CHAMPION_LIST_ID = "list_id";
    public static final int    CHAMPION_LIST_ID_COL = 1;

    public static final String CHAMPION_TITLE = "title";
    public static final int    CHAMPION_TITLE_COL = 2;

    public static final String CHAMPION_CHAMPION_ID = "champion_id";
    public static final int    CHAMPION_CHAMPION_ID_COL = 3;

    public static final String CHAMPION_KEY = "key";
    public static final int    CHAMPION_KEY_COL = 4;

    public static final String CHAMPION_NAME = "name";
    public static final int    CHAMPION_NAME_COL = 5;

    // CREATE and DROP TABLE statements
    public static final String CREATE_LIST_TABLE =
            "CREATE TABLE " + LIST_TABLE + " (" +
                    LIST_ID   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    LIST_NAME + " TEXT    NOT NULL UNIQUE);";

    public static final String CREATE_MASTERY_TABLE =
            "CREATE TABLE " + MASTERY_TABLE + " (" +
                    MASTERY_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MASTERY_LIST_ID + " INTEGER NOT NULL, " +
                    MASTERY_CHEST_GRANTED + " TEXT, " +
                    MASTERY_CHAMPION_LEVEL + " TEXT, " +
                    MASTERY_CHAMPION_POINTS + " TEXT, " +
                    MASTERY_CHAMPION_ID + " TEXT, " +
                    MASTERY_PLAYER_ID +" TEXT, " +
                    MASTERY_POINTS_UNTIL_NEXT_LEVEL + " TEXT, " +
                    MASTERY_POINTS_SINCE_LAST_LEVEL + " TEXT, " +
                    MASTERY_LAST_PLAY_TIME + " TEXT);";

    public static final String CREATE_SUMMONER_TABLE =
            "CREATE TABLE " + SUMMONER_TABLE + " (" +
                    SUMMONER_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SUMMONER_LIST_ID + " INTEGER NOT NULL, " +
                    SUMMONER_PROFILE_ICON_ID + " TEXT, " +
                    SUMMONER_NAME + " TEXT, " +
                    SUMMONER_SUMMONER_LEVEL + " TEXT, " +
                    SUMMONER_REVISION_DATE + " TEXT, " +
                    SUMMONER_SUMMONER_ID +" TEXT, " +
                    SUMMONER_ACCOUNT_ID + " TEXT);";

    public static final String CREATE_CHAMPION_TABLE =
            "CREATE TABLE " + CHAMPION_TABLE + " (" +
                    CHAMPION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CHAMPION_LIST_ID + " INTEGER NOT NULL, " +
                    CHAMPION_TITLE + " TEXT, " +
                    CHAMPION_KEY + " TEXT, " +
                    CHAMPION_CHAMPION_ID + " TEXT, " +
                    CHAMPION_NAME + " TEXT);";

    public static final String DROP_LIST_TABLE =
            "DROP TABLE IF EXISTS " + LIST_TABLE;

    public static final String DROP_MASTERY_TABLE =
            "DROP TABLE IF EXISTS " + MASTERY_TABLE;

    public static final String DROP_SUMMONER_TABLE =
            "DROP TABLE IF EXISTS " + SUMMONER_TABLE;

    public static final String DROP_CHAMPION_TABLE =
            "DROP TABLE IF EXISTS " + CHAMPION_TABLE;

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // create tables
            db.execSQL(CREATE_LIST_TABLE);
            db.execSQL(CREATE_MASTERY_TABLE);
            db.execSQL(CREATE_SUMMONER_TABLE);
            db.execSQL(CREATE_CHAMPION_TABLE);

            // TODO: Temporary Creation
            // insert location lists
            db.execSQL("INSERT INTO list VALUES (1, 'champion')");
            db.execSQL("INSERT INTO list VALUES (2, 'metalmarsh89')");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {

            Log.d("Task list", "Upgrading db from version "
                    + oldVersion + " to " + newVersion);

            db.execSQL(ChampionDB.DROP_LIST_TABLE);
            db.execSQL(ChampionDB.DROP_MASTERY_TABLE);
            db.execSQL(ChampionDB.DROP_SUMMONER_TABLE);
            db.execSQL(ChampionDB.DROP_CHAMPION_TABLE);
            onCreate(db);
        }
    }

    // database and database helper objects
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    // constructor
    public ChampionDB(Context context) {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    // private methods
    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }

    private void openWriteableDB() {
        db = dbHelper.getWritableDatabase();
    }

    public void closeDB() {
        if (db != null)
            db.close();
    }

    // public methods

    public int doesExist(String table, String column, String arg) {
        String query = "SELECT * FROM " + table + " WHERE " + column + " = ?";
        int tempId = -1;
        openReadableDB();
        Cursor cursor = db.rawQuery(query, new String[] {arg});

        try {
            if (cursor != null && cursor.moveToFirst()) {
                tempId = cursor.getInt(CHAMPION_ID_COL);
            }
        } catch (NullPointerException e) {
            Log.e("Error in Does Exist", e.getMessage());
        } finally {
            cursor.close();
            db.close();
        }

        return tempId;
    }

    public ArrayList<List> getLists() {
        ArrayList<List> lists = new ArrayList<List>();
        openReadableDB();
        Cursor cursor = db.query(LIST_TABLE,
                null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            List list = new List();
            list.setId(cursor.getInt(LIST_ID_COL));
            list.setName(cursor.getString(LIST_NAME_COL));

            lists.add(list);
        }
        if (cursor != null) {
            cursor.close();
        }
        closeDB();

        return lists;
    }

    public List getList(String name) {
        String where = LIST_NAME + "= ?";
        String[] whereArgs = { name };

        openReadableDB();
        Cursor cursor = db.query(LIST_TABLE, null,
                where, whereArgs, null, null, null);
        List list = null;
        cursor.moveToFirst();
        list = new List(cursor.getInt(LIST_ID_COL),
                cursor.getString(LIST_NAME_COL));
        if (cursor != null) {
            cursor.close();
        }
        this.closeDB();

        return list;
    }

    public ArrayList<ChampionMastery> getMasteries(String listName) {
        String where =
                MASTERY_LIST_ID + "= ?";
        int listID = getList(listName).getId();
        String[] whereArgs = { Integer.toString(listID) };

        this.openReadableDB();
        Cursor cursor = db.query(MASTERY_TABLE, null,
                where, whereArgs,
                null, null, null);
        ArrayList<ChampionMastery> masteries = new ArrayList<>();
        while (cursor.moveToNext()) {
            masteries.add(getMasteryFromCursor(cursor));
        }
        if (cursor != null) {
            cursor.close();
        }
        this.closeDB();

        return masteries;
    }

    public ChampionMastery getMastery(int id) {
        String where = MASTERY_TABLE_ID + "= ?";
        String[] whereArgs = { Integer.toString(id) };

        this.openReadableDB();
        Cursor cursor = db.query(MASTERY_TABLE,
                null, where, whereArgs, null, null, null);
        cursor.moveToFirst();
        ChampionMastery mastery = getMasteryFromCursor(cursor);
        if (cursor != null) {
            cursor.close();
        }
        this.closeDB();

        return mastery;
    }

    private static ChampionMastery getMasteryFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0){
            return null;
        }
        else {
            try {
                return new ChampionMastery(
                        cursor.getInt(MASTERY_TABLE_ID_COL),
                        cursor.getInt(MASTERY_LIST_ID_COL),
                        cursor.getString(MASTERY_CHEST_GRANTED_COL),
                        cursor.getString(MASTERY_CHAMPION_LEVEL_COL),
                        cursor.getString(MASTERY_CHAMPION_POINTS_COL),
                        cursor.getString(MASTERY_CHAMPION_ID_COL),
                        cursor.getString(MASTERY_PLAYER_ID_COL),
                        cursor.getString(MASTERY_POINTS_UNTIL_NEXT_LEVEL_COL),
                        cursor.getString(MASTERY_POINTS_SINCE_LAST_LEVEL_COL),
                        cursor.getString(MASTERY_LAST_PLAY_TIME_COL));
            }
            catch(Exception e) {
                return null;
            }
        }
    }

    public long insertMastery(ChampionMastery mastery) {
        int checkExistence;

        ContentValues cv = new ContentValues();
        cv.put(MASTERY_LIST_ID, mastery.getListId());
        cv.put(MASTERY_CHEST_GRANTED, mastery.getChestGranted());
        cv.put(MASTERY_CHAMPION_LEVEL, mastery.getChampionLevel());
        cv.put(MASTERY_CHAMPION_POINTS, mastery.getChampionPoints());
        cv.put(MASTERY_CHAMPION_ID, mastery.getChampionId());
        cv.put(MASTERY_PLAYER_ID, mastery.getPlayerId());
        cv.put(MASTERY_POINTS_UNTIL_NEXT_LEVEL, mastery.getChampionPointsUntilNextLevel());
        cv.put(MASTERY_POINTS_SINCE_LAST_LEVEL, mastery.getChampionPointsSinceLastLevel());
        cv.put(MASTERY_LAST_PLAY_TIME, mastery.getLastPlayTime());

        if ((checkExistence = doesExist(MASTERY_TABLE, MASTERY_CHAMPION_ID, mastery.getChampionId())) != -1) {
            mastery.setChampionMasteryId(checkExistence);
            return updateMastery(mastery);
        }

        this.openWriteableDB();
        long rowID = db.insert(MASTERY_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }

    public int updateMastery(ChampionMastery mastery) {
        ContentValues cv = new ContentValues();
        cv.put(MASTERY_LIST_ID, mastery.getListId());
        cv.put(MASTERY_CHEST_GRANTED, mastery.getChestGranted());
        cv.put(MASTERY_CHAMPION_LEVEL, mastery.getChampionLevel());
        cv.put(MASTERY_CHAMPION_POINTS, mastery.getChampionPoints());
        cv.put(MASTERY_CHAMPION_ID, mastery.getChampionId());
        cv.put(MASTERY_PLAYER_ID, mastery.getPlayerId());
        cv.put(MASTERY_POINTS_UNTIL_NEXT_LEVEL, mastery.getChampionPointsUntilNextLevel());
        cv.put(MASTERY_POINTS_SINCE_LAST_LEVEL, mastery.getChampionPointsSinceLastLevel());
        cv.put(MASTERY_LAST_PLAY_TIME, mastery.getLastPlayTime());

        String where = MASTERY_TABLE_ID + "= ?";
        String[] whereArgs = { String.valueOf(mastery.getChampionMasteryId()) };

        this.openWriteableDB();
        int rowCount = db.update(MASTERY_TABLE, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    public int deleteMastery(long id) {
        String where = MASTERY_TABLE_ID + "= ?";
        String[] whereArgs = { String.valueOf(id) };

        this.openWriteableDB();
        int rowCount = db.delete(MASTERY_TABLE, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    public Summoner getSummoner(int id) {
        String where = SUMMONER_TABLE_ID + "= ?";
        String[] whereArgs = { Integer.toString(id) };

        this.openReadableDB();
        Cursor cursor = db.query(SUMMONER_TABLE,
                null, where, whereArgs, null, null, null);
        cursor.moveToFirst();
        Summoner summoner = getSummonerFromCursor(cursor);
        if (cursor != null) {
            cursor.close();
        }
        this.closeDB();

        return summoner;
    }

    private static Summoner getSummonerFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0){
            return null;
        }
        else {
            try {
                return new Summoner(
                        cursor.getInt(SUMMONER_TABLE_ID_COL),
                        cursor.getInt(SUMMONER_LIST_ID_COL),
                        cursor.getString(SUMMONER_PROFILE_ICON_ID_COL),
                        cursor.getString(SUMMONER_NAME_COL),
                        cursor.getString(SUMMONER_SUMMONER_LEVEL_COL),
                        cursor.getString(SUMMONER_REVISION_DATE_COL),
                        cursor.getString(SUMMONER_SUMMONER_ID_COL),
                        cursor.getString(SUMMONER_ACCOUNT_ID_COL));
            }
            catch(Exception e) {
                return null;
            }
        }
    }

    public long insertSummoner(Summoner summoner) {
        int checkExistence;

        ContentValues cv = new ContentValues();
        cv.put(SUMMONER_LIST_ID, summoner.getListId());
        cv.put(SUMMONER_PROFILE_ICON_ID, summoner.getProfileIconId());
        cv.put(SUMMONER_NAME, summoner.getName());
        cv.put(SUMMONER_SUMMONER_LEVEL, summoner.getSummonerLevel());
        cv.put(SUMMONER_REVISION_DATE, summoner.getRevisionDate());
        cv.put(SUMMONER_SUMMONER_ID, summoner.getSummonerId());
        cv.put(SUMMONER_ACCOUNT_ID, summoner.getAccountId());

        if ((checkExistence = doesExist(SUMMONER_TABLE, SUMMONER_SUMMONER_ID, summoner.getSummonerId())) != -1) {
            summoner.setSummonerTableId(checkExistence);
            return updateSummoner(summoner);
        }

        this.openWriteableDB();
        long rowID = db.insert(SUMMONER_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }

    public int updateSummoner(Summoner summoner) {
        ContentValues cv = new ContentValues();
        cv.put(SUMMONER_LIST_ID, summoner.getListId());
        cv.put(SUMMONER_PROFILE_ICON_ID, summoner.getProfileIconId());
        cv.put(SUMMONER_NAME, summoner.getName());
        cv.put(SUMMONER_SUMMONER_LEVEL, summoner.getSummonerLevel());
        cv.put(SUMMONER_REVISION_DATE, summoner.getRevisionDate());
        cv.put(SUMMONER_SUMMONER_ID, summoner.getSummonerId());
        cv.put(SUMMONER_ACCOUNT_ID, summoner.getAccountId());

        String where = SUMMONER_TABLE_ID + "= ?";
        String[] whereArgs = { String.valueOf(summoner.getSummonerTableId()) };

        this.openWriteableDB();
        int rowCount = db.update(SUMMONER_TABLE, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    public int deleteSummoner(long id) {
        String where = SUMMONER_TABLE_ID + "= ?";
        String[] whereArgs = { String.valueOf(id) };

        this.openWriteableDB();
        int rowCount = db.delete(SUMMONER_TABLE, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    public ArrayList<Champion> getChampions(String listName) {
        String where =
                CHAMPION_LIST_ID + "= ?";
        int listID = getList(listName).getId();
        String[] whereArgs = { Integer.toString(listID) };

        this.openReadableDB();
        Cursor cursor = db.query(CHAMPION_TABLE, null,
                where, whereArgs,
                null, null, null);
        ArrayList<Champion> champions = new ArrayList<>();
        while (cursor.moveToNext()) {
            champions.add(getChampionFromCursor(cursor));
        }
        if (cursor != null) {
            cursor.close();
        }
        this.closeDB();

        return champions;
    }

    public Champion getChampion(int id) {
        String where = CHAMPION_ID + "= ?";
        String[] whereArgs = { Integer.toString(id) };

        this.openReadableDB();
        Cursor cursor = db.query(CHAMPION_TABLE,
                null, where, whereArgs, null, null, null);
        cursor.moveToFirst();
        Champion champion = getChampionFromCursor(cursor);
        if (cursor != null) {
            cursor.close();
        }
        this.closeDB();

        return champion;
    }

    private static Champion getChampionFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0){
            return null;
        }
        else {
            try {
                return new Champion(
                        cursor.getInt(CHAMPION_ID_COL),
                        cursor.getInt(CHAMPION_LIST_ID_COL),
                        cursor.getString(CHAMPION_TITLE_COL),
                        cursor.getString(CHAMPION_CHAMPION_ID_COL),
                        cursor.getString(CHAMPION_KEY_COL),
                        cursor.getString(CHAMPION_NAME_COL));
            }
            catch(Exception e) {
                return null;
            }
        }
    }

    public long insertChampion(Champion champion) {
        int checkExistence;

        ContentValues cv = new ContentValues();
        cv.put(CHAMPION_LIST_ID, champion.getListId());
        cv.put(CHAMPION_TITLE, champion.getTitle());
        cv.put(CHAMPION_CHAMPION_ID, champion.getChampionId());
        cv.put(CHAMPION_KEY, champion.getKey());
        cv.put(CHAMPION_NAME, champion.getName());

        if ((checkExistence = doesExist(CHAMPION_TABLE, CHAMPION_CHAMPION_ID, champion.getChampionId())) != -1) {
            champion.setChampionTableId(checkExistence);
            return updateChampion(champion);
        }

        this.openWriteableDB();
        long rowID = db.insert(CHAMPION_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }

    public int updateChampion(Champion champion) {
        ContentValues cv = new ContentValues();
        cv.put(CHAMPION_LIST_ID, champion.getListId());
        cv.put(CHAMPION_TITLE, champion.getTitle());
        cv.put(CHAMPION_CHAMPION_ID, champion.getChampionId());
        cv.put(CHAMPION_KEY, champion.getKey());
        cv.put(CHAMPION_NAME, champion.getName());

        String where = CHAMPION_ID + "= ?";
        String[] whereArgs = { String.valueOf(champion.getChampionTableId()) };

        this.openWriteableDB();
        int rowCount = db.update(CHAMPION_TABLE, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    public int deleteChampion(long id) {
        String where = CHAMPION_ID + "= ?";
        String[] whereArgs = { String.valueOf(id) };

        this.openWriteableDB();
        int rowCount = db.delete(CHAMPION_TABLE, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    public HashMap<ChampionMastery, Champion> getChampionsAndMasteries() {
        String query = "SELECT * FROM " + MASTERY_TABLE + " INNER JOIN " +
                CHAMPION_TABLE + " ON " +
                MASTERY_TABLE + "." + MASTERY_CHAMPION_ID + "=" +
                CHAMPION_TABLE + "." + CHAMPION_CHAMPION_ID + ";";

        HashMap<ChampionMastery, Champion> combined = new HashMap<>();

        this.openWriteableDB();
        Cursor cursor = db.rawQuery(query, null);

        int championIndex = cursor.getColumnIndex(CHAMPION_ID);
        int masteryIndex = cursor.getColumnIndex(MASTERY_TABLE_ID);

        while (cursor.moveToNext()) {
            Champion champion = getChampion(cursor.getInt(championIndex));
            ChampionMastery mastery = getMastery(cursor.getInt(masteryIndex));
            if (champion != null && mastery != null) {
                combined.put(mastery, champion);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        this.closeDB();

        return combined;
    }
}