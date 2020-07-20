package com.narcoding.actingaptitudetesting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.narcoding.actingaptitudetesting.View.MainActivity;
import com.narcoding.actingaptitudetesting.View.TestCamActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        WebView webView= findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/webhtml.html");

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), TestCamActivity.class);
                startActivity(intent);
                finish();
            }
        },2500);

    }
}
