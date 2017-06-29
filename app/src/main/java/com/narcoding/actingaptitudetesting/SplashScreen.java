package com.narcoding.actingaptitudetesting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.narcoding.actingaptitudetesting.View.MainActivity;
import com.narcoding.actingaptitudetesting.View.TestCamActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        WebView webView= (WebView) findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/webhtml.html");


        Thread thread = new Thread() {

            @Override
            public void run() {

                try {
                    synchronized (this) {
                        wait(2500);
                    }
                } catch (InterruptedException e) {

                } finally {
                    //finish();

                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), TestCamActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();


    }
}
