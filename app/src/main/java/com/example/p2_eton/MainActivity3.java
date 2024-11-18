package com.example.p2_eton;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity3 extends AppCompatActivity {

    private Spinner itemSpinner;
    private DatabaseOpenHelper dbHelper = null;
    private SQLiteDatabase db = null;
    Cursor cursor;

    private String name;
    private Float cost;
    private Integer stock;

    private Integer runningTotal;

    private HashMap<String, Integer> runningTotals = new HashMap<>();
    private HashMap<String, Float> itemCosts = new HashMap<>();
    List<String> orderList = new ArrayList<>();;
    ArrayAdapter<String> adapter;
    ListView listView;
    TextView totalField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main3);
        itemSpinner = findViewById(R.id.itemSpinner);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, orderList);
        listView = findViewById(R.id.mylist);
        listView.setAdapter(adapter);
        totalField = findViewById(R.id.totalField);
        dbHelper = new DatabaseOpenHelper(this);
        db = dbHelper.getReadableDatabase();

        cursor = dbHelper.readAll();
        if (cursor != null && cursor.getCount() > 0) {
            ArrayList<String> items = new ArrayList<>();
            while(cursor.moveToNext()) {
                String item_name = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.ITEM_NAME));
                items.add(item_name);
                Float item_cost = cursor.getFloat(cursor.getColumnIndex(DatabaseOpenHelper.COST));
                itemCosts.put(item_name, item_cost);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            itemSpinner.setAdapter(adapter);
        }
        else {
            Toast.makeText(this, "No items in inventory.", Toast.LENGTH_SHORT).show();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void clickAdd(View v) {
        EditText stockField = findViewById(R.id.stockField);
        String quantityStr = stockField.getText().toString();

        if (quantityStr.isEmpty()) {
            Toast.makeText(this, "Must specify a quantity to add/remove!", Toast.LENGTH_LONG).show();
            return;
        }

        int quantityToAdd  = Integer.parseInt(quantityStr);

        String selected = itemSpinner.getSelectedItem().toString();
        cursor = dbHelper.readAll();

        if (cursor != null && cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.ITEM_NAME));
                if (name.equals(selected)) {
                    cost = cursor.getFloat(cursor.getColumnIndex(DatabaseOpenHelper.COST));
                    stock = cursor.getInt(cursor.getColumnIndex(DatabaseOpenHelper.STOCK));
                    break;
                }
            }
        }
        else {
            Toast.makeText(this, "Item not found in DB", Toast.LENGTH_LONG).show();
            return;
        }

        if (runningTotals.containsKey(name)) {
            runningTotal = runningTotals.get(name);
            if (runningTotal + quantityToAdd > stock) {
                Toast.makeText(this, "Cannot order more items than in stock!", Toast.LENGTH_LONG).show();
            }
            else {
                int newTotal = runningTotal + quantityToAdd;
                runningTotals.put(name, newTotal);
            }
        }
        else {
            runningTotals.put(name, quantityToAdd);
        }
        orderList.clear();
        float grandTotal = 0;
        for(Map.Entry<String, Integer> entry : runningTotals.entrySet()) {
            String name = entry.getKey();
            int quantity = entry.getValue();
            float cost = itemCosts.get(name);
            float totalCost = quantity * cost;
            grandTotal += totalCost;
            orderList.add(entry.getKey() + " x" + entry.getValue() + " $" + String.format("%.2f", totalCost));
        }
        totalField.setText("Current Total: $" + String.format("%.2f", grandTotal));
        adapter.notifyDataSetChanged();
    }

    public void clickRemove(View v) {
        EditText stockField = findViewById(R.id.stockField);
        String quantityStr = stockField.getText().toString();

        if (quantityStr.isEmpty()) {
            Toast.makeText(this, "Must specify a quantity to add/remove!", Toast.LENGTH_LONG).show();
            return;
        }

        int quantityToRemove = Integer.parseInt(quantityStr);

        String selected = itemSpinner.getSelectedItem().toString();
        cursor = dbHelper.readAll();

        if (cursor != null && cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.ITEM_NAME));
                if (name.equals(selected)) {
                    cost = cursor.getFloat(cursor.getColumnIndex(DatabaseOpenHelper.COST));
                    stock = cursor.getInt(cursor.getColumnIndex(DatabaseOpenHelper.STOCK));
                    break;
                }
            }
        } else {
            Toast.makeText(this, "Item not found in DB", Toast.LENGTH_LONG).show();
            return;
        }

        if (runningTotals.containsKey(name)) {
            runningTotal = runningTotals.get(name);
            if (runningTotal - quantityToRemove < 0) {
                Toast.makeText(this, "Cannot remove more items than ordered!", Toast.LENGTH_LONG).show();
            } else {
                int newTotal = runningTotal - quantityToRemove;
                if (newTotal == 0) {
                    runningTotals.remove(name);
                } else {
                    runningTotals.put(name, newTotal);
                }
            }
        } else {
            Toast.makeText(this, "Item not in order!", Toast.LENGTH_LONG).show();
            return;
        }

        orderList.clear();
        float grandTotal = 0;
        for (Map.Entry<String, Integer> entry : runningTotals.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            float itemCost = itemCosts.get(itemName);
            float totalCost = quantity * itemCost;
            grandTotal += totalCost;
            orderList.add(itemName + " x" + quantity + " $" + String.format("%.2f", totalCost));
        }
        totalField.setText("Current Total: $" + String.format("%.2f", grandTotal));
        adapter.notifyDataSetChanged();
    }

    public void clickFinish(View v) {
        Integer id = -1;
        cursor = dbHelper.readAll();

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String itemName = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.ITEM_NAME));
                int currentStock = cursor.getInt(cursor.getColumnIndex(DatabaseOpenHelper.STOCK));

                if (runningTotals.containsKey(itemName)) {
                    int orderedQuantity = runningTotals.get(itemName);
                    int newStock = currentStock - orderedQuantity;

                    Cursor cursor2 = db.query(
                            DatabaseOpenHelper.TABLE_NAME,
                            new String[]{DatabaseOpenHelper._ID},
                            DatabaseOpenHelper.ITEM_NAME + "=?",
                            new String[]{itemName},
                            null,
                            null,
                            null
                    );

                    if (cursor2 != null) {
                        if (cursor2.moveToFirst()) {
                            id = cursor2.getInt(cursor.getColumnIndex(DatabaseOpenHelper._ID));
                        }
                        cursor2.close();
                    }
                    else {
                        return;
                    }

                    dbHelper.updateStock(id.toString(), newStock) ;
                }
            }
        }

        runningTotals.clear();
        orderList.clear();
        adapter.notifyDataSetChanged();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}