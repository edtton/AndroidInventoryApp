package com.example.p2_eton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity4 extends AppCompatActivity {
    private String name;
    private String cost;
    private String stock;
    private String desc;

    private Float newCost;
    private Integer newStock;
    private String newDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main4);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView nameField = findViewById(R.id.nameField);
        EditText costField = findViewById(R.id.costField);
        EditText stockField = findViewById(R.id.stockField);
        EditText descField = findViewById(R.id.descField);

        if (savedInstanceState != null) {
            String savedCost = savedInstanceState.getString("savedCost");
            costField.setText(savedCost);

            String savedStock = savedInstanceState.getString("savedStock");
            stockField.setText(savedStock);

            String savedDesc = savedInstanceState.getString("savedDesc");
            descField.setText(savedDesc);
        }

        Intent intent = getIntent();

        name = intent.getStringExtra("name");
        cost = intent.getStringExtra("cost");
        stock = intent.getStringExtra("stock");
        desc = intent.getStringExtra("desc");

        if (desc == null || desc.trim().isEmpty()) {
            desc = "No description found for item.";
        }

        nameField.setText(name);
        costField.setHint(cost);
        stockField.setHint(stock);
        descField.setHint(desc);
    }

    public void clickUpdate(View v) {
        EditText costField = findViewById(R.id.costField);
        EditText stockField = findViewById(R.id.stockField);
        EditText descField = findViewById(R.id.descField);

        String newCostStr = costField.getText().toString().trim();
        if (!newCostStr.isEmpty()) {
            newCost = Float.parseFloat(newCostStr);
            if (newCost < (float)0.00) {
                Toast.makeText(this, "Cost cannot be less than zero dollars!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String newStockStr = stockField.getText().toString();
        if (!newStockStr.isEmpty()) {
            newStock = Integer.parseInt(newStockStr);
            if (newStock < 0) {
                Toast.makeText(this, "Stock cannot be less than zero!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String newDesc = descField.getText().toString();

        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }
}