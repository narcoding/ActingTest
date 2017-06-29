package com.narcoding.actingaptitudetesting.Model;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.narcoding.actingaptitudetesting.MyApp;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.narcoding.actingaptitudetesting.MyApp.emoslowercase;
import static com.narcoding.actingaptitudetesting.MyApp.savedname;

/**
 * Created by Belgeler on 29.06.2017.
 */

public class MySurfaceCam implements SurfaceHolder.Callback {

    public static Context con;
    public static Camera cam;
    public static SurfaceView sv;
    public static SurfaceHolder surfaceHolder;
    public static ShutterCallback shutterCallback;
    public static PictureCallback rawCallback,jpegCallback;



    public MySurfaceCam(Context con, SurfaceView sv) {
        MyApp.init(con);
        this.con=con;
        this.sv=sv;

        shutterCallback = new ShutterCallback() {
            public void onShutter() {
                Log.i("Log", "onShutter'd");
            }
        };
        rawCallback = new PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {

                Log.d("Log", "onPictureTaken - raw");
            }
        };


        surfaceHolder = sv.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public static void captureImage(final int emosListId) {

        jpegCallback = new PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream = null;
                try {
                    //outStream = new FileOutputStream(String.format("/sdcard/%d.jpg", System.currentTimeMillis()));
                    outStream = new FileOutputStream(String.format("/sdcard/%s.jpg", savedname+emoslowercase[emosListId]));
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

        // TODO Auto-generated method stub
        try {
            cam.takePicture(shutterCallback, rawCallback, jpegCallback);
        }catch (RuntimeException r){
            r.printStackTrace();
            Log.e("cameratake",r.getMessage());

        }

    }

    public static void start_camera()
    {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    Log.e("camopenfail", "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }



        Camera.Parameters param;
        param = cam.getParameters();

        //modify parameter
        param.setPreviewFrameRate(20);


        Camera.Size bestSize = null;

        List<Camera.Size> sizeList = cam.getParameters().getSupportedPreviewSizes();
        bestSize = sizeList.get(0);

        for(int i = 1; i < sizeList.size(); i++){
            if((sizeList.get(i).width * sizeList.get(i).height) >
                    (bestSize.width * bestSize.height)){
                bestSize = sizeList.get(i);
            }
        }

        param.setPreviewSize(bestSize.width, bestSize.height);
        cam.setDisplayOrientation(90);
        cam.setParameters(param);
        try {
            cam.setPreviewDisplay(surfaceHolder);
            cam.startPreview();
            //camera.takePicture(shutter, raw, jpeg)
        } catch (Exception e) {
            Log.e("previewcam", "init_camera: " + e);
            return;
        }
    }

    public static void stop_camera()
    {
        cam.stopPreview();
        cam.release();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        start_camera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
