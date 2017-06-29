package com.narcoding.actingaptitudetesting.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.narcoding.actingaptitudetesting.Model.MySurfaceCam;
import com.narcoding.actingaptitudetesting.R;

import static com.narcoding.actingaptitudetesting.MyApp.emos;

public class TestCamActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private static LinearLayout ll_sf;
    private static LinearLayout ll_takeselfie;
    private static LinearLayout ll_sure;
    public static SurfaceView sv_testcam;
    private static Button btn_go;
    private static Button btn_takeselfie;
    private static TextView txt_takeselfie;
    private static TextView txt_emo;
    private static TextView txt_sure;

    private static int sayi=0;

    private void init(){
        ll_sf= (LinearLayout) findViewById(R.id.ll_sf);
        ll_takeselfie= (LinearLayout) findViewById(R.id.ll_takeselfie);
        ll_sure= (LinearLayout) findViewById(R.id.ll_sure);
        sv_testcam= (SurfaceView) findViewById(R.id.sv_testcam);
        new MySurfaceCam(this,sv_testcam);

        btn_go= (Button) findViewById(R.id.btn_go);
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_sf.setVisibility(View.GONE);
                ll_takeselfie.setVisibility(View.VISIBLE);
            }
        });

        txt_takeselfie= (TextView) findViewById(R.id.txt_takeselfie);
        txt_takeselfie.setText(getText(R.string.takeselfie)+""+ getText(R.string.happiness));

        btn_takeselfie= (Button) findViewById(R.id.btn_takeselfie);
        btn_takeselfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_takeselfie.setVisibility(View.GONE);
                ll_sure.setVisibility(View.VISIBLE);
                switch (sayi){
                    case 0:
                        runThread(sayi);
                        break;
                    case 1:
                        runThread(sayi);
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;

                }

                //runThread(sayi);

            }
        });

        txt_emo= (TextView) findViewById(R.id.txt_emo);
        txt_sure= (TextView) findViewById(R.id.txt_sure);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_cam);
        init();

    }

    private void runThread(final int mSayi){

        Thread thread=null;
        thread= new Thread(){
            public void run(){
                int i=0;
                final int time=5;
                while (i++< 5){
                    try {
                        final int finalI = i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                txt_sure.setText(time- finalI +"");

                            }
                        });
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                MySurfaceCam.captureImage(mSayi);
                sayi++;

                //hesapla ve olumlu olumsuz döndür olumsuzsa sayıyı artırma
                //duruma göre bir sonraki take selfie layoutuna dön

            }
        };
        thread.start();

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
