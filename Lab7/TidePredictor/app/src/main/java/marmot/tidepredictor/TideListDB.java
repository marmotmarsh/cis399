package marmot.tidepredictor;

/*
 * Created by Holden on 7/16/2017.
 *
 */

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TideListDB {

    // database constants
    public static final String DB_NAME = "tideItem.db";
    public static final int    DB_VERSION = 1;

    // list table constants
    public static final String LIST_TABLE = "list";

    public static final String LIST_ID = "_id";
    public static final int    LIST_ID_COL = 0;

    public static final String LIST_NAME = "list_name";
    public static final int    LIST_NAME_COL = 1;

    // tide table constants
    public static final String TIDE_TABLE = "tide_item";

    public static final String TIDE_ID = "_id";
    public static final int    TIDE_ID_COL = 0;

    public static final String TIDE_LIST_ID = "list_id";
    public static final int    TIDE_LIST_ID_COL = 1;

    public static final String TIDE_DATE = "date";
    public static final int    TIDE_DATE_COL = 2;

    public static final String TIDE_DAY = "day";
    public static final int    TIDE_DAY_COL = 3;

    public static final String TIDE_TIME = "time";
    public static final int    TIDE_TIME_COL = 4;

    public static final String TIDE_PRED_CM = "pred_cm";
    public static final int    TIDE_PRED_CM_COL = 5;

    public static final String TIDE_TYPE = "type";
    public static final int    TIDE_TYPE_COL = 6;

    // CREATE and DROP TABLE statements
    public static final String CREATE_LIST_TABLE =
            "CREATE TABLE " + LIST_TABLE + " (" +
                    LIST_ID   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    LIST_NAME + " TEXT    NOT NULL UNIQUE);";

    public static final String CREATE_TIDE_TABLE =
            "CREATE TABLE " + TIDE_TABLE + " (" +
                    TIDE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TIDE_LIST_ID + " INTEGER NOT NULL, " +
                    TIDE_DATE + " TEXT, " +
                    TIDE_DAY + " TEXT, " +
                    TIDE_TIME + " TEXT, " +
                    TIDE_PRED_CM + " TEXT, " +
                    TIDE_TYPE + " TEXT);";

    public static final String DROP_LIST_TABLE =
            "DROP TABLE IF EXISTS " + LIST_TABLE;

    public static final String DROP_TIDE_TABLE =
            "DROP TABLE IF EXISTS " + TIDE_TABLE;

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // create tables
            db.execSQL(CREATE_LIST_TABLE);
            db.execSQL(CREATE_TIDE_TABLE);

            // insert location lists
            db.execSQL("INSERT INTO list VALUES (1, 'Coos Bay')");
            db.execSQL("INSERT INTO list VALUES (2, 'Florence')");
            db.execSQL("INSERT INTO list VALUES (3, 'Gold Beach')");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {

            Log.d("Task list", "Upgrading db from version "
                    + oldVersion + " to " + newVersion);

            db.execSQL(TideListDB.DROP_LIST_TABLE);
            db.execSQL(TideListDB.DROP_TIDE_TABLE);
            onCreate(db);
        }
    }

    // database and database helper objects
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    // constructor
    public TideListDB(Context context) {
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

    public ArrayList<TideItem> getTideItems(String listName) {
        String where =
                TIDE_LIST_ID + "= ?";
        int listID = getList(listName).getId();
        String[] whereArgs = { Integer.toString(listID) };

        this.openReadableDB();
        Cursor cursor = db.query(TIDE_TABLE, null,
                where, whereArgs,
                null, null, null);
        ArrayList<TideItem> tides = new ArrayList<>();
        while (cursor.moveToNext()) {
            tides.add(getTideFromCursor(cursor));
        }
        if (cursor != null) {
            cursor.close();
        }
        this.closeDB();

        return tides;
    }

    public TideItem getTideItem(int id) {
        String where = TIDE_ID + "= ?";
        String[] whereArgs = { Integer.toString(id) };

        this.openReadableDB();
        Cursor cursor = db.query(TIDE_TABLE,
                null, where, whereArgs, null, null, null);
        cursor.moveToFirst();
        TideItem tide = getTideFromCursor(cursor);
        if (cursor != null) {
            cursor.close();
        }
        this.closeDB();

        return tide;
    }

    private static TideItem getTideFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0){
            return null;
        }
        else {
            try {
                return new TideItem(
                        cursor.getInt(TIDE_ID_COL),
                        cursor.getInt(TIDE_LIST_ID_COL),
                        cursor.getString(TIDE_DATE_COL),
                        cursor.getString(TIDE_DAY_COL),
                        cursor.getString(TIDE_TIME_COL),
                        cursor.getString(TIDE_PRED_CM_COL),
                        cursor.getString(TIDE_TYPE_COL));
            }
            catch(Exception e) {
                return null;
            }
        }
    }

    public long insertTideItem(TideItem tide) {
        ContentValues cv = new ContentValues();
        cv.put(TIDE_LIST_ID, tide.getListId());
        cv.put(TIDE_DATE, tide.getDate());
        cv.put(TIDE_DAY, tide.getDay());
        cv.put(TIDE_TIME, tide.getTime());
        cv.put(TIDE_PRED_CM, tide.getPredCm());
        cv.put(TIDE_TYPE, tide.getType());

        this.openWriteableDB();
        long rowID = db.insert(TIDE_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }

    public int updateTideItem(TideItem tide) {
        ContentValues cv = new ContentValues();
        cv.put(TIDE_LIST_ID, tide.getListId());
        cv.put(TIDE_DATE, tide.getDate());
        cv.put(TIDE_DAY, tide.getDay());
        cv.put(TIDE_TIME, tide.getTime());
        cv.put(TIDE_PRED_CM, tide.getPredCm());
        cv.put(TIDE_TYPE, tide.getType());

        String where = TIDE_ID + "= ?";
        String[] whereArgs = { String.valueOf(tide.getTideId()) };

        this.openWriteableDB();
        int rowCount = db.update(TIDE_TABLE, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    public int deleteTideItem(long id) {
        String where = TIDE_ID + "= ?";
        String[] whereArgs = { String.valueOf(id) };

        this.openWriteableDB();
        int rowCount = db.delete(TIDE_TABLE, where, whereArgs);
        this.closeDB();

        return rowCount;
    }
}