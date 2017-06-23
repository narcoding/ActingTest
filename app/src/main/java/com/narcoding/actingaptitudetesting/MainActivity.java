package com.narcoding.actingaptitudetesting;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import junit.framework.Test;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    Button btn_basla;
    LinearLayout llmain;

    double happiness, sadness, surprise, fear, anger, neutral, contempt, disgust=0.0;

    String savedname="actingtest";

    String[] emos;

    private void init(){
        textView= (TextView) findViewById(R.id.textView);
        btn_basla= (Button) findViewById(R.id.btn_basla);
        llmain= (LinearLayout) findViewById(R.id.llmain);
        emos= new String[]{
                getString(R.string.happiness)
                , getString(R.string.sadness)
                , getString(R.string.surprise)
                , getString(R.string.fear)
                , getString(R.string.anger)
                , getString(R.string.neutral)
                , getString(R.string.contempt)
                , getString(R.string.disgust)
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        for (int t=0;t<emos.length;t++){
            ImageView img = new ImageView(this);
            if(getImages(t)!=null) {
                textView.setVisibility(View.GONE);
                img.setImageBitmap(getImages(t));
                llmain.addView(img);
            }
        }


        double ortalama=getIntent().getDoubleExtra("ortalama",0);
        happiness=getIntent().getDoubleExtra("happiness",0);
        sadness=getIntent().getDoubleExtra("sadness",0);
        surprise=getIntent().getDoubleExtra("surprise",0);
        fear=getIntent().getDoubleExtra("fear",0);
        anger=getIntent().getDoubleExtra("anger",0);
        neutral=getIntent().getDoubleExtra("neutral",0);
        contempt=getIntent().getDoubleExtra("contempt",0);
        disgust=getIntent().getDoubleExtra("disgust",0);

        if(happiness>1.0)
            happiness=1;
        if(sadness>1.0)
            sadness=1;
        if(surprise>1.0)
            surprise=1;
        if(fear>1.0)
            fear=1;
        if(anger>1.0)
            anger=1;
        if(neutral>1.0)
            neutral=1;
        if(contempt>1.0)
            contempt=1;
        if(disgust>1.0)
            disgust=1;

        happiness=happiness*100;
        sadness=sadness*100;
        surprise=surprise*100;
        fear=fear*100;
        anger=anger*100;
        neutral=neutral*100;
        contempt=contempt*100;
        disgust=disgust*100;

        if (ortalama!=0.0){
            if(ortalama>80) {
                textView.setText(getString(R.string.pekiyi)+Math.floor(ortalama)+"\n \n"+
                        getString(R.string.happiness)+": %"+Math.floor(happiness)+"\n"+
                        getString(R.string.sadness)+": %"+Math.floor(sadness)+"\n"+
                        getString(R.string.surprise)+": %"+Math.floor(surprise)+"\n"+
                        getString(R.string.fear)+": %"+Math.floor(fear)+"\n"+
                        getString(R.string.anger)+": %"+Math.floor(anger)+"\n"+
                        getString(R.string.neutral)+": %"+Math.floor(neutral)+"\n"+
                        getString(R.string.contempt)+": %"+Math.floor(contempt)+"\n"+
                        getString(R.string.disgust)+": %"+Math.floor(disgust)+"\n"
                );
            }else if (ortalama>60){
                textView.setText(getString(R.string.iyi)+Math.floor(ortalama)+"\n \n"+
                        getString(R.string.happiness)+": %"+Math.floor(happiness)+"\n"+
                        getString(R.string.sadness)+": %"+Math.floor(sadness)+"\n"+
                        getString(R.string.surprise)+": %"+Math.floor(surprise)+"\n"+
                        getString(R.string.fear)+": %"+Math.floor(fear)+"\n"+
                        getString(R.string.anger)+": %"+Math.floor(anger)+"\n"+
                        getString(R.string.neutral)+": %"+Math.floor(neutral)+"\n"+
                        getString(R.string.contempt)+": %"+Math.floor(contempt)+"\n"+
                        getString(R.string.disgust)+": %"+Math.floor(disgust)+"\n"
                );
            }else if (ortalama>40){
                textView.setText(getString(R.string.orta)+Math.floor(ortalama)+"\n \n"+
                        getString(R.string.happiness)+": %"+Math.floor(happiness)+"\n"+
                        getString(R.string.sadness)+": %"+Math.floor(sadness)+"\n"+
                        getString(R.string.surprise)+": %"+Math.floor(surprise)+"\n"+
                        getString(R.string.fear)+": %"+Math.floor(fear)+"\n"+
                        getString(R.string.anger)+": %"+Math.floor(anger)+"\n"+
                        getString(R.string.neutral)+": %"+Math.floor(neutral)+"\n"+
                        getString(R.string.contempt)+": %"+Math.floor(contempt)+"\n"+
                        getString(R.string.disgust)+": %"+Math.floor(disgust)+"\n"
                );
            }else if (ortalama>20){
                textView.setText(getString(R.string.zayif)+Math.floor(ortalama)+"\n \n"+
                        getString(R.string.happiness)+": %"+Math.floor(happiness)+"\n"+
                        getString(R.string.sadness)+": %"+Math.floor(sadness)+"\n"+
                        getString(R.string.surprise)+": %"+Math.floor(surprise)+"\n"+
                        getString(R.string.fear)+": %"+Math.floor(fear)+"\n"+
                        getString(R.string.anger)+": %"+Math.floor(anger)+"\n"+
                        getString(R.string.neutral)+": %"+Math.floor(neutral)+"\n"+
                        getString(R.string.contempt)+": %"+Math.floor(contempt)+"\n"+
                        getString(R.string.disgust)+": %"+Math.floor(disgust)+"\n"
                );
            }else if (ortalama>0){
                textView.setText(getString(R.string.kotu)+Math.floor(ortalama)+"\n \n"+
                        getString(R.string.happiness)+": %"+Math.floor(happiness)+"\n"+
                        getString(R.string.sadness)+": %"+Math.floor(sadness)+"\n"+
                        getString(R.string.surprise)+": %"+Math.floor(surprise)+"\n"+
                        getString(R.string.fear)+": %"+Math.floor(fear)+"\n"+
                        getString(R.string.anger)+": %"+Math.floor(anger)+"\n"+
                        getString(R.string.neutral)+": %"+Math.floor(neutral)+"\n"+
                        getString(R.string.contempt)+": %"+Math.floor(contempt)+"\n"+
                        getString(R.string.disgust)+": %"+Math.floor(disgust)+"\n"
                );
            }

            btn_basla.setText(R.string.tekrar);
        }

        btn_basla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this,TestingActivity.class));
                startActivity(new Intent(MainActivity.this,MakeTestActivity.class));

                //finish();
            }
        });

    }

    private Bitmap getImages(int k){
        File f = new File("/mnt/sdcard/"+savedname+emos[k].toLowerCase()+".jpg");
        Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
        return bmp;
    }

}