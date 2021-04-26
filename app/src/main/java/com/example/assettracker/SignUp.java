package com.example.assettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class SignUp extends AppCompatActivity {

    TextInputEditText textInputEditTextFirstname, textInputEditTextLastname, textInputEditTextDomID, textInputEditTextAddress, textInputEditTextCity, textInputEditTextPhonenumber,     textInputEditTextEmail, textInputEditTextPassword;
    Button buttonSignUp;
    TextView textViewLogin;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        /*To set the variables for the input data*/
        textInputEditTextFirstname = findViewById(R.id.firstname);
        textInputEditTextLastname = findViewById(R.id.lastname);
        textInputEditTextDomID = findViewById(R.id.id);
        textInputEditTextAddress = findViewById(R.id.address);
        textInputEditTextCity = findViewById(R.id.city);
        textInputEditTextPhonenumber = findViewById(R.id.phonenumber);
        textInputEditTextEmail = findViewById(R.id.email);
        textInputEditTextPassword = findViewById(R.id.password);

        buttonSignUp = findViewById(R.id.buttonSignUp);
        textViewLogin = findViewById(R.id.loginText);
        progressBar = findViewById(R.id.progress);

        /*On click listener for the Login button in case the user already has an account*/
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        /*On click listener for the sign up button*/
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Getting the data from the input variables*/
                String firstname, lastname, id, address, city, phonenumber, email, password;
                firstname = String.valueOf(textInputEditTextFirstname.getText());
                lastname = String.valueOf(textInputEditTextLastname.getText());
                id = String.valueOf(textInputEditTextDomID.getText());
                address = String.valueOf(textInputEditTextAddress.getText());
                city = String.valueOf(textInputEditTextCity.getText());
                phonenumber = String.valueOf(textInputEditTextPhonenumber.getText());
                email = String.valueOf(textInputEditTextEmail.getText());
                password = String.valueOf(textInputEditTextPassword.getText());

                /*Error handling, if the fields are empty it will show Toast*/
                if(!firstname.equals("") && !lastname.equals("") && !id.equals("") && !address.equals("") && !city.equals("") && !phonenumber.equals("") && !email.equals("") && !password.equals("")){

                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[8];
                            field[0] = "FirstName";
                            field[1] = "LastName";
                            field[2] = "DomID";
                            field[3] = "Address";
                            field[4] = "City";
                            field[5] = "PhoneNumber";
                            field[6] = "Email";
                            field[7] = "Password";

                            //Creating array for data
                            String[] data = new String[8];
                            data[0] = firstname;
                            data[1] = lastname;
                            data[2] = id;
                            data[3] = address;
                            data[4] = city;
                            data[5] = phonenumber;
                            data[6] = email;
                            data[7] = password;
                            PutData putData = new PutData("http://10.0.0.14/LoginRegister/signup.php", "POST", field, data); //If using a mobil device it will be visible if they are connected to the same network
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    if(result.equals("Sign Up Success")){
                                        Intent intent = new Intent(getApplicationContext(), Login.class); //If success it will take you to Login
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
}