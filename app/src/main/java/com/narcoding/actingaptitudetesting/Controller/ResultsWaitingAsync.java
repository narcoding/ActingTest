package com.narcoding.actingaptitudetesting.Controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import com.microsoft.projectoxford.face.contract.Emotion;
import com.narcoding.actingaptitudetesting.Model.Emoge;
import com.narcoding.actingaptitudetesting.emotion.rest.RecognizeImage;

import java.io.File;

import static com.narcoding.actingaptitudetesting.MyApp.emogesList;
import static com.narcoding.actingaptitudetesting.MyApp.rotate;

public class ResultsWaitingAsync extends AsyncTask<Void, Void, Void> {

    OnCaptureCompleted completed;
    RecognizeImage recognizeImage;

    public ResultsWaitingAsync(OnCaptureCompleted completed) {
        this.completed = completed;
        recognizeImage = new RecognizeImage();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        final int[] count = {0};
        for (int i = 0; i < 8; i++) {
            Bitmap btm = rotate(getImages(i), -90);
            int finalI = i;
            recognizeImage.detectAndFrame(btm, new OnRecognizeCompleted() {
                @Override
                public void onResult(Emotion emotion) {

                    Emoge emo = emogesList.get(finalI);
                    switch (emo.getName()) {
                        case "happiness":
                            emo.setPercent(emotion.happiness * 100);
                            break;
                        case "sadness":
                            emo.setPercent(emotion.sadness * 100);
                            break;
                        case "surprise":
                            emo.setPercent(emotion.surprise * 100);
                            break;
                        case "fear":
                            emo.setPercent(emotion.fear * 100);
                            break;
                        case "anger":
                            emo.setPercent(emotion.anger * 100);
                            break;
                        case "neutral":
                            emo.setPercent(emotion.neutral * 100);
                            break;
                        case "contempt":
                            emo.setPercent(emotion.contempt * 100);
                            break;
                        case "disgust":
                            emo.setPercent(emotion.disgust * 100);
                            break;
                    }
                    count[0]++;
                }
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (count[0] < 7) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return null;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        completed.onCaptured(true);
    }

    private Bitmap getImages(int k) {

        Bitmap mbitmap = null;
        Uri followUri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/" + emogesList.get(k).getUrl() + ".jpg");
        if (followUri != null && !followUri.equals(Uri.EMPTY)) {

            BitmapFactory.Options options;
            File f = new File(String.valueOf(followUri));
            if (f.length() != 0) {

                try {

                    mbitmap = BitmapFactory.decodeFile(String.valueOf(followUri));
                    emogesList.get(k).setWidth(mbitmap.getWidth());
                    emogesList.get(k).setHeight(mbitmap.getHeight());
                    return mbitmap;
                } catch (OutOfMemoryError e) {

                    options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    mbitmap = BitmapFactory.decodeFile(String.valueOf(followUri), options);
                    emogesList.get(k).setWidth(options.outWidth);
                    emogesList.get(k).setHeight(options.outHeight);
                    return mbitmap;
                }
            }
        }

        return mbitmap;
    }

}
