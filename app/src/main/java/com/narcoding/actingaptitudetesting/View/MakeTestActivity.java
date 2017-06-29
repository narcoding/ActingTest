package com.narcoding.actingaptitudetesting.View;

import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.narcoding.actingaptitudetesting.MyApp;
import com.narcoding.actingaptitudetesting.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.narcoding.actingaptitudetesting.MyApp.emos;
import static com.narcoding.actingaptitudetesting.MyApp.emoslowercase;
import static com.narcoding.actingaptitudetesting.MyApp.savedname;

public class MakeTestActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    SurfaceView sv;
    TextView txt_cam_emo,txt_cam_time;

    Camera camera;
    SurfaceHolder surfaceHolder;
    PictureCallback rawCallback,jpegCallback;
    ShutterCallback shutterCallback;


    int i=0;
    int m=0;

    int time=5;


    Thread thread;

    private void init(){
        MyApp.init(this);
        sv= (SurfaceView) findViewById(R.id.sv);
        txt_cam_emo= (TextView) findViewById(R.id.txt_cam_emo);
        txt_cam_time= (TextView) findViewById(R.id.txt_cam_time);

        surfaceHolder = sv.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        rawCallback = new PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {

                Log.d("Log", "onPictureTaken - raw");
            }
        };

        /** Handles data for jpeg picture */
        shutterCallback = new ShutterCallback() {
            public void onShutter() {
                Log.i("Log", "onShutter'd");
            }
        };
        jpegCallback = new PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream = null;
                try {
                    //outStream = new FileOutputStream(String.format("/sdcard/%d.jpg", System.currentTimeMillis()));
                    outStream = new FileOutputStream(String.format("/sdcard/%s.jpg", savedname+emoslowercase[m-1]));
                    outStream.write(data);
                    outStream.close();
                    Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }
                Log.d("Log", "onPictureTaken - jpeg");
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_test);
        init();


    }

    private void captureImage() {
        // TODO Auto-generated method stub
        try {
            camera.takePicture(shutterCallback, rawCallback, jpegCallback);
        }catch (RuntimeException r){
            r.printStackTrace();
            Log.e("cameratake",r.getMessage());

        }

    }

    private void start_camera()
    {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    camera = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    Log.e("camopenfail", "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }



        Camera.Parameters param;
        param = camera.getParameters();

        //modify parameter
        param.setPreviewFrameRate(20);


        Camera.Size bestSize = null;

        List<Camera.Size> sizeList = camera.getParameters().getSupportedPreviewSizes();
        bestSize = sizeList.get(0);

        for(int i = 1; i < sizeList.size(); i++){
            if((sizeList.get(i).width * sizeList.get(i).height) >
                    (bestSize.width * bestSize.height)){
                bestSize = sizeList.get(i);
            }
        }

        param.setPreviewSize(bestSize.width, bestSize.height);
        camera.setDisplayOrientation(90);
        camera.setParameters(param);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
            //camera.takePicture(shutter, raw, jpeg)
        } catch (Exception e) {
            Log.e("previewcam", "init_camera: " + e);
            return;
        }
    }

    private void stop_camera()
    {
        camera.stopPreview();
        camera.release();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        start_camera();
        runThread();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stop_camera();
        finish();
    }

    private void runThread(){

        thread= new Thread(){
            public void run(){

                while (i++< 40){
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if(i>0){
                                    txt_cam_time.setText(time-i%5+"");
                                    if(i%5==0&&m<emos.length){
                                        captureImage();
                                        Toast.makeText(MakeTestActivity.this,"çekildi",Toast.LENGTH_SHORT).show();
                                        if(m<emos.length-1)
                                            txt_cam_emo.setText(emos[m+1]);
                                        m++;
                                    }else {
                                        //startActivity(new Intent(MakeTestActivity.this,MainActivity.class));
                                    }

                                }else{
                                    txt_cam_time.setText(time-i+"");
                                }

                            }
                        });
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

                startActivity(new Intent(MakeTestActivity.this,MainActivity.class).putExtra("girissayisi",1));

            }
        };
        thread.start();

    }





}
