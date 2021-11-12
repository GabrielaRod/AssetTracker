package com.example.assettracker;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    //String ServerURL = "http://52.20.133.226/apprequest/";
    String ServerURL = "http://10.0.0.14/LoginRegister/";

    /*Constructor*/
    public SessionManager(Context context){
        sharedPreferences = context.getSharedPreferences("AppKey", 0);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    /*Create set login method*/
    public void setLogin(boolean login){
        editor.putBoolean("KEY_LOGIN", login);
        editor.commit();
    }

    /*Create get login method*/
    public boolean getLogin(){
        return sharedPreferences.getBoolean("KEY_LOGIN", false);
    }

    /*Create set email method*/
    public void setEmail(String email){
        editor.putString("KEY_EMAIL", email);
        editor.commit();
    }

    /*Create get email method*/
    public String getEmail(){
        return  sharedPreferences.getString("KEY_EMAIL", "");
    }
}
