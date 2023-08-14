package com.example.funmath;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.funmath.Database.Database;

public class MainActivity extends AppCompatActivity {


    Database db;
    Button lButtonLvl0;
    Button lButtonLvl1;
    Button lButtonLvl2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db =new Database(this );
        lButtonLvl0 = (Button) findViewById(R.id.button);
        lButtonLvl1 = (Button) findViewById(R.id.button2);
        lButtonLvl2 = (Button) findViewById(R.id.button3);

        lButtonLvl0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Level0Activity
                Intent intent = new Intent(MainActivity.this, Level0Activity.class);
                startActivity(intent);
            }
        });

        lButtonLvl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Level1Activity
                Intent intent = new Intent(MainActivity.this, Level1Activity.class);
                startActivity(intent);
            }
        });

        lButtonLvl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Level2Activity
                Intent intent = new Intent(MainActivity.this, Level2Activity.class);
                startActivity(intent);
            }
        });

    }

        @Override
        public void onBackPressed() {
            // Display a dialog asking if the user wants to leave the app
            new AlertDialog.Builder(this)
                    .setMessage("Do you want to exit the Fun Math?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Exit the app
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();

    }


}