package com.example.assettracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    Button buttonRegister, buttonCreateAlert, buttonViewMap, buttonDisableAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        /*On click listener for the Cancel button in case the user wants to cancel registration*/
        buttonRegister = findViewById(R.id.buttonasset);
        buttonCreateAlert = findViewById(R.id.buttoncreatealert);
        buttonViewMap = findViewById(R.id.buttonViewMap);
        buttonDisableAlert = findViewById(R.id.buttonDisableAlert);

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

        buttonViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonDisableAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
                finish();
            }
        });

        }
}
