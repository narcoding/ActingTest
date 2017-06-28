package com.narcoding.actingaptitudetesting;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.gson.Gson;
import com.narcoding.actingaptitudetesting.View.MainActivity;
import com.narcoding.actingaptitudetesting.emotion.EmotionServiceClient;
import com.narcoding.actingaptitudetesting.emotion.EmotionServiceRestClient;
import com.narcoding.actingaptitudetesting.emotion.contract.RecognizeResult;
import com.narcoding.actingaptitudetesting.emotion.rest.EmotionServiceException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class TestingActivity extends AppCompatActivity {

    private static final int image_REQUEST = 0;
    ImagePicker imagePicker=new ImagePicker();

    Button btn_takePhoto;
    Bitmap mBitmap;
    private int resultsayisi=0;
    private ProgressDialog progressBar;
    double anger,contempt, disgust,fear, happiness, neutral, sadness, surprise=0.0;
    double ortalamasonuc;
    private EmotionServiceClient client;

    private void init(){
        btn_takePhoto= (Button) findViewById(R.id.btn_takePhoto);
        btn_takePhoto.setText(getString(R.string.takeselfie)+""+ getText(R.string.happiness));
        if (client == null) {
            client = new EmotionServiceRestClient(getString(R.string.subscription_key));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        init();

        btn_takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(imagePicker.getPickImageIntent(TestingActivity.this),image_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==image_REQUEST){

        mBitmap = imagePicker.getImageFromResult(TestingActivity.this, resultCode, data);

            //Toast.makeText(this,mBitmap.toString(),Toast.LENGTH_LONG).show();
        doRecognize();

        }
    }


    public void doRecognize() {

        switch (resultsayisi){
            case 0:
                btn_takePhoto.setText(getText(R.string.takeselfie)+""+ getText(R.string.sadness));
                break;
            case 1:
                btn_takePhoto.setText(getText(R.string.takeselfie)+""+getText(R.string.surprise));
                break;
            case 2:
                btn_takePhoto.setText(getText(R.string.takeselfie)+""+getText(R.string.fear));
                break;
            case 3:
                btn_takePhoto.setText(getText(R.string.takeselfie)+""+getText(R.string.anger));
                break;
            case 4:
                btn_takePhoto.setText(getText(R.string.takeselfie)+""+getText(R.string.neutral));
                break;
            case 5:
                btn_takePhoto.setText(getText(R.string.takeselfie)+""+getText(R.string.contempt));
                break;
            case 6:
                btn_takePhoto.setText(getText(R.string.takeselfie)+""+getText(R.string.disgust));
                break;
        }
        resultsayisi++;

        btn_takePhoto.setEnabled(false);
        progressBar = ProgressDialog.show(TestingActivity.this, getString(R.string.bekle), getString(R.string.hesaplaniyor));


        // Do emotion detection using auto-detected faces.
        try {
            new doRequest().execute();
        } catch (Exception e) {
            Log.e("Error encountered", e.toString());
        }

    }


    private class doRequest extends AsyncTask<String, String, List<RecognizeResult>> {
        // Store error message
        private Exception e = null;


        public doRequest() {

        }

        @Override
        protected List<RecognizeResult> doInBackground(String... args) {

            try {
                return processWithAutoFaceDetection();
            } catch (Exception e) {
                this.e = e;    // Store error
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<RecognizeResult> result) {
            super.onPostExecute(result);
            // Display based on error existence

            if (e != null) {
                Log.e("Error: " , e.getMessage());
                this.e = null;
            } else {
                if (result.size() == 0) {
                    Log.e("sonresult","No emotion detected :(");

                    Toast.makeText(TestingActivity.this,R.string.alone,Toast.LENGTH_LONG).show();
                    resultsayisi--;
                    //Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //takePhotoIntent.putExtra("return-data", true);
                    //takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile(GetPhotosActivity.this)));

                    //startActivityForResult(takePhotoIntent,image_REQUEST);
                    //startActivityForResult(getPickImageIntent(GetPhotosActivity.this),image_REQUEST);
                    //GetPhotosActivityPermissionsDispatcher.startCameraWithCheck(GetPhotosActivity.this);
                    startActivityForResult(imagePicker.getPickImageIntent(TestingActivity.this),image_REQUEST);


                } else {
                    Integer count = 1;

                    for (RecognizeResult r : result) {

                        switch (resultsayisi){
                            case 1:
                                happiness=r.scores.happiness;
                                break;
                            case 2:
                                sadness=r.scores.sadness;
                                break;
                            case 3:
                                surprise=r.scores.surprise;
                                break;
                            case 4:
                                fear=r.scores.fear;
                                break;
                            case 5:
                                anger=r.scores.anger;
                                break;
                            case 6:
                                neutral=r.scores.neutral;
                                break;
                            case 7:
                                contempt=r.scores.contempt;
                                break;
                            case 8:
                                disgust=r.scores.disgust;
                                break;
                        }

                        ortalamasonuc=(happiness+sadness+surprise+fear+anger+neutral+contempt+disgust)*100/8;
                        Log.e("sonuclar",
                                "happiness:"+happiness+"\n"+
                                        "sadness:"+sadness+"\n"+
                                        "surprise:"+surprise+"\n"+
                                        "fear:"+fear+"\n"+
                                        "anger:"+anger+"\n"+
                                        "neutral:"+neutral+"\n"+
                                        "contempt:"+contempt+"\n"+
                                        "disgust:"+disgust+"\n"
                        );
                        //txt.append(String.format("\nFace #%1$d \n", count));
                        //txt.append(String.format("\t anger: %1$.5f\n", r.scores.anger));
                        //txt.append(String.format("\t contempt: %1$.5f\n", r.scores.contempt));
                        //txt.append(String.format("\t disgust: %1$.5f\n", r.scores.disgust));
                        //txt.append(String.format("\t fear: %1$.5f\n", r.scores.fear));
                        //txt.append(String.format("\t happiness: %1$.5f\n", r.scores.happiness));
                        //txt.append(String.format("\t neutral: %1$.5f\n", r.scores.neutral));
                        //txt.append(String.format("\t sadness: %1$.5f\n", r.scores.sadness));
                        //txt.append(String.format("\t surprise: %1$.5f\n", r.scores.surprise));
                        //txt.append(String.format("\t face rectangle: %d, %d, %d, %d", r.faceRectangle.left, r.faceRectangle.top, r.faceRectangle.width, r.faceRectangle.height));

                        count++;
                    }
                    if(count>2){

                        Toast.makeText(TestingActivity.this,R.string.alone,Toast.LENGTH_LONG).show();
                        resultsayisi--;
                        //Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //takePhotoIntent.putExtra("return-data", true);
                        //takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile(GetPhotosActivity.this)));

                        //startActivityForResult(getPickImageIntent(GetPhotosActivity.this),image_REQUEST);
                        //GetPhotosActivityPermissionsDispatcher.startCameraWithCheck(GetPhotosActivity.this);
                        startActivityForResult(imagePicker.getPickImageIntent(TestingActivity.this),image_REQUEST);

                    }

                    if(resultsayisi==8){
                        startActivity(new Intent(TestingActivity.this,MainActivity.class)
                                .putExtra("ortalama",ortalamasonuc)
                                .putExtra("happiness",happiness)
                                .putExtra("sadness",sadness)
                                .putExtra("surprise",surprise)
                                .putExtra("fear",fear)
                                .putExtra("anger",anger)
                                .putExtra("neutral",neutral)
                                .putExtra("contempt",contempt)
                                .putExtra("disgust",disgust)
                        );
                    }


                }
                //mEditText.setSelection(0);
            }

            btn_takePhoto.setEnabled(true);
            if (progressBar.isShowing()) {
                progressBar.dismiss();
            }
        }
    }


    private List<RecognizeResult> processWithAutoFaceDetection() throws EmotionServiceException, IOException {
        Log.d("emotion", "Start emotion detection with auto-face detection");

        Gson gson = new Gson();

        // Put the image into an input stream for detection.
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

        long startTime = System.currentTimeMillis();
        // -----------------------------------------------------------------------
        // KEY SAMPLE CODE STARTS HERE
        // -----------------------------------------------------------------------

        List<RecognizeResult> result = null;
        //
        // Detect emotion by auto-detecting faces in the image.
        //
        result = this.client.recognizeImage(inputStream);

        String json = gson.toJson(result);
        Log.d("result", json);

        Log.d("emotion", String.format("Detection done. Elapsed time: %d ms", (System.currentTimeMillis() - startTime)));
        // -----------------------------------------------------------------------
        // KEY SAMPLE CODE ENDS HERE
        // -----------------------------------------------------------------------
        return result;
    }


}
