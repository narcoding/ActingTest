package com.narcoding.actingaptitudetesting;

import android.content.Context;

import com.narcoding.actingaptitudetesting.View.MainActivity;

/**
 * Created by Belgeler on 28.06.2017.
 */

public class MyApp {
    public static Context c;
    public static String[] emos;
    public static String[] emoslowercase;

    public static void init(Context context) {
        MyApp.c = context;
        emos= new String[]{
                c.getResources().getString(R.string.happiness),
                c.getResources().getString(R.string.sadness),
                c.getResources().getString(R.string.surprise),
                c.getResources().getString(R.string.fear),
                c.getResources().getString(R.string.anger),
                c.getResources().getString(R.string.neutral),
                c.getResources().getString(R.string.contempt),
                c.getResources().getString(R.string.disgust)
        };

        emoslowercase= new String[]{
                c.getResources().getString(R.string.happinessLC),
                c.getResources().getString(R.string.sadnessLC),
                c.getResources().getString(R.string.surpriseLC),
                c.getResources().getString(R.string.fearLC),
                c.getResources().getString(R.string.angerLC),
                c.getResources().getString(R.string.neutralLC),
                c.getResources().getString(R.string.contemptLC),
                c.getResources().getString(R.string.disgustLC)
        };
    }



}
