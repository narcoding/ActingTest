package com.narcoding.actingaptitudetesting;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.narcoding.actingaptitudetesting.Model.Emoge;

import java.util.ArrayList;

/**
 * Created by Belgeler on 28.06.2017.
 */

public class MyApp extends Application {
    public static String[] emos;
    public static String[] emoslowercase;
    public static ArrayList<Emoge> emogesList;

    public static final int REQUEST_PERMISSIONS = 20;
    public static boolean isPermissionsGranted = false;

    public static String savedname = "actingtest";

    private static MyApp instance;

    public static MyApp getInstance() {
        if (instance == null) {
            synchronized (MyApp.class) {
                if (instance == null)
                    instance = new MyApp();
            }
        }
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        instance = getInstance();

        emos = new String[]{
                getResources().getString(R.string.happiness),
                getResources().getString(R.string.sadness),
                getResources().getString(R.string.surprise),
                getResources().getString(R.string.fear),
                getResources().getString(R.string.anger),
                getResources().getString(R.string.neutral),
                getResources().getString(R.string.contempt),
                getResources().getString(R.string.disgust)
        };

        emoslowercase = new String[]{
                getResources().getString(R.string.happinessLC),
                getResources().getString(R.string.sadnessLC),
                getResources().getString(R.string.surpriseLC),
                getResources().getString(R.string.fearLC),
                getResources().getString(R.string.angerLC),
                getResources().getString(R.string.neutralLC),
                getResources().getString(R.string.contemptLC),
                getResources().getString(R.string.disgustLC)
        };

        if (emogesList == null ){
            emogesList = new ArrayList<>();
        } else {
            emogesList.clear();
            emogesList = new ArrayList<>();
        }

        for (int e = 0; e < emoslowercase.length; e++) {
            Emoge tempEmoge = new Emoge();
            tempEmoge.setEmotion(emos[e]);
            tempEmoge.setUrl(savedname + emoslowercase[e]);
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
