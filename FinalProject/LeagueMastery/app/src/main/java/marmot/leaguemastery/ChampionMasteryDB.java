package marmot.leaguemastery;

/*
 * Created by Holden on 7/18/2017.
 *
 */

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChampionMasteryDB {

    // database constants
    public static final String DB_NAME = "championMastery.db";
    public static final int    DB_VERSION = 1;

    // list table constants
    public static final String LIST_TABLE = "list";

    public static final String LIST_ID = "_id";
    public static final int    LIST_ID_COL = 0;

    public static final String LIST_NAME = "list_name";
    public static final int    LIST_NAME_COL = 1;

    // champion mastery table constants
    public static final String MASTERY_TABLE = "champion_mastery";

    public static final String MASTERY_ID = "_id";
    public static final int    MASTERY_ID_COL = 0;

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

    public static final String MASTERY_LAST_PLAY_TIME = "player_id";
    public static final int    MASTERY_LAST_PLAY_TIME_COL = 9;

    // CREATE and DROP TABLE statements
    public static final String CREATE_LIST_TABLE =
            "CREATE TABLE " + LIST_TABLE + " (" +
                    LIST_ID   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    LIST_NAME + " TEXT    NOT NULL UNIQUE);";

    public static final String CREATE_MASTERY_TABLE =
            "CREATE TABLE " + MASTERY_TABLE + " (" +
                    MASTERY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MASTERY_LIST_ID + " INTEGER NOT NULL, " +
                    MASTERY_CHEST_GRANTED + " TEXT, " +
                    MASTERY_CHAMPION_LEVEL + " INTEGER, " +
                    MASTERY_CHAMPION_POINTS + " INTEGER, " +
                    MASTERY_CHAMPION_ID + " INTEGER, " +
                    MASTERY_PLAYER_ID +" INTEGER, " +
                    MASTERY_POINTS_UNTIL_NEXT_LEVEL + " INTEGER, " +
                    MASTERY_POINTS_SINCE_LAST_LEVEL + " INTEGER, " +
                    MASTERY_LAST_PLAY_TIME + " INTEGER);";

    public static final String DROP_LIST_TABLE =
            "DROP TABLE IF EXISTS " + LIST_TABLE;

    public static final String DROP_MASTERY_TABLE =
            "DROP TABLE IF EXISTS " + MASTERY_TABLE;

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

            //TODO: Temporary Creation
            // insert location lists
            db.execSQL("INSERT INTO list VALUES (1, 'metalmarsh89')");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {

            Log.d("Task list", "Upgrading db from version "
                    + oldVersion + " to " + newVersion);

            db.execSQL(ChampionMasteryDB.DROP_LIST_TABLE);
            db.execSQL(ChampionMasteryDB.DROP_MASTERY_TABLE);
            onCreate(db);
        }
    }

    // database and database helper objects
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    // constructor
    public ChampionMasteryDB(Context context) {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    // private methods
    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }

    private void openWriteableDB() {
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB() {
        if (db != null)
            db.close();
    }

    // public methods
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
        if (cursor != null)
            cursor.close();
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
        if (cursor != null)
            cursor.close();
        this.closeDB();

        return list;
    }

    public ArrayList<ChampionMastery> getChampionMasteries(String listName) {
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
        String where = MASTERY_ID + "= ?";
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
                        cursor.getInt(MASTERY_ID_COL),
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

        String where = MASTERY_ID + "= ?";
        String[] whereArgs = { String.valueOf(mastery.getChampionMasteryId()) };

        this.openWriteableDB();
        int rowCount = db.update(MASTERY_TABLE, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    public int deleteMastery(long id) {
        String where = MASTERY_ID + "= ?";
        String[] whereArgs = { String.valueOf(id) };

        this.openWriteableDB();
        int rowCount = db.delete(MASTERY_TABLE, where, whereArgs);
        this.closeDB();

        return rowCount;
    }
}