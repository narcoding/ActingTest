package com.narcoding.actingaptitudetesting.View;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
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

import com.narcoding.actingaptitudetesting.Controller.OnCaptureCompleted;
import com.narcoding.actingaptitudetesting.Controller.ResultsWaitingAsync;
import com.narcoding.actingaptitudetesting.R;
import com.narcoding.actingaptitudetesting.emotion.rest.RecognizeImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.narcoding.actingaptitudetesting.MyApp.emogesList;
import static com.narcoding.actingaptitudetesting.MyApp.rotate;
import static com.narcoding.actingaptitudetesting.MyApp.savedname;

public class EndActivity extends Activity {

    TableLayout tbllayout;
    TextView textView;
    Button btn_basla;
    ImageButton imgbtn_paylas;
    private ProgressDialog progressBar;
    File shareimagePath;

    RecognizeImage recognizeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        tbllayout = findViewById(R.id.tbllayout);
        textView = findViewById(R.id.textView);
        btn_basla = findViewById(R.id.btn_basla);
        imgbtn_paylas = findViewById(R.id.imgbtn_paylas);

        btn_basla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EndActivity.this, TestCamActivity.class));
            }
        });

        if (getImages(0) != null) {
            progressBar = ProgressDialog.show(EndActivity.this, getString(R.string.bekle), getString(R.string.hesaplaniyor));
        }

        imgbtn_paylas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = takeScreenshot();
                saveBitmap(bitmap);
                shareIt();
            }
        });

        recognizeImage = new RecognizeImage();

        new ResultsWaitingAsync(new OnCaptureCompleted() {
            @Override
            public void onCaptured(Boolean comp) {
                AdapterTbllayout();
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    public void saveBitmap(Bitmap bitmap) {
        shareimagePath = new File(Environment.getExternalStorageDirectory() + "/" + savedname + "screenshot.png");
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
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody + "\n" + shareapplink);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(sharingIntent, getString(R.string.sharewith)));
    }


    private void AdapterTbllayout() {

        tbllayout.removeAllViews();

        int z1 = 0;
        for (int t = 0; t < 2; t++) {
            TableRow tblrow = new TableRow(this);
            tblrow.setGravity(Gravity.CENTER);
            tblrow.setHorizontalGravity(Gravity.CENTER);
            tblrow.setVerticalGravity(Gravity.CENTER);
            for (int j = 0; j < 4; j++) {
                LinearLayout ll = new LinearLayout(this);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.setGravity(Gravity.CENTER);
                ll.setHorizontalGravity(Gravity.CENTER);
                ll.setVerticalGravity(Gravity.CENTER);

                ImageView img = new ImageView(this);
                img.setPadding(1, 1, 1, 1);

                TextView txt = new TextView(this);
                txt.setGravity(Gravity.CENTER);
                txt.setTextSize(12);

                TextView txtscore = new TextView(this);
                txtscore.setGravity(Gravity.CENTER);
                txtscore.setTextSize(12);
                txtscore.setTextColor(Color.RED);

                Bitmap mImage = getImages(z1);
                if (mImage != null) {
                    //textView.setVisibility(View.GONE);
                    //img.setImageBitmap(rotate(getImages(t),-90));
                    Bitmap nbtm = Bitmap.createScaledBitmap(rotate(mImage, -90), setCustomHeight(emogesList.get(z1).getWidth(), emogesList.get(z1).getHeight()), setCustomWidth(emogesList.get(z1).getWidth()), false);
                    img.setImageBitmap(nbtm);
                    txt.setText(emogesList.get(z1).emotion);
                    if (emogesList.get(z1).getPercent() != null) {
                        if (emogesList.get(z1).getPercent() * 100 > 100.0)
                            emogesList.get(z1).setPercent(1.0);
                        if (emogesList.get(z1).getPercent() * 100 < 0)
                            emogesList.get(z1).setPercent(0.0);
                        txtscore.setText("%" + Math.floor(emogesList.get(z1).getPercent() * 100));
                    }
                    ll.addView(img);
                    ll.addView(txt);
                    ll.addView(txtscore);
                    z1++;
                    ll.setBackgroundResource(R.drawable.list_shadow1);
                    tblrow.addView(ll);

                } else {
                    tbllayout.setVisibility(View.GONE);
                }
            }

            tbllayout.addView(tblrow);
        }

        //if (emogesList.get(0).getPercent() != null
        //        && emogesList.get(1).getPercent() != null
        //        && emogesList.get(2).getPercent() != null
        //        && emogesList.get(3).getPercent() != null
        //        && emogesList.get(4).getPercent() != null
        //        && emogesList.get(5).getPercent() != null
        //        && emogesList.get(6).getPercent() != null
        //        && emogesList.get(7).getPercent() != null
        //) {

        for (int ii = 0; ii < 8; ii++) {
            if (emogesList.get(ii).getPercent() == null)
                emogesList.get(ii).setPercent(0.0);
        }

        double ortalama = (emogesList.get(0).getPercent()
                + emogesList.get(1).getPercent()
                + emogesList.get(2).getPercent()
                + emogesList.get(3).getPercent()
                + emogesList.get(4).getPercent()
                + emogesList.get(5).getPercent()
                + emogesList.get(6).getPercent()
                + emogesList.get(7).getPercent()) * 100 / 8;

        //if (ortalama != 0.0) {
        if (ortalama > 80) {
            textView.setText(getString(R.string.pekiyi) + Math.floor(ortalama));
        } else if (ortalama > 60) {
            textView.setText(getString(R.string.iyi) + Math.floor(ortalama));
        } else if (ortalama > 40) {
            textView.setText(getString(R.string.orta) + Math.floor(ortalama));
        } else if (ortalama > 20) {
            textView.setText(getString(R.string.zayif) + Math.floor(ortalama));
        } else if (ortalama >= 0) {
            textView.setText(getString(R.string.kotu) + Math.floor(ortalama));
        }

        btn_basla.setText(R.string.tekrar);
        btn_basla.setEnabled(true);
        imgbtn_paylas.setVisibility(View.VISIBLE);

        //}

        if (progressBar.isShowing()) {
            progressBar.dismiss();

        }

        //}

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

    private int getDisplayWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //int height = displayMetrics.heightPixels;
        return displayMetrics.widthPixels;
    }

    private int getDisplayHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //int width = displayMetrics.widthPixels;
        return displayMetrics.heightPixels;
    }

    private int setCustomWidth(int imageWidth) {
        int targetWidth = getDisplayWidth() / 4;

        return imageWidth / (imageWidth / targetWidth);
    }

    private int setCustomHeight(int imageWidth, int imageHeight) {
        int targetWidth = getDisplayWidth() / 4;

        return imageHeight / (imageWidth / targetWidth);
    }
}
