package com.example.assettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.FetchData;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Alert extends AppCompatActivity {

    Button buttonSubmit, buttonCancel;
    Spinner tags;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        /*Spinner (Drop Down Menu's)*/
        tags = findViewById(R.id.tags_spinner);

        /*On click listener for the Cancel button in case the user wants to cancel registration*/
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
                finish();
                }
            }

            /*On click listener for the submit button*/
            buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /*Getting the data from the input variables*/


                    /*Error handling, if the fields are empty it will show Toast*/
                    if(!cartype.equals("") && !tagid.equals("")){

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        FetchData fetchData = new FetchData("http://10.0.0.14/LoginRegister/gettag.php");
                                        if (fetchData.startFetch()) {
                                            if (fetchData.onComplete()) {
                                                String result = fetchData.getResult();
                                                Log.i("FetchData", result);
                                            }
                                        }
                                    }
                                });
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                    }
                }

        });
    }
}