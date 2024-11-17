package com.example.p2_eton;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    final static String TABLE_NAME = "inventory";
    final static String ITEM_NAME = "name";
    final static String COST = "cost";
    final static String STOCK = "stock";
    final static String DESCRIPTION = "description";
    final static String _ID = "_id";
    final private static String NAME = "inventory_db";
    final private static Integer VERSION = 1;
    final private Context context;
    final static String[] allColumns = {_ID, ITEM_NAME, STOCK, COST, DESCRIPTION};

    public DatabaseOpenHelper(Context context) {
        super(context, NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table with the new columns
        String CREATE_CMD = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ITEM_NAME + " TEXT NOT NULL, " +
                COST + " REAL NOT NULL, " +
                STOCK + " INTEGER NOT NULL, " +
                DESCRIPTION + " TEXT)";
        db.execSQL(CREATE_CMD);

        ContentValues values = new ContentValues();

        values.put(ITEM_NAME, "Chocolate Delight");
        values.put(COST, 2.99);
        values.put(STOCK, 25);
        db.insert(TABLE_NAME, null, values);
        values.clear();

        values.put(ITEM_NAME, "Melty Mints");
        values.put(COST, 1.99);
        values.put(STOCK, 105);
        values.put(DESCRIPTION, "Mint and chocolate drops sure to please.");
        db.insert(TABLE_NAME, null, values);
        values.clear();

        values.put(ITEM_NAME, "Peanut Butter Treasure");
        values.put(COST, 3.99);
        values.put(STOCK, 5);
        db.insert(TABLE_NAME, null, values);
        values.clear();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        // Handle schema upgrade (drop the old table and recreate)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // add item to table
    public void insert(String name, double cost, int stock, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ITEM_NAME, name);
        values.put(COST, cost);
        values.put(STOCK, stock);
        values.put(DESCRIPTION, description);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // delete item by name
    public void delete(String itemName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ITEM_NAME + "=?", new String[]{itemName});
        db.close();
    }

    // update item by name
    public void update(int id, String newName, double newCost, int newStock, String newDescription) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ITEM_NAME, newName);
        values.put(COST, newCost);
        values.put(STOCK, newStock);
        values.put(DESCRIPTION, newDescription);

        db.update(TABLE_NAME, values, _ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public Cursor readAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, allColumns, null, null, null, null, null);
    }
}

