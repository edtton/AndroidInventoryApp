package com.example.p2_eton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickStartApp(View v) {

        if (v.getId() == R.id.buttonManageInventory) {
            Intent intent = new Intent(this, MainActivity2.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.buttonHandleOrder) {
            Intent intent = new Intent(this, MainActivity3.class);
            startActivity(intent);
        }
    }
}