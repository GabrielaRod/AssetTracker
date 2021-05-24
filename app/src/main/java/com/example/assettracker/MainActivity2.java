package com.example.assettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity2 extends AppCompatActivity {

    Button buttonRegister, buttonCreateAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        /*On click listener for the Cancel button in case the user wants to cancel registration*/
        buttonRegister = findViewById(R.id.buttonasset);
        buttonCreateAlert = findViewById(R.id.buttoncreatealert);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AssetRegistration.class);
                startActivity(intent);
                finish();
            }
        });

        buttonCreateAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Alert.class);
                startActivity(intent);
                finish();
            }
        });

        }
}
