package com.narcoding.actingaptitudetesting;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.narcoding.actingaptitudetesting.Model.Emoge;
import com.narcoding.actingaptitudetesting.emotion.EmotionServiceClient;
import com.narcoding.actingaptitudetesting.emotion.EmotionServiceRestClient;
import com.narcoding.actingaptitudetesting.emotion.contract.RecognizeResult;
import com.narcoding.actingaptitudetesting.emotion.rest.EmotionServiceException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static com.narcoding.actingaptitudetesting.MainActivity.emogesList;
import static com.narcoding.actingaptitudetesting.MainActivity.rotate;

/**
 * Created by Belgeler on 24.06.2017.
 */

public class Recognize extends AsyncTask<Emoge, String, List<RecognizeResult>> {

    private EmotionServiceClient client;
    private Emoge emo=new Emoge();
        // Store error message
    private Exception e = null;

    Context context;

    public Recognize(Context context) {
        this.context = context;
    }

    @Override
    protected List<RecognizeResult> doInBackground(Emoge... params) {
        this.emo=params[0];

        try {
            return processWithAutoFaceDetection(params[0].getUrl());
        } catch (Exception e) {
            Log.e("processerror",e.toString());
            this.e = e;    // Store error
        }

        return null;
    }

    @Override
        protected void onPostExecute(List<RecognizeResult> result) {
            super.onPostExecute(result);
            // Display based on error existence

            if (e != null) {
                //Log.e("Error: " , e.getMessage());
                this.e = null;
            } else {
                if (result.size() == 0) {
                    Log.e("sonresult","No emotion detected :(");

                    emo.setPercent(-1.0);


                } else {
                    Integer count = 1;

                    for (RecognizeResult r : result) {

                        switch (emo.getName()){
                            case "happiness":
                                emo.setPercent(r.scores.happiness);
                                break;
                            case "sadness":
                                emo.setPercent(r.scores.sadness);
                                break;
                            case "surprise":
                                emo.setPercent(r.scores.surprise);
                                break;
                            case "fear":
                                emo.setPercent(r.scores.fear);
                                break;
                            case "anger":
                                emo.setPercent(r.scores.anger);
                                break;
                            case "neutral":
                                emo.setPercent(r.scores.neutral);
                                break;
                            case "contempt":
                                emo.setPercent(r.scores.contempt);
                                break;
                            case "disgust":
                                emo.setPercent(r.scores.disgust);
                                break;
                        }


                        //ortalamasonuc=(happiness+sadness+surprise+fear+anger+neutral+contempt+disgust)*100/8;
                        //Log.e("sonuclar",
                        //        "happiness:"+happiness+"\n"+
                        //                "sadness:"+sadness+"\n"+
                        //                "surprise:"+surprise+"\n"+
                        //                "fear:"+fear+"\n"+
                        //                "anger:"+anger+"\n"+
                        //                "neutral:"+neutral+"\n"+
                        //                "contempt:"+contempt+"\n"+
                        //                "disgust:"+disgust+"\n"
                        //);
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

                        emo.setPercent(-1.0);

                    }



                }
                //mEditText.setSelection(0);
            }

        }



    private List<RecognizeResult> processWithAutoFaceDetection(String url) throws EmotionServiceException, IOException {
        Log.d("emotion", "Start emotion detection with auto-face detection");

        Gson gson = new Gson();

        Bitmap btm=Bitmap.createScaledBitmap(rotate(getImage(url), -90), emo.getHeight()/8, emo.getWidth()/8, false);

        // Put the image into an input stream for detection.
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        btm.compress(Bitmap.CompressFormat.JPEG, 100, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

        //long startTime = System.currentTimeMillis();

        // -----------------------------------------------------------------------
        // KEY SAMPLE CODE STARTS HERE
        // -----------------------------------------------------------------------

        List<RecognizeResult> result = null;
        //
        // Detect emotion by auto-detecting faces in the image.
        //

        if (client == null) {
            client = new EmotionServiceRestClient(context.getString(R.string.subscription_key));
        }

        result = this.client.recognizeImage(inputStream);
        Log.d("resultresult", result.toString());
        String json = gson.toJson(result);
        Log.d("resultjson", json);

        //Log.d("emotion", String.format("Detection done. Elapsed time: %d ms", (System.currentTimeMillis() - startTime)));
        // -----------------------------------------------------------------------
        // KEY SAMPLE CODE ENDS HERE
        // -----------------------------------------------------------------------
        return result;
    }

    private Bitmap getImage(String url){

        Bitmap mbitmap=null;
        BitmapFactory.Options options;
        String imageInSD = "/sdcard/"+url+".jpg";
        try {

            mbitmap = BitmapFactory.decodeFile(imageInSD);
            emo.setWidth(mbitmap.getWidth());
            emo.setHeight(mbitmap.getHeight());
            return mbitmap;
        } catch (OutOfMemoryError e) {

            options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            mbitmap = BitmapFactory.decodeFile(imageInSD, options);
            emo.setWidth(options.outWidth);
            emo.setHeight(options.outHeight);
            return mbitmap;
        }

    }


}
