package com.narcoding.actingaptitudetesting.View;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.narcoding.actingaptitudetesting.Model.Emoge;
import com.narcoding.actingaptitudetesting.R;
import com.narcoding.actingaptitudetesting.emotion.EmotionServiceClient;
import com.narcoding.actingaptitudetesting.emotion.EmotionServiceRestClient;
import com.narcoding.actingaptitudetesting.emotion.contract.RecognizeResult;
import com.narcoding.actingaptitudetesting.emotion.rest.EmotionServiceException;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.narcoding.actingaptitudetesting.MyApp.REQUEST_PERMISSIONS;
import static com.narcoding.actingaptitudetesting.MyApp.emogesList;
import static com.narcoding.actingaptitudetesting.MyApp.rotate;
import static com.narcoding.actingaptitudetesting.MyApp.savedname;

public class MainActivity extends RuntimePermissionsActivity {
    TextView textView;
    Button btn_basla;
    ImageButton imgbtn_paylas;
    LinearLayout llmain;
    TableLayout tbllayout;

    private ProgressDialog progressBar;

    int z=0;
    int girissayisi=0;

    File shareimagePath;

    private void init(){
        textView= (TextView) findViewById(R.id.textView);
        btn_basla= (Button) findViewById(R.id.btn_basla);
        imgbtn_paylas= (ImageButton) findViewById(R.id.imgbtn_paylas);
        llmain= (LinearLayout) findViewById(R.id.llmain);
        tbllayout= (TableLayout) findViewById(R.id.tbllayout);

        imgbtn_paylas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = takeScreenshot();
                saveBitmap(bitmap);
                shareIt();
            }
        });

    }

    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    public void saveBitmap(Bitmap bitmap) {
        shareimagePath = new File(Environment.getExternalStorageDirectory() + "/"+savedname+"screenshot.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(shareimagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }

    private void shareIt() {
        Uri uri = Uri.fromFile(shareimagePath);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        String shareBody = getString(R.string.sharebody);
        String shareapplink = getString(R.string.shareapplink);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.sharescore));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody+"\n"+shareapplink);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(sharingIntent, getString(R.string.sharewith)));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        if (Build.VERSION.SDK_INT >= 23) {
            MainActivity.super.requestAppPermissions(new
                            String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA},
                    R.string.runtimepermission
                    , REQUEST_PERMISSIONS);
        }

        if(getIntent().getIntExtra("girissayisi",0)!=-1){
        girissayisi=getIntent().getIntExtra("girissayisi",0);
        }
        if(girissayisi!=0){

        if (getImages(0) != null) {
            progressBar = ProgressDialog.show(MainActivity.this, getString(R.string.bekle), getString(R.string.hesaplaniyor));
        }


        for (int t=0;t<2;t++){
            for(int j=0;j<4;j++) {
                if (getImages(t) != null) {
                    btn_basla.setEnabled(false);
                    new Recognize(this).execute(emogesList.get(z));
                    z++;
                }
                else {
                    tbllayout.setVisibility(View.GONE);
                }
            }

        }

        }


/*
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
*/
        btn_basla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this,TestingActivity.class));
                startActivity(new Intent(MainActivity.this,MakeTestActivity.class));

                //finish();
            }
        });

    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    private void AdapterTbllayout(){

        tbllayout.removeAllViews();

        int z1=0;
        for (int t=0;t<2;t++){
            TableRow tblrow=new TableRow(this);
            tblrow.setGravity(Gravity.CENTER);
            tblrow.setHorizontalGravity(Gravity.CENTER);
            tblrow.setVerticalGravity(Gravity.CENTER);
            for(int j=0;j<4;j++) {
                LinearLayout ll=new LinearLayout(this);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.setGravity(Gravity.CENTER);
                ll.setHorizontalGravity(Gravity.CENTER);
                ll.setVerticalGravity(Gravity.CENTER);

                ImageView img = new ImageView(this);
                img.setPadding(1,1,1,1);

                TextView txt=new TextView(this);
                txt.setGravity(Gravity.CENTER);
                txt.setTextSize(12);

                TextView txtscore=new TextView(this);
                txtscore.setGravity(Gravity.CENTER);
                txtscore.setTextSize(12);
                txtscore.setTextColor(Color.RED);

                if (getImages(t) != null) {

                    //textView.setVisibility(View.GONE);
                    //img.setImageBitmap(rotate(getImages(t),-90));
                    Bitmap nbtm=Bitmap.createScaledBitmap(rotate(getImages(z1), -90), emogesList.get(z1).getHeight()/8, emogesList.get(z1).getWidth()/8, false);
                    img.setImageBitmap(nbtm);
                    txt.setText(emogesList.get(z1).emotion);
                    if(emogesList.get(z1).getPercent()!=null) {
                        if(emogesList.get(z1).getPercent()*100>100.0)
                            emogesList.get(z1).setPercent(1.0);
                        if(emogesList.get(z1).getPercent()*100<0)
                            emogesList.get(z1).setPercent(0.0);
                        txtscore.setText("%" + Math.floor(emogesList.get(z1).getPercent() * 100));
                    }
                    ll.addView(img);
                    ll.addView(txt);
                    ll.addView(txtscore);
                    z1++;
                    ll.setBackgroundResource(R.drawable.list_shadow1);
                    tblrow.addView(ll);

                }
                else {
                    tbllayout.setVisibility(View.GONE);
                }
            }

            tbllayout.addView(tblrow);
        }

        if(emogesList.get(0).getPercent()!=null
                &&emogesList.get(1).getPercent()!=null
                &&emogesList.get(2).getPercent()!=null
                &&emogesList.get(3).getPercent()!=null
                &&emogesList.get(4).getPercent()!=null
                &&emogesList.get(5).getPercent()!=null
                &&emogesList.get(6).getPercent()!=null
                &&emogesList.get(7).getPercent()!=null
                ){

        Double ortalama=(emogesList.get(0).getPercent()
                +emogesList.get(1).getPercent()
                +emogesList.get(2).getPercent()
                +emogesList.get(3).getPercent()
                +emogesList.get(4).getPercent()
                +emogesList.get(5).getPercent()
                +emogesList.get(6).getPercent()
                +emogesList.get(7).getPercent())*100/8;

        if (ortalama!=0.0){
            if(ortalama>80) {
                textView.setText(getString(R.string.pekiyi)+Math.floor(ortalama));
            }else if (ortalama>60){
                textView.setText(getString(R.string.iyi)+Math.floor(ortalama));
            }else if (ortalama>40){
                textView.setText(getString(R.string.orta)+Math.floor(ortalama));
            }else if (ortalama>20){
                textView.setText(getString(R.string.zayif)+Math.floor(ortalama));
            }else if (ortalama>0){
                textView.setText(getString(R.string.kotu)+Math.floor(ortalama));
            }

            btn_basla.setText(R.string.tekrar);
            btn_basla.setEnabled(true);
            imgbtn_paylas.setVisibility(View.VISIBLE);

            if (progressBar.isShowing()) {
                progressBar.dismiss();

            }

        }


        }

    }

    private Bitmap getImages(int k){

        Bitmap mbitmap=null;
        Uri followUri= Uri.parse("/sdcard/"+emogesList.get(k).getUrl()+".jpg");
        if (followUri != null && !followUri.equals(Uri.EMPTY)) {

        BitmapFactory.Options options;
            File f = new File(String.valueOf(followUri));
            if(f.length()!=0){

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



        //Bitmap bmp=null;
        //Uri followUri= Uri.parse("/sdcard/"+emogesList.get(k).getUrl()+".jpg");
        //if (followUri != null && !followUri.equals(Uri.EMPTY)) {
        //    //doTheThing()
        //    File f = new File(String.valueOf(followUri));
        //    BitmapFactory.Options options = new BitmapFactory.Options();
        //    options.inJustDecodeBounds = true;
        //    bmp = BitmapFactory.decodeFile(f.getPath(),options);
        //    emogesList.get(k).setWidth(options.outWidth);
        //    emogesList.get(k).setHeight(options.outHeight);
        //} else {
        //    //followUri is null or empty
        //    bmp=null;
        //}

        //return bmp;
        return mbitmap;
    }


    class Recognize extends AsyncTask<Emoge, String, List<RecognizeResult>> {

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
                                emo.setPercent(r.scores.happiness*100);
                                break;
                            case "sadness":
                                emo.setPercent(r.scores.sadness*100);
                                break;
                            case "surprise":
                                emo.setPercent(r.scores.surprise*100);
                                break;
                            case "fear":
                                emo.setPercent(r.scores.fear*100);
                                break;
                            case "anger":
                                emo.setPercent(r.scores.anger*100);
                                break;
                            case "neutral":
                                emo.setPercent(r.scores.neutral*100);
                                break;
                            case "contempt":
                                emo.setPercent(r.scores.contempt*100);
                                break;
                            case "disgust":
                                emo.setPercent(r.scores.disgust*100);
                                break;
                        }


                            AdapterTbllayout();


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

}