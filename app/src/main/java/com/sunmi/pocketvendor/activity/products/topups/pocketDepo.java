package com.sunmi.pocketvendor.activity.products.topups;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.sunmi.pocketvendor.AppConn;
import com.sunmi.pocketvendor.R;
import com.sunmi.pocketvendor.activity.accountActivity;
import com.sunmi.pocketvendor.activity.navMainMenuActivity;
import com.sunmi.pocketvendor.activity.reportDetailActivity;
import com.sunmi.pocketvendor.network.Global;
import com.sunmi.pocketvendor.network.ResponseListener;
import com.sunmi.pocketvendor.network.WebAsyncTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class pocketDepo extends AppCompatActivity {

    ImageView generate_qr;
    EditText deposit_amount;
    AppConn appConn;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    String getStamp, did, dpin;
    String pdpin;
    //String depin;

    final Handler handler = new Handler();
    Timer timer = new Timer();

    private final static int INTERVAL = 100;
    int counter = 0;
    CountDownTimer loopcheck = new CountDownTimer(INTERVAL, 100){
        public void onTick(long mililisUntilFinished){
        }

        @Override
        public void onFinish() {
            counter = 0;
            System.out.println("==== loopcheck");

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocket_depo);

        generate_qr = findViewById(R.id.iv_qr);
        deposit_amount = findViewById(R.id.et_pckamt);
        pdpin = getIntent().getStringExtra("depin");
        System.out.println("=== depo pin : " + pdpin);

        // FRONT POS
        //depositcall();
        //pdpin = depin;

    }

    public void depositcall() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Getting Deposit");
        progressDialog.show();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://threegmedia.com/merchantpos/pocket/depositphone.php?phone=8885555").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressDialog.dismiss();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final okhttp3.Response response) throws IOException {
                if (response.isSuccessful()){
                    progressDialog.dismiss();
                    final String res = response.body().string();
                    pocketDepo.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("====> depo result : ", res);
                            System.out.println("=== depo result : " + res);

                            if (res.equals("false")){
                                //depin = "";
                            } else {
                                String[] p = res.split("=", -1);
                                //depin = p[1];
                            }

                        }
                    });

                } else {
                    progressDialog.dismiss();
                    System.out.println("=== depo result : reply fail");
                }

            }
        });
    }

    public void pocketBack(View view){
        loopcheck.cancel();
        exit();
    }

    public void PSubmit (View view){

        if (deposit_amount.getText().toString().isEmpty()){
            Toast.makeText(pocketDepo.this, "Enter your deposit amount", Toast.LENGTH_SHORT).show();

        } else {

            //
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Loading");
            progressDialog.show();

            OkHttpClient client = new OkHttpClient();
            //Request request = new Request.Builder().url("https://pocket.com.bn/wallet/cashierregister.php?mid=10001&terminal=001&phone=8885555&amount=" + deposit_amount.getText().toString()).build(); //mid=33333 phone=8138346
            //Log.e("====>", "https://pocket.com.bn/wallet/cashierregister.php?mid=10001&terminal=001&phone=8885555&amount=" + deposit_amount.getText().toString());
            Request request = new Request.Builder().url("https://www.mybillpayment.com/wallet/cashierregister.php?phone=8885555&pin=" + pdpin + "&amount=" + deposit_amount.getText().toString()).build(); //mid=33333 phone=8138346
            //Log.e("====>", "https://www.mybillpayment.com/wallet/cashierregister.php?phone=8885555&pin=" + pdpin + "&amount=" + deposit_amount.getText().toString());
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                    Log.e("====> Result : ", "QR Error");
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    if (response.isSuccessful()){
                        progressDialog.dismiss();
                        final String res = response.body().string();
                        pocketDepo.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("====> Result : ", res);
                                getStamp = res;
                                //Toast.makeText(pocketDepo.this, "Reply : " + res, Toast.LENGTH_SHORT).show();

                                try {
                                    MultiFormatWriter mfw = new MultiFormatWriter();
                                    BarcodeEncoder be = new BarcodeEncoder();
                                    BitMatrix bm = mfw.encode(res, BarcodeFormat.QR_CODE, 400, 400);
                                    bitmap = be.createBitmap(bm);
                                    generate_qr.setImageBitmap(bitmap);
                                    progressDialog.dismiss();


                                    String dp[] = getStamp.split("<and>");
                                    if (dp.length > 0){
                                        for (String dps : dp){
                                            String dep[] = dps.split("<eq>");
                                            Log.e("--> pair: ", dps);

                                            if (dep[0].equals("depositid")){
                                                did = dep[1];
                                            } else if (dep[0].equals("depositpin")){
                                                dpin = dep[1];
                                            }

                                        }
                                    }

                                    //

                                    TimerTask doAsynchronousTask = new TimerTask() {
                                        @Override
                                        public void run() {
                                            handler.post(new Runnable() {
                                                public void run() {
                                                    try {
                                                        //your method here
                                                        cashiercheck();
                                                        //
                                                    } catch (Exception e) {
                                                    }
                                                }
                                            });
                                        }
                                    };
                                    timer.schedule(doAsynchronousTask, 0, 2000);
                                    //


                                } catch (WriterException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                    }

                }
            });

            //





        }

    }

    private void cashiercheck(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://www.mybillpayment.com/wallet/cashiercheck.php?depositid=" + did + "&depositpin=" + dpin).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressDialog.dismiss();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()){
                    final String receipt = response.body().string();
                    pocketDepo.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("====> Result : ", receipt);

                            if (receipt.contains("<stamped>")){
                                timer.cancel();
                                startActivity(new Intent(pocketDepo.this, pocketDepo2.class)
                                        .putExtra("stamp", receipt));
                            }

                        }
                    });

                }

            }
        });
    }

    private void exit(){
        timer.cancel();
        final ProgressDialog pdialog = new ProgressDialog(pocketDepo.this);
        pdialog.setMessage("Exiting, Please wait...");
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setCancelable(false);
        pdialog.show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(pocketDepo.this);
        //
        appConn = new AppConn();
        appConn.versionBuild = Global.VER;
        appConn.merchant_id = preferences.getString("m_id", null);
        appConn.terminal_id = preferences.getString("t_id", null);
        appConn.terminal_imei = preferences.getString("t_imei", null);
        appConn.terminal_pin = preferences.getString("t_pin", null);
        appConn.uuid = preferences.getString("uuid", null);
        appConn.webLink = Global.URL;
        appConn.commandpost = "login";
        appConn.cardAccess(new ResponseListener() {
            @Override
            public void onResponse(com.sunmi.pocketvendor.network.Response response) {
                pdialog.dismiss();
                if (appConn.cardFailedcode != null){
                    finish();
                } else {
                    startActivity(new Intent(pocketDepo.this, navMainMenuActivity.class)
                            .putExtra("sPower"      , appConn.res_powerinstant)
                            .putExtra("sPCSB"       , appConn.res_progresif)
                            .putExtra("sPCSBZ"       , appConn.res_zoom)
                            .putExtra("sEasi"       , appConn.res_easi)
                            .putExtra("sGooglePlayUS", appConn.res_googleplayus)
                            .putExtra("sITunesUS"   , appConn.res_itunesus)
                            .putExtra("sTelbruPrepaidWIFI"   , appConn.res_telbruprepaidwifi)

                            .putExtra("statuspi"    , appConn.stat_powerinstant)
                            .putExtra("statusp"     , appConn.stat_progresif)
                            .putExtra("statuspz"     , appConn.stat_zoom)
                            .putExtra("statuse"     , appConn.stat_easi)
                            .putExtra("statusgp"    , appConn.stat_googleplayus)
                            .putExtra("statusit"    , appConn.stat_itunesus)
                            .putExtra("statustbpw"   , appConn.sta_telbruprepaidwifi));
                    finish();
                }
            }

            @Override
            public void onError(String error) {
                pdialog.dismiss();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        exit();
    }
}
