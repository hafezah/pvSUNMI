package com.sunmi.pocketvendor.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Vibrator;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.sunmi.pocketvendor.R;
import com.sunmi.pocketvendor.activity.products.topups.easiActivity;
import com.sunmi.pocketvendor.activity.products.topups.googleplayActivity;
import com.sunmi.pocketvendor.activity.products.topups.itunesActivity;
import com.sunmi.pocketvendor.activity.products.topups.powerInstantActivity;
import com.sunmi.pocketvendor.activity.products.topups.progresifActivity;
import com.sunmi.pocketvendor.activity.products.topups.telbruActivity;
import com.sunmi.pocketvendor.utils.FinderView;
import com.sunmi.pocketvendor.utils.SoundUtils;
import com.sunmi.scan.Config;
import com.sunmi.scan.Image;
import com.sunmi.scan.ImageScanner;
import com.sunmi.scan.Symbol;
import com.sunmi.scan.SymbolSet;

public class qrActivity extends Activity implements SurfaceHolder.Callback {
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private SurfaceView surface_view;
    private ImageScanner scanner;// declare a scanner
    private Handler autoFocusHandler;
    private AsyncDecode asyncDecode;
    SoundUtils soundUtils;
    private boolean vibrate;
    public int decode_count = 0;

    String prod, ref, sms, pid, pemail;
    String getPow, getPro, getZoom, getEas, getGoo, getItu, getTPW, serPower, serPcsb, serZoom, serEasi, serGoogle, serItunes, serTPW;

    private FinderView finder_view;
    private TextView textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        init();
    }

    private void init() {
        surface_view = findViewById(R.id.surface_view);
        finder_view = findViewById(R.id.finder_view);
        textview = findViewById(R.id.textview);
        mHolder = surface_view.getHolder();
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mHolder.addCallback(this);

        scanner = new ImageScanner();// create scanner
        scanner.setConfig(0, Config.X_DENSITY, 2);          // Line scanning interval
        scanner.setConfig(0, Config.Y_DENSITY, 2);          // Column scanning interval
        scanner.setConfig(0, Config.ENABLE_MULTILESYMS, 0); // Whether to identify several code,0 means identify one，1 means identify multiple code
        scanner.setConfig(0, Config.ENABLE_INVERSE, 0);     // Whether to identify inverse code
        scanner.setConfig(Symbol.PDF417, Config.ENABLE, 0);    // Disable decode PDF417 code, default enable.

        autoFocusHandler = new Handler();
        asyncDecode = new AsyncDecode();
        decode_count = 0;

        serPower    = getIntent().getStringExtra("sPower");
        serPcsb     = getIntent().getStringExtra("sPcsb");
        serZoom     = getIntent().getStringExtra("sZoom");
        serEasi     = getIntent().getStringExtra("sEasi");
        serGoogle   = getIntent().getStringExtra("sGoogleUS");
        serItunes   = getIntent().getStringExtra("sItunesUS");
        serTPW      = getIntent().getStringExtra("sTPW");

        getPow      = getIntent().getStringExtra("gPower");
        getPro      = getIntent().getStringExtra("gPcsb");
        getZoom     = getIntent().getStringExtra("gZoom");
        getEas      = getIntent().getStringExtra("gEasi");
        getGoo      = getIntent().getStringExtra("gGoogleUS");
        getItu      = getIntent().getStringExtra("gItunesUS");
        getTPW      = getIntent().getStringExtra("gTPW");
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            mCamera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if (mHolder.getSurface() == null) {
            return;
        }
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
        }
        try {
            //camera preview resolution and  image zooming settings, nonobligatory
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(800, 480); // set preview resolution
            // parameters.set("zoom", String.valueOf(27 / 10.0));// Image magnification factor of 2 . 7
            mCamera.setParameters(parameters);
            // //////////////////////////////////////////
            mCamera.setDisplayOrientation(90);// vertical showing
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setPreviewCallback(previewCallback);
            mCamera.startPreview();
            mCamera.autoFocus(autoFocusCallback);
        } catch (Exception e) {
            Log.d("DBG", "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private void initBeepSound() {
        if (soundUtils == null) {
            soundUtils = new SoundUtils(this, SoundUtils.RING_SOUND);
            soundUtils.putSound(0, R.raw.beep);
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initBeepSound();
        vibrate = false;
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (soundUtils != null) {
            soundUtils.playSound(0, SoundUtils.SINGLE_PLAY);
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            if (asyncDecode.isStoped()) {
                Camera.Parameters parameters = camera.getParameters();
                Camera.Size size = parameters.getPreviewSize();// getting the preview resolution

                //creating decode image , transform to source data,the image has been  rotated 90 degrees
                Image source = new Image(size.width, size.height, "Y800");
                Rect scanImageRect = finder_view.getScanImageRect(size.height,
                        size.width);
                //image has been rotated 90 degrees, crop the image
                source.setCrop(scanImageRect.top, scanImageRect.left,scanImageRect.height(), scanImageRect.width());
                source.setData(data);// filling data
                asyncDecode = new AsyncDecode();
                asyncDecode.execute(source);// decoding the code asynchronously
            }
        }
    };

    private class AsyncDecode extends AsyncTask<Image, Void, Void> {
        private boolean stopped = true;
        private String str = "";

        @Override
        protected Void doInBackground(Image... params) {
            stopped = false;
            StringBuilder sb = new StringBuilder();
            Image src_data = params[0];// getting the source data

            long startTimeMillis = System.currentTimeMillis();

            // decoding ，return value 0 means failure，>0 means successful
            int nsyms = scanner.scanImage(src_data);

            long endTimeMillis = System.currentTimeMillis();
            long cost_time = endTimeMillis - startTimeMillis;

            if (nsyms != 0) {
                playBeepSoundAndVibrate();// play prompt tone after scanning

                decode_count++;
                sb.append("count: " + String.valueOf(decode_count) + ", cost time: "
                        + String.valueOf(cost_time) + " ms \n");

                SymbolSet syms = scanner.getResults();// getting the result
                for (Symbol sym : syms) {
                    sb.append("[ " + sym.getSymbolName() + " ]: "
                            + sym.getResult() + "\n");
                }
            }
            str = sb.toString();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            stopped = true;

            if (null == str || str.equals("")) {

            } else {
                //textview.setText(str);// showing the result
                qrVerification(str);
                finish();
                //
            }
        }

        public boolean isStoped() {
            return stopped;
        }
    }

    private void qrVerification(String qrcontents){
        String qrString = qrcontents.substring(41, qrcontents.length());
        if (qrString.contains("product<eq>")){

            String and[] = qrString.toLowerCase().split("<and>", -1);

            for (int i = 0; i<and.length; i++){
                String pairs[] = and[i].split("<eq>", -1);

                if (pairs[0].equals("product")){
                    prod = pairs[1];
                } else if (pairs[0].equals("ref")){
                    ref = pairs[1];
                } else if (pairs[0].equals("smsto")){
                    sms = pairs[1];
                } else if (pairs[0].equals("phone")){
                    pid = pairs[1];
                } else if (pairs[0].equals("email")){
                    pemail = pairs[1];
                }
            }

            System.out.println("prod  : " + prod);
            System.out.println("ref   : " + ref);
            System.out.println("smsto : " + sms);
            System.out.println("phone : " + pid);

            if (prod.equals("powerinstant")){
                if (serPower.equals("no") || getPow.equals("no")){
                    Toast.makeText(qrActivity.this, "PowerInstant service currently unavailable", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(qrActivity.this, powerInstantActivity.class)
                            .putExtra("qrMeter", ref)
                            .putExtra("qrSms" , sms)
                            .putExtra("pid", pid)
                            .putExtra("pmail", pemail));
                    finish();
                }

            } else if (prod.equals("pcsb")){
                if (serPcsb.equals("no") || getPro.equals("no")){
                    Toast.makeText(qrActivity.this, "Progresif TopUp service currently unavailable", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(qrActivity.this, progresifActivity.class)
                            .putExtra("qrPCSBno", ref)
                            .putExtra("pid", pid)
                            .putExtra("pmail", pemail)
                            .putExtra("title", "PROGRESIF TOPUP")
                            .putExtra("pcsbtype", "PCSB"));
                    finish();
                }

            } else if (prod.equals("pcsbzoom")){
                if (serZoom.equals("no") || getZoom.equals("no")){
                    Toast.makeText(qrActivity.this, "Progresif Zoom service currently unavailable", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(qrActivity.this, progresifActivity.class)
                            .putExtra("qrPCSBno", ref)
                            .putExtra("pid", pid)
                            .putExtra("pmail", pemail)
                            .putExtra("title", "PROGRESIF ZOOM")
                            .putExtra("pcsbtype", "pcsbzoom"));
                    finish();
                }

            } else if (prod.equals("easi")) {
                if (serEasi.equals("no") || getEas.equals("no")) {
                    Toast.makeText(qrActivity.this, "Easi Top Up service currently unavailable", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(qrActivity.this, easiActivity.class)
                            .putExtra("qrEASIno", ref)
                            .putExtra("pid", pid)
                            .putExtra("pmail", pemail));
                    finish();
                }
            } else if (prod.equals("googleplayus")){
                if (serGoogle.equals("no") || getGoo.equals("no")){
                    Toast.makeText(qrActivity.this, "GooglePlay(US) service currently unavailable", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(qrActivity.this, googleplayActivity.class)
                            .putExtra("qrGOOGLEno", ref)
                            .putExtra("pid", pid)
                            .putExtra("pmail", pemail));
                    finish();
                }
            } else if (prod.equals("itunesus")){
                if (serItunes.equals("no") || getItu.equals("no")){
                    Toast.makeText(qrActivity.this, "iTunes(US) service currently unavailable", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(qrActivity.this, itunesActivity.class)
                            .putExtra("qrITUNESno", ref)
                            .putExtra("pid", pid)
                            .putExtra("pmail", pemail));
                    finish();
                }
            } else if (prod.equals("telbruprepaidwifi")){
                if (serTPW.equals("no") || getTPW.equals("no")){
                    Toast.makeText(qrActivity.this, "Telbru Prepaid Wifi service currently unavailable", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(qrActivity.this, telbruActivity.class)
                            .putExtra("product", "prepaidwifi")
                            .putExtra("qrTPWno", ref)
                            .putExtra("pid", pid)
                            .putExtra("pmail", pemail));
                    finish();
                }
            }

        } else {
            Toast.makeText(qrActivity.this, "Invalid QR Code", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (null == mCamera || null == autoFocusCallback) {
                return;
            }
            mCamera.autoFocus(autoFocusCallback);
        }
    };
}
