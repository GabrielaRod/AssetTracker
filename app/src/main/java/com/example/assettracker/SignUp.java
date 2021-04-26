package com.example.assettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class SignUp extends AppCompatActivity {

    TextInputEditText textInputEditTextFirstname, textInputEditTextLastname, textInputEditTextDomID, textInputEditTextAddress, textInputEditTextCity, textInputEditTextPhonenumber,     textInputEditTextEmail, textInputEditTextPassword;
    Button buttonSignUp;
    TextView textViewLogin;

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
                    //Start ProgressBar first (Set visibility VISIBLE)
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[7];
                            field[0] = "firstname";
                            field[1] = "lastname";
                            field[2] = "id";
                            field[3] = "address";
                            field[4] = "city";
                            field[5] = "phonenumber";
                            field[6] = "email";
                            field[7] = "password";
                            //Creating array for data
                            String[] data = new String[7];
                            data[0] = firstname;
                            data[1] = lastname;
                            data[2] = id;
                            data[3] = address;
                            data[4] = city;
                            data[5] = phonenumber;
                            data[6] = email;
                            data[7] = password;
                            PutData putData = new PutData("https://projects.vishnusivadas.com/AdvancedHttpURLConnection/putDataTest.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    //End ProgressBar (Set visibility to GONE)
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