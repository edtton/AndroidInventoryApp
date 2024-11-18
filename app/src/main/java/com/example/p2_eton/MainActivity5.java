package com.example.p2_eton;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity5 extends AppCompatActivity {
    private DatabaseOpenHelper dbHelper = null;
    SQLiteDatabase db = null;
    Cursor cursor = null;

    private String newName;
    private Float newCost;
    private Integer newStock;
    private String newDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main5);

        dbHelper = new DatabaseOpenHelper(this);
        db = dbHelper.getReadableDatabase();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void clickAdd(View v) {
        EditText nameField = findViewById(R.id.nameField);
        EditText costField = findViewById(R.id.costField);
        EditText stockField = findViewById(R.id.stockField);
        EditText descField = findViewById(R.id.descField);

        newName = nameField.getText().toString();
        if (newName.isEmpty()) {
            Toast.makeText(this, "Must have an item name!", Toast.LENGTH_SHORT).show();
            return;
        }
        cursor = db.query(DatabaseOpenHelper.TABLE_NAME,
                new String[]{"name"},
                "name = ?",
                new String[]{newName},
                null, null, null);
        if (cursor.getCount() > 0) {
            Toast.makeText(this, "Must have a unique item name!", Toast.LENGTH_SHORT).show();
            cursor.close();
            return;
        }

        String newCostStr = costField.getText().toString().trim();
        if (!newCostStr.isEmpty()) {
            newCost = Float.parseFloat(newCostStr);
            if (newCost < (float)0.00) {
                Toast.makeText(this, "Cost cannot be less than zero dollars!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else {
            Toast.makeText(this, "Must have a cost per item value!", Toast.LENGTH_SHORT).show();
            return;
        }

        String newStockStr = stockField.getText().toString().trim();
        if (!newStockStr.isEmpty()) {
            newStock = Integer.parseInt(newStockStr);
            if (newStock < 0) {
                Toast.makeText(this, "Stock cannot be less than zero!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else {
            Toast.makeText(this, "Must have a stock number value!", Toast.LENGTH_SHORT).show();
            return;
        }

        newDesc = descField.getText().toString();

        dbHelper.insert(newName, newCost, newStock, newDesc);

        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }
}