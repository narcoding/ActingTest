package com.narcoding.actingaptitudetesting.emotion.rest;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Emotion;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.FaceRectangle;
import com.narcoding.actingaptitudetesting.Controller.OnCaptureCompleted;
import com.narcoding.actingaptitudetesting.Controller.OnRecognizeCompleted;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class RecognizeImage {
    private final String apiEndpoint = "https://myfaceapinaim.cognitiveservices.azure.com/face/v1.0/";
    private final String subscriptionKey = "5bb8c8a4d9744ed3a88810c802d2cb48";

    private final FaceServiceClient faceServiceClient =
            new FaceServiceRestClient(apiEndpoint, subscriptionKey);

    public void detectAndFrame(final Bitmap imageBitmap, OnRecognizeCompleted completed) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(outputStream.toByteArray());

        @SuppressLint("StaticFieldLeak") AsyncTask<InputStream, String, Face[]> detectTask =
                new AsyncTask<InputStream, String, Face[]>() {
                    String exceptionMessage = "";

                    @Override
                    protected Face[] doInBackground(InputStream... params) {
                        try {

                            FaceServiceClient.FaceAttributeType[] fatype = new FaceServiceClient.FaceAttributeType[] {
                                    FaceServiceClient.FaceAttributeType.Emotion};

                            publishProgress("Detecting...");
                            Face[] result = faceServiceClient.detect(
                                    params[0],
                                    true,         // returnFaceId
                                    false,        // returnFaceLandmarks
                                    fatype          // returnFaceAttributes:
                                    /* new FaceServiceClient.FaceAttributeType[] {
                                        FaceServiceClient.FaceAttributeType.Age,
                                        FaceServiceClient.FaceAttributeType.Gender }
                                    */
                            );
                            if (result == null){
                                //publishProgress("Detection Finished. Nothing detected");
                                return null;
                            }
                            //publishProgress(String.format("Detection Finished. %d face(s) detected", result.length));
                            return result;
                        } catch (Exception e) {
                            exceptionMessage = String.format(
                                    "Detection failed: %s", e.getMessage());
                            return null;
                        }
                    }

                    @Override
                    protected void onPreExecute() {
                        //TODO: show progress dialog

                    }
                    @Override
                    protected void onProgressUpdate(String... progress) {
                        //TODO: update progress

                    }
                    @Override
                    protected void onPostExecute(Face[] result) {
                        //TODO: update face frames


                        if(!exceptionMessage.equals("")){
                            //showError(exceptionMessage);
                        }
                        if (result.length != 0){
                            completed.onResult(result[0].faceAttributes.emotion);
                        } else {
                            completed.onResult(new Emotion());
                        }


                        //ImageView imageView = findViewById(R.id.imageView1);
                        //imageView.setImageBitmap(drawFaceRectanglesOnBitmap(imageBitmap, result));
                        //imageBitmap.recycle();
                    }
                };

        detectTask.execute(inputStream);
    }

    private static Bitmap drawFaceRectanglesOnBitmap(
            Bitmap originalBitmap, Face[] faces) {
        Bitmap bitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        if (faces != null) {
            for (Face face : faces) {
                FaceRectangle faceRectangle = face.faceRectangle;
                canvas.drawRect(
                        faceRectangle.left,
                        faceRectangle.top,
                        faceRectangle.left + faceRectangle.width,
                        faceRectangle.top + faceRectangle.height,
                        paint);
            }
        }
        return bitmap;
    }

}
