package com.example.driveroutreach.ui.app_utility;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AppUtility {


  public static String getDate(){
       return new SimpleDateFormat("dd-MMMM-yyyy").format(Calendar.getInstance().getTime());
    }


    public static String getDateTime(){
        return new SimpleDateFormat("dd-MMMM-yyyy hh-mm-ss").format(Calendar.getInstance().getTime());
    }

    public static void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public static String getToday(){
        return  new SimpleDateFormat("EEEE").format(Calendar.getInstance().getTime());
    }

    public static String getTime(){
        return new SimpleDateFormat("hh:mm aa").format(Calendar.getInstance().getTime());
    }

    public static void hideSoftKeyboard(Context context, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
