package com.narcoding.actingaptitudetesting.View;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.narcoding.actingaptitudetesting.Controller.OnCaptureCompleted;
import com.narcoding.actingaptitudetesting.Model.MySurfaceCam;
import com.narcoding.actingaptitudetesting.R;

import static com.narcoding.actingaptitudetesting.MyApp.REQUEST_PERMISSIONS;

public class TestCamActivity extends RuntimePermissionsActivity implements SurfaceHolder.Callback {

    private LinearLayout ll_sf;
    private LinearLayout ll_takeselfie;
    private LinearLayout ll_sure;
    public SurfaceView sv_testcam;
    private Button btn_go;
    private Button btn_takeselfie;
    private TextView txt_takeselfie;
    private TextView txt_emo;
    private TextView txt_sure;

    MySurfaceCam mySurfaceCam;

    private static int sayi = 0;

    private void init() {
        ll_sf = findViewById(R.id.ll_sf);
        ll_takeselfie = findViewById(R.id.ll_takeselfie);
        ll_sure = findViewById(R.id.ll_sure);
        sv_testcam = findViewById(R.id.sv_testcam);
        mySurfaceCam = new MySurfaceCam(sv_testcam);

        btn_go = findViewById(R.id.btn_go);
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_sf.setVisibility(View.GONE);
                ll_takeselfie.setVisibility(View.VISIBLE);
            }
        });

        txt_takeselfie = findViewById(R.id.txt_takeselfie);

        String txt = getText(R.string.happiness).toString();
        switch (sayi){
            case 1:
                txt = getText(R.string.sadness).toString();
                break;
            case 2:
                txt = getText(R.string.surprise).toString();
                break;
            case 3:
                txt = getText(R.string.fear).toString();
                break;
            case 4:
                txt = getText(R.string.anger).toString();
                break;
            case 5:
                txt = getText(R.string.neutral).toString();
                break;
            case 6:
                txt = getText(R.string.contempt).toString();
                break;
            case 7:
                txt = getText(R.string.disgust).toString();
                break;
            default:
                break;

        }

        txt_takeselfie.setText(getText(R.string.takeselfie) + "" + txt);

        btn_takeselfie = findViewById(R.id.btn_takeselfie);
        btn_takeselfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_takeselfie.setVisibility(View.GONE);
                ll_sure.setVisibility(View.VISIBLE);

                runThread(sayi);

            }
        });

        txt_emo = findViewById(R.id.txt_emo);
        txt_emo.setText(txt);
        txt_sure = findViewById(R.id.txt_sure);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_cam);
        init();

        if (Build.VERSION.SDK_INT >= 23) {
            TestCamActivity.super.requestAppPermissions(new
                            String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    R.string.runtimepermission
                    , REQUEST_PERMISSIONS);
        }

    }

    @Override
    public void onPermissionsGranted(int requestCode) {

        mySurfaceCam.start_camera();
    }

    private void runThread(final int mSayi) {

        Thread thread = new Thread() {
            public void run() {
                int i = 0;
                final int time = 5;
                while (i++ < 5) {
                    try {
                        final int finalI = i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                txt_sure.setText(time - finalI + "");

                            }
                        });
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mySurfaceCam.captureImage(mSayi, new OnCaptureCompleted() {
                    @Override
                    public void onCaptured(Boolean comp) {
                        mySurfaceCam.stop_camera();
                        sayi++;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (sayi == 8){
                            startActivity(new Intent(TestCamActivity.this, EndActivity.class));
                        } else {
                            mySurfaceCam.start_camera();
                            ll_takeselfie.setVisibility(View.VISIBLE);
                            ll_sure.setVisibility(View.GONE);
                            init();
                        }
                    }
                });


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
