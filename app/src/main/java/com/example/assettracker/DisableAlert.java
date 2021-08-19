package com.example.assettracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DisableAlert extends AppCompatActivity {

    Button buttonSubmit, buttonCancel;
    Spinner vinspinner;
    ArrayList<String> alertslist, makelist, modellist, colorlist;
    ArrayAdapter<String> alertAdapter;
    String URL, sessionemail, vinChoice, makeindex, modelindex, colorindex;
    RequestQueue requestQueue;
    SessionManager sessionManager;
    TextView Make, Model, Color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disable_alert);

        /*Initialize Spinner (Drop Down Menu's)*/
        vinspinner = findViewById(R.id.alerts_spinner);

        /*Initialize the Request Queue*/
        requestQueue = Volley.newRequestQueue(this);

        /*Initializing SessionManager and Get Email*/
        sessionManager = new SessionManager(getApplicationContext());
        sessionemail = sessionManager.getEmail();

        /*URL Where I get the tags from*/
        URL = "http://10.0.0.14/LoginRegister/getalert.php";

        /*Initialize the TextViewer*/
        Make = findViewById(R.id.textViewVehicleMake_disable);
        Model = findViewById(R.id.textViewVehicleModel_disable);
        Color = findViewById(R.id.textViewVehicleColor_disable);

        /*Method to fetch Data from the URL*/
        try {
            getData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*Initialize buttons*/
        buttonSubmit = findViewById(R.id.submit);
        buttonCancel = findViewById(R.id.cancel);


        /*On click listener for the Cancel button in case the user wants to cancel and go back the main activity*/
        buttonCancel.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
            startActivity(intent);
            finish();
        });

        /*On click listener for the submit button*/
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    /*Getting the data from the input variables*/
                    String vin, email;
                    vin = String.valueOf(vinChoice);
                    email = sessionemail;


                    /*Error handling, if the fields are empty it will show Toast*/
                    if (!vin.equals("") && !email.equals("")) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //Starting Write and Read data with URL
                                //Creating array for parameters
                                String[] field = new String[2];
                                field[0] = "Email";
                                field[1] = "VIN";


                                //Creating array for data
                                String[] data = new String[2];
                                data[0] = email;
                                data[1] = vin;

                                PutData putData = new PutData("http://10.0.0.14/LoginRegister/disable.php", "POST", field, data); //If using a mobil device it will be visible if they are connected to the same network
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        String result = putData.getResult();
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        if (result.equals("Alert Disabled Successful")) {
                                            Intent intent = new Intent(getApplicationContext(), MainActivity2.class); //If success it will take you to MainActivity2
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }
                                //End Write and Read data with URL
                            }

                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
            startActivity(intent);
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }



    /* GETS THE DATA FOR THE SPINNER AND RETRIEVE DATA ASSOCIATED TO THE TAG TO DISPLAY */
    public void getData() throws JSONException {
        /*Create ArrayList to store vehicle_id to later get vehicle data from DB Query*/
        alertslist = new ArrayList<>();
        makelist = new ArrayList<>();
        modellist = new ArrayList<>();
        colorlist = new ArrayList<>();

        /*To get the JSON Data from the Query*/
        StringRequest strRequest = new StringRequest(Request.Method.POST,
                URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        JSONArray jsonArray = null;

                        try {
                            jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String vin = jsonObject.optString("VIN");
                                String make = jsonObject.optString("Make");
                                String model = jsonObject.optString("Model");
                                String color = jsonObject.optString("Color");
                                alertslist.add(vin);
                                makelist.add(make);
                                modellist.add(model);
                                colorlist.add(color);
                                alertAdapter = new ArrayAdapter<>(DisableAlert.this, android.R.layout.simple_spinner_item, alertslist);
                                alertAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                vinspinner.setAdapter(alertAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                /*To add parameters & values for the request*/
                Map<String, String> params = new HashMap<String, String>();
                params.put("Email", sessionemail);
                return params;
            }
        };

        requestQueue.add(strRequest);



        vinspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vinChoice = parent.getItemAtPosition(position).toString();

                if (alertslist.contains(vinChoice)) {
                    int choice = alertslist.indexOf(vinChoice);
                    makeindex = makelist.get(choice);
                    modelindex = modellist.get(choice);
                    colorindex = colorlist.get(choice);
                } else {
                    Toast.makeText(getApplicationContext(), "The tag does not exist", Toast.LENGTH_SHORT).show();
                }

                    Make.setText(makeindex);
                    Model.setText(modelindex);
                    Color.setText(colorindex);
                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "No Data to retrieve", Toast.LENGTH_SHORT).show();
            }
        });

    }
}