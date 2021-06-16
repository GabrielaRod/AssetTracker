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

public class Alert extends AppCompatActivity {

    Button buttonSubmit, buttonCancel;
    Spinner tagSpinner;
    ArrayList<String> tagList;
    ArrayList<String> vehicleInfo;
    ArrayAdapter<String> tagidAdapter;
    RequestQueue requestQueue;
    String URL, sessionemail, tagChoice, idvehicle;
    SessionManager sessionManager;
    TextView VIN, Make, Model, Color;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        /*Initialize Spinner (Drop Down Menu's)*/
        tagSpinner = findViewById(R.id.tags_spinner);

        /*Initialize the Request Queue*/
        requestQueue = Volley.newRequestQueue(this);

        /*Initializing SessionManager and Get Email*/
        sessionManager = new SessionManager(getApplicationContext());
        sessionemail = sessionManager.getEmail();

        /*URL Where I get the tags from*/
        URL = "http://10.0.0.14/LoginRegister/gettag.php";



        /*Initialize the TextViewer*/
        VIN = findViewById(R.id.textViewVehicleVIN);
        Make = findViewById(R.id.textViewVehicleMake);
        Model = findViewById(R.id.textViewVehicleModel);
        Color = findViewById(R.id.textViewVehicleColor);


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
                String vin, make, model, color, status, email;
                vin = String.valueOf(VIN.getText());
                make = String.valueOf(Make.getText());
                model = String.valueOf(Model.getText());
                color = String.valueOf(Color.getText());
                status = "ACTIVE";
                email = sessionemail;


                /*Error handling, if the fields are empty it will show Toast*/
                if(!vin.equals("") && !make.equals("") && !model.equals("") && !color.equals("") && !email.equals("")){
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[6];
                            field[0] = "VIN";
                            field[1] = "Make";
                            field[2] = "Model";
                            field[3] = "Color";
                            field[4] = "Status";
                            field[5] = "Email";


                            //Creating array for data
                            String[] data = new String[6];
                            data[0] = vin;
                            data[1] = make;
                            data[2] = model;
                            data[3] = color;
                            data[4] = status;
                            data[5] = email;

                            PutData putData = new PutData("http://10.0.0.14/LoginRegister/alert.php", "POST", field, data); //If using a mobil device it will be visible if they are connected to the same network
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    if(result.equals("Registration Successful")){
                                        Intent intent = new Intent(getApplicationContext(), MainActivity2.class); //If success it will take you to MainActivity2
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }

                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    /* GETS THE DATA FOR THE SPINNER AND RETRIEVE DATA ASSOCIATED TO THE TAG TO DISPLAY */
    public void getData() throws JSONException {
        /*Create ArrayList to store vehicle_id to later get vehicle data from DB Query*/
        ArrayList<String> vehicleid = new ArrayList<>();
        ArrayList<String> tagList = new ArrayList<>();

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
                                String tag = jsonObject.optString("Tag");
                                String vehicle = jsonObject.optString("Vehicle_Id");
                                tagList.add(tag);
                                vehicleid.add(vehicle);
                                tagidAdapter = new ArrayAdapter<>(Alert.this, android.R.layout.simple_spinner_item, tagList);
                                tagidAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                tagSpinner.setAdapter(tagidAdapter);
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



        tagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tagChoice = parent.getItemAtPosition(position).toString();

                if (tagList.contains(tagChoice)) {
                    int choice = tagList.indexOf(tagChoice);
                    idvehicle = vehicleid.get(choice);
                } else {
                    Toast.makeText(getApplicationContext(), "The tag does not exist", Toast.LENGTH_SHORT).show();
                }

                String url = "http://10.0.0.14/LoginRegister/getvehicleinfo.php";

                /*To get the JSON Data from the Query*/
                StringRequest strRequest2 = new StringRequest(Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONArray jsonArray2 = null;

                                try {
                                    jsonArray2 = new JSONArray(response);
                                    for (int i = 0; i < jsonArray2.length(); i++) {
                                        JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                                        String vin = jsonObject2.optString("VIN");
                                        String make = jsonObject2.optString("Make");
                                        String model = jsonObject2.optString("Model");
                                        String color = jsonObject2.optString("Color");
                                        VIN.setText(vin);
                                        Make.setText(make);
                                        Model.setText(model );
                                        Color.setText(color);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        /*To add parameters & values for the request*/
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("VehicleId", idvehicle);
                        return params;
                    }
                };
                requestQueue.add(strRequest2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "No Data to retrieve", Toast.LENGTH_SHORT).show();
            }
        });
    }
}