package com.example.p2_eton;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity2 extends AppCompatActivity {

    EditText elem;
    ListView listView;

    private SQLiteDatabase db = null;
    private DatabaseOpenHelper dbHelper = null;
    SimpleCursorAdapter scAdapter;
    Cursor mCursor;

    final static String _ID = "_id";
    final static String ITEM_NAME = "name";
    final static String ITEM_COST = "cost";
    final static String ITEM_STOCK = "stock";
    final static String ITEM_DESC = "description";
    final static String[] columns = {_ID, ITEM_NAME, ITEM_COST, ITEM_STOCK, ITEM_DESC};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        dbHelper = new DatabaseOpenHelper(this);
        listView = (ListView) findViewById(R.id.mylist);

        scAdapter = new SimpleCursorAdapter(this, R.layout.line, null, new String[]{ITEM_NAME, ITEM_COST, ITEM_STOCK}, new int[]{R.id.item_name, R.id.item_cost, R.id.item_stock}, 0);
        listView.setAdapter(scAdapter);

        scAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
                if (view.getId() == R.id.item_cost) {
                    double cost = cursor.getDouble(i);
                    ((TextView) view).setText("$" + String.format("%.2f", cost));
                    return true;
                }

                else if (view.getId() == R.id.item_stock) {
                    int stock = cursor.getInt(i);
                    ((TextView) view).setText(stock + " in stock");
                    return true;
                }

                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mCursor.moveToPosition(position)) {

                    String name = mCursor.getString(mCursor.getColumnIndex(ITEM_NAME));
                    String desc = mCursor.getString(mCursor.getColumnIndex(ITEM_DESC));

                    if (desc == null || desc.trim().isEmpty()) {
                        desc = "No description found for item.";
                    }

                    Snackbar.make(view, name + ": " + desc, Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show();
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity2.this, MainActivity4.class);

                    String name = mCursor.getString(mCursor.getColumnIndex(ITEM_NAME));
                    String desc = mCursor.getString(mCursor.getColumnIndex(ITEM_DESC));
                    String cost = mCursor.getString(mCursor.getColumnIndex(ITEM_COST));
                    String stock = mCursor.getString(mCursor.getColumnIndex(ITEM_STOCK));

                    intent.putExtra("name", name);
                    intent.putExtra("desc", desc);
                    intent.putExtra("cost", cost);
                    intent.putExtra("stock", stock);

                    startActivity(intent);

                    return true;
                }
            }
        );

//        elem =  (EditText)findViewById(R.id.input);
//        dbHelper = new DatabaseOpenHelper(this);
    }

    public void onResume() {
        super.onResume();
        db = dbHelper.getWritableDatabase();
        mCursor = dbHelper.readAll();
        scAdapter.swapCursor(mCursor);
    }
}