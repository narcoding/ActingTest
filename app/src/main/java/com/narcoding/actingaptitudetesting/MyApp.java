package com.narcoding.actingaptitudetesting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.narcoding.actingaptitudetesting.Model.Emoge;
import com.narcoding.actingaptitudetesting.View.MainActivity;

import java.util.ArrayList;

/**
 * Created by Belgeler on 28.06.2017.
 */

public class MyApp {
    public static Context c;
    public static String[] emos;
    public static String[] emoslowercase;
    public static ArrayList<Emoge> emogesList=new ArrayList<>();

    public static final int REQUEST_PERMISSIONS = 20;

    public static String savedname="actingtest";

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

        for(int e=0;e<emoslowercase.length;e++){
            Emoge tempEmoge=new Emoge();
            tempEmoge.setEmotion(emos[e]);
            tempEmoge.setUrl(savedname+emoslowercase[e]);
            tempEmoge.setName(emoslowercase[e]);
            tempEmoge.setWidth(0);
            tempEmoge.setHeight(0);
            emogesList.add(tempEmoge);
        }

    }

    public static Bitmap rotate(Bitmap bm, int rotation) {
        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap bmOut = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            return bmOut;
        }
        return bm;
    }


}
